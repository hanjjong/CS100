package pnu.cs100.answer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pnu.cs100.answer.dto.*;
import pnu.cs100.exam.Exam;
import pnu.cs100.exam.ExamRepository;
import pnu.cs100.member.Member;
import pnu.cs100.member.MemberRepository;
import pnu.cs100.problem.Problem;
import pnu.cs100.problem.ProblemRepository;
import pnu.cs100.solvedExam.SolvedExamServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService {

    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final ExamRepository examRepository;
    private final SolvedExamServiceImpl solvedExamService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;

    @Transactional
    public void createAnswer(CreateAnswerRequest createAnswerRequest) {
        Member member = memberRepository.findById(createAnswerRequest.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
        Exam exam = examRepository.findById(createAnswerRequest.examId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제집입니다."));

        //각 답안을 확인하고, answer에 답안 저장
        createAnswerRequest.answers().forEach(answerData -> {
            // Problem ID를 이용해 문제를 찾음
            Problem problem = problemRepository.findById(answerData.problemId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

            // 해당 회원이 해당 문제에 대해 이미 답변을 제출했는지 확인
            Optional<Answer> existingAnswer = answerRepository.findByMemberAndProblem(member, problem);

            if (existingAnswer.isPresent()) {
                // 이미 답변이 존재하면 업데이트
                Answer answerToUpdate = existingAnswer.get();
                answerToUpdate.setContent(answerData.content());
                answerRepository.save(answerToUpdate);
            } else {
                // 답변이 존재하지 않으면 새로 생성하여 저장
                Answer newAnswer = Answer.builder()
                        .member(member)
                        .problem(problem)
                        .content(answerData.content())
                        .build();
                answerRepository.save(newAnswer);
            }
        });
        //시험 제출 했다면 해당 멤버가 시험을 푼것을 디비에 기록
        solvedExamService.createOrUpdateSolvedExam(exam, member);
    }

    @Transactional
    public GradeAnswerResponse gradeAnswer(GradeAnswerRequest gradeAnswerRequest) throws JsonProcessingException {
        Exam exam = examRepository.findById(gradeAnswerRequest.examId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제집입니다."));
        Member member = memberRepository.findById(gradeAnswerRequest.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        List<String> problemContents = exam.getExamProblems().stream()
                .map(examProblem -> examProblem.getProblem().getContent())
                .toList();

        List<String> answerContents = member.getAnswers().stream()
                .filter(answer ->
                        exam.getExamProblems().stream()
                                .anyMatch(examProblem -> examProblem.getProblem().equals(answer.getProblem()))
                )
                .map(Answer::getContent)
                .toList();
        GradeAnswerResponse gradeAnswerResponse = gradeAnswerToGpt(problemContents, answerContents);

        member.getAnswers().forEach(answer -> {
            gradeAnswerResponse.getQuestions().forEach(gradedQuestion -> {
                if (answer.getProblem().getId().equals(gradedQuestion.getQuestionNumber())) {
                    answer.setScore(gradedQuestion.getScore());
                }
            });
        });
        return gradeAnswerResponse;
    }

    @Retryable(
            value = {JsonMappingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    private GradeAnswerResponse gradeAnswerToGpt(List<String> problemContents, List<String> answerContents) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("너는 IT 기술 면접을 도와주는 선생님이야. 나는 너에게 여러 개의 IT 기술 문제와 사용자 답안을 줄거야. ")
                .append("너는 각 문제에 대한 사용자의 답변의 내용이 정확한지, 면접 질문의 답안으로 꼭 필요하다고 생각하는 내용이 들어가 있는지를 판단해서 없으면 점수를 완전 낮게 주고 100점 만점으로 점수를 계산해서 채점해줘. 아예 문제와 관련이 없는 사용자 답변이라면 0점을 줘도 돼. 점수를 너무 후하게 주지는 않았으면 좋겠어. ")
                .append("그리고 answer키에 대한 value로는 사용자의 답안을 적지말고 해당 문제에 대한 너가 생각하는 모범 답안을 작성해줘. ")
                .append("그리고 feedback은 사용자의 답안이 모범답안에 비해서 어떤 내용이 부족하고, 어떤 부분을 더 공부해야할지 설명해주고, 사용자가 모르겠다고 답변을 하면 어떤 부분을 공부해야 할지 알려줘. 사용자가 잘 작성했다면 잘 작성해주었다고 말해줘 ")
                .append("해당 응답은 JSON 형식으로만 응답해줘. ")
                .append("각각의 JSON 객체는 다음과 같은 키를 가져야 해:\n")
                .append("- question_number: 문제 번호 (정수)\n")
                .append("- score: 점수 (정수)\n")
                .append("- answer: 너가 생각하는 각 문제에 대한 모범 답안 (문자열)\n")
                .append("- feedback: 아주 상세한 피드백 및 부연 설명 (문자열)\n\n")
                .append("응답은 아래와 같은 형식을 따라야 해:\n")
                .append("```json\n")
                .append("{\n")
                .append("    \"questions\": [\n")
                .append("        {\n")
                .append("            \"question_number\": 1,\n")
                .append("            \"score\": 100,\n")
                .append("            \"answer\": \"트랜잭션이란 데이터베이스의 논리적 흐름으로 한번에 처리되는 데이터베이스 처리 흐름이라고 할 수 있습니다.\",\n")
                .append("            \"feedback\": \"해당 답안의 개념적인 설명을 추가적으로 명시해주면 좋을 것 같습니다. 중요한 내용인 ACID에 대한 설명을 추가로 하면 좋을 것 같습니다.\"\n")
                .append("        },\n")
                .append("        {\n")
                .append("            \"question_number\": 2,\n")
                .append("            \"score\": 100,\n")
                .append("            \"answer\": \"비정규화란 데이터베이스의 성능 및 조회 성능을 개선하기 위해서 데이터베이스 정규화에 반하는 조치를 취하는 것을 뜻합니다.\",\n")
                .append("            \"feedback\": \"비정규화에 대한 답안이 모호합니다. 조금 더 구체적인 답안이 좋습니다.\"\n")
                .append("        }\n")
                .append("    ]\n")
                .append("}\n")
                .append("```\n")
                .append("각 문제와 사용자의 답안은 다음과 같은 형식으로 제공될 거야:\n");

        for (int i = 0; i < problemContents.size(); i++) {
            String answer = answerContents.get(i);
            if (answer.equals("."))
                continue;
            promptBuilder.append("<").append(i + 1).append("번 문제>").append(problemContents.get(i)).append("\n");
            promptBuilder.append("<").append(i + 1).append("번 문제의 사용자 답안>").append(answerContents.get(i)).append("\n");
        }

        String prompt = promptBuilder.toString();

        ChatGptRequest request = new ChatGptRequest(model, prompt);
        ChatGptResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGptResponse.class);

        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            throw new RuntimeException("GPT API 응답이 비어있습니다.");
        }

        String gptAnswer = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        String extractJson = extractJsonFromResponse(gptAnswer);
        try {
            // JSON 데이터 로깅 (로깅 프레임워크 사용 권장)
            log.info("GPT 응답 JSON: " + extractJson);
            return objectMapper.readValue(extractJson, GradeAnswerResponse.class);
        } catch (JsonProcessingException e) {
            // 매핑 오류 상세 로그
            log.error("JSON 파싱 오류: " + e.getMessage());
            throw e; // @Retryable에 의해 재시도됨
        }
    }

    private String extractJsonFromResponse(String gptAnswer) {
        int start = gptAnswer.indexOf("```json");
        int end = gptAnswer.indexOf("```", start + 7);
        if (start != -1 && end != -1) {
            return gptAnswer.substring(start + 7, end).trim();
        }
        return gptAnswer.trim(); // 코드 블록이 없을 경우 전체 응답 사용
    }

    @Recover
    public GradeAnswerResponse recover(JsonProcessingException e, List<String> problemContents, List<String> answerContents) {
        // 재시도 실패 시 기본 응답을 반환하거나 예외를 던질 수 있음
        log.error("재시도 실패: JSON 파싱 오류가 계속 발생했습니다.");
        GradeAnswerResponse response = new GradeAnswerResponse();
        response.setQuestions(new ArrayList<>());
        // 추가적인 필드 설정
        return response;
    }

//    private GradeAnswerResponse GptjsonParsing(String gptAnswer) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(gptAnswer, GradeAnswerResponse.class);
//    }
}
