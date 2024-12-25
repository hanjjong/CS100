package pnu.cs100.problem;

import jakarta.persistence.*;
import lombok.*;
import pnu.cs100.BaseEntity;
import pnu.cs100.answer.Answer;
import pnu.cs100.examProblem.ExamProblem;
import pnu.cs100.member.Member;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Problem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "problem")
    private List<Answer> answer;

    @OneToMany(mappedBy = "problem")
    private List<ExamProblem> examProblems;

    @Column(length = 200)
    private String reference;

    @Column(length = 30, nullable = false)
    private String type;

    @Column(length = 30, nullable = false)
    private String category;

    @Column(length = 500, nullable = false)
    private String content;

    @Column(length = 200, nullable = false)
    private Long difficulty;

    @Column(length = 500, nullable = false)
    private String correctAnswer;

    @Setter
    @Column(columnDefinition = "boolean default false")
    private boolean checked;
}
