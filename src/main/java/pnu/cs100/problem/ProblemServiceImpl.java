package pnu.cs100.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.cs100.member.Member;
import pnu.cs100.member.MemberServiceImpl;
import pnu.cs100.problem.dto.CreateProblemRequest;
import pnu.cs100.problem.dto.SearchProblemResponse;
import pnu.cs100.problem.dto.UpdateProblemRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final MemberServiceImpl memberService;

    @Transactional
    public void createProblem(CreateProblemRequest createProblemRequest) {
        Optional<Member> author = memberService.findById(createProblemRequest.authorId());
        author.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Problem problem = Problem.builder()
                .content(createProblemRequest.content())
                .category(createProblemRequest.category())
                .type(createProblemRequest.type())
                .difficulty(createProblemRequest.difficulty())
                .member(author.get())
                .difficulty(createProblemRequest.difficulty())
                .correctAnswer(createProblemRequest.correctAnswer())
                .reference(createProblemRequest.reference())
                .build();
        problemRepository.save(problem);
    }

    @Transactional
    public void updateProblem(UpdateProblemRequest updateProblemRequest) {
        Optional<Problem> problem = problemRepository.findById(updateProblemRequest.id());
        problem.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));
        if (updateProblemRequest.member_id() != null) {
            Optional<Member> newAuthor = memberService.findById(updateProblemRequest.member_id());
            newAuthor.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
            problem.get().setMember(newAuthor.get());
        }
        if (updateProblemRequest.content() != null)
            problem.get().setContent(updateProblemRequest.content());
        if (updateProblemRequest.category() != null)
            problem.get().setCategory(updateProblemRequest.category());
        if (updateProblemRequest.correct_answer() != null)
            problem.get().setCorrectAnswer(updateProblemRequest.correct_answer());
        if (updateProblemRequest.type() != null)
            problem.get().setType(updateProblemRequest.type());
        if (updateProblemRequest.reference() != null)
            problem.get().setReference(updateProblemRequest.reference());
        if (updateProblemRequest.difficulty() != null)
            problem.get().setDifficulty(updateProblemRequest.difficulty());
    }

    @Transactional
    public List<SearchProblemResponse> searchAllProblem() {
        List<Problem> problemList = problemRepository.findAll();

        List<SearchProblemResponse> responseList = problemList.stream()
                .map(problem -> SearchProblemResponse.builder()
                        .id(problem.getId())
                        .authorName(problem.getMember().getName())
                        .category(problem.getCategory())
                        .content(problem.getContent())
                        .type(problem.getType())
                        .difficulty(problem.getDifficulty())
                        .createdAt(problem.getCreatedAt())
                        .correctAnswer(problem.getCorrectAnswer())
                        .reference(problem.getReference())
                        .build())
                .toList();
        return responseList;
    }

    @Transactional
    public SearchProblemResponse searchProblem(Long id) {
        Problem problem = searchOneProblem(id);

        return SearchProblemResponse.builder()
                .id(problem.getId())
                .authorName(problem.getMember().getName())
                .category(problem.getCategory())
                .content(problem.getContent())
                .type(problem.getType())
                .difficulty(problem.getDifficulty())
                .createdAt(problem.getCreatedAt())
                .correctAnswer(problem.getCorrectAnswer())
                .build();
    }

    public Problem searchOneProblem(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다."));
    }

    @Transactional
    public void deleteProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다."));
        problem.setDeleted(true);
    }
}
