package pnu.cs100.examProblem;

import jakarta.persistence.*;
import lombok.*;
import pnu.cs100.BaseEntity;
import pnu.cs100.exam.Exam;
import pnu.cs100.problem.Problem;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExamProblem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem-id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam-id")
    private Exam exam;

    public ExamProblem(Exam exam, Problem problem) {
        this.exam = exam;
        this.problem = problem;
    }
}
