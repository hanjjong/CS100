package pnu.cs100.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.cs100.exam.dto.*;
import pnu.cs100.examProblem.ExamProblem;
import pnu.cs100.examProblem.ExamProblemRepository;
import pnu.cs100.member.Member;
import pnu.cs100.member.MemberRepository;
import pnu.cs100.problem.Problem;
import pnu.cs100.problem.ProblemRepository;
import pnu.cs100.problem.dto.SearchProblemResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final ExamProblemRepository examProblemRepository;

    @Transactional
    public void createExam(CreateExamResquest createExamResquest) {
        Exam exam = Exam.builder()
                .title(createExamResquest.title())
                .grade(createExamResquest.grade())
                .description(createExamResquest.description())
                .limitSecond(createExamResquest.limit_second())
                .build();
        examRepository.save(exam);
    }

    @Transactional
    public SearchExamResponse searchExam() {
        List<Exam> examList = examRepository.findAll();
        List<SearchOneExamResponse> examDtos = examList.stream()
                .map(SearchOneExamResponse::from)
                .toList();
        return SearchExamResponse.builder()
                .exams(examDtos).build();
    }

    @Transactional
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제집입니다."));
        ;
        exam.setDeleted(true);
    }

    @Transactional
    public SearchExamListRequest searchOneExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제집입니다."));

        List<SearchProblemResponse> searchProblemResponses = exam.getExamProblems().stream()
                .map(examProblem -> {
                    Problem problem = examProblem.getProblem();
                    return SearchProblemResponse.builder()
                            .id(problem.getId())
                            .content(problem.getContent())
                            .reference(problem.getReference())
                            .type(problem.getType())
                            .reference(problem.getReference())
                            .authorName(problem.getMember().getName())
                            .category(problem.getCategory())
                            .difficulty(problem.getDifficulty())
                            .build();
                })
                .collect(Collectors.toList());

        return SearchExamListRequest.builder()
                .examId(exam.getId())
                .title(exam.getTitle())
                .descreption(exam.getDescription())
                .grade(exam.getGrade())
                .limitSecond(exam.getLimitSecond())
                .count((long) exam.getExamProblems().size())
                .problems(searchProblemResponses)
                .build();
    }

    @Transactional
    public SearchExamResponse searchCanSolveExam(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        // 멤버가 이미 푼 시험의 ID 리스트를 가져옴
        List<Long> solvedExamIds = member.getSolvedExams().stream()
                .map(solvedExam -> solvedExam.getExam().getId())
                .toList();

        // 모든 시험 중 멤버가 풀지 않은 시험만 필터링
        List<SearchOneExamResponse> examDtos = examRepository.findAll().stream()
                .filter(exam -> !solvedExamIds.contains(exam.getId()))  // 풀지 않은 시험만 남김
                .map(SearchOneExamResponse::from)                       // DTO로 변환
                .toList();

        // SearchExamResponse로 반환
        return SearchExamResponse.builder()
                .exams(examDtos)
                .build();
    }

    @Transactional
    public void addProblemList(AddProblemListRequest addProblemListRequest) {
        Exam exam = Exam.builder()
                .title(addProblemListRequest.examTitle())
                .grade(addProblemListRequest.examGrade())
                .description(addProblemListRequest.examDescription())
                .limitSecond(addProblemListRequest.limitSecond())
                .build();
        examRepository.save(exam);

        addProblemListRequest.problems().forEach(problemRequest -> {
            Problem problem = problemRepository.findById(problemRequest.problemId()).orElseThrow();

            // ExamProblem 객체를 생성하여 Exam과 Problem의 관계를 설정
            ExamProblem examProblem = ExamProblem.builder()
                    .exam(exam)
                    .problem(problem)
                    .build();
            examProblemRepository.save(examProblem);
        });
    }
}
