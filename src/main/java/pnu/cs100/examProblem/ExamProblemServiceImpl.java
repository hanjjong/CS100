package pnu.cs100.examProblem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.cs100.exam.Exam;
import pnu.cs100.exam.ExamRepository;
import pnu.cs100.examProblem.dto.CreateExamProblemRequest;
import pnu.cs100.examProblem.dto.DeleteExamProblem;
import pnu.cs100.problem.Problem;
import pnu.cs100.problem.ProblemRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamProblemServiceImpl implements ExamProblemService {

    private final ExamRepository examRepository;
    private final ProblemRepository problemRepository;
    private final ExamProblemRepository examProblemRepository;

    @Transactional
    public void createExamProblem(CreateExamProblemRequest createExamProblemRequest) {
        Optional<Exam> exam = examRepository.findById(createExamProblemRequest.examId());
        exam.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 exam ID 입니다."));

        Optional<Problem> problem = problemRepository.findById(createExamProblemRequest.problemId());
        problem.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

        exam.get().addProblem(problem.get());
        ExamProblem examProblem = ExamProblem.builder()
                .exam(exam.get())
                .problem(problem.get())
                .build();
        examProblemRepository.save(examProblem);
    }

    @Transactional
    public void deleteExamProblem(DeleteExamProblem deleteExamProblem) {
        Optional<Exam> exam = examRepository.findById(deleteExamProblem.examId());
        exam.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 exam ID 입니다."));

        Optional<Problem> problem = problemRepository.findById(deleteExamProblem.problemId());
        problem.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제입니다."));

        exam.get().removeProblem(problem.get());
        ExamProblem examProblem = ExamProblem.builder()
                .exam(exam.get())
                .problem(problem.get())
                .build();
        examProblemRepository.delete(examProblem);
    }
}
