package pnu.cs100.exam;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import pnu.cs100.BaseEntity;
import pnu.cs100.examProblem.ExamProblem;
import pnu.cs100.problem.Problem;
import pnu.cs100.solvedExam.SolvedExam;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Exam extends BaseEntity {

    @OneToMany(mappedBy = "exam")
    private List<ExamProblem> examProblems;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<SolvedExam> solvedExams;

    @Column(length = 200, nullable = true)
    private String title;

    @Column(length = 200, nullable = true)
    private String description;

    @Column(length = 200, nullable = true)
    private String grade;

    @Column(length = 200, nullable = true)
    private Long limitSecond;

    public void addProblem(Problem problem) {
        ExamProblem examProblem = new ExamProblem(this, problem);
        examProblems.add(examProblem);
        problem.getExamProblems().add(examProblem);
    }

    public void removeProblem(Problem problem) {
        ExamProblem examProblem = new ExamProblem(this, problem);
        examProblems.remove(examProblem);
        problem.getExamProblems().remove(examProblem);
    }
}
