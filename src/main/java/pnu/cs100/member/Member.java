package pnu.cs100.member;


import jakarta.persistence.*;
import lombok.*;
import pnu.cs100.BaseEntity;
import pnu.cs100.answer.Answer;
import pnu.cs100.problem.Problem;
import pnu.cs100.solvedExam.SolvedExam;

import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Member extends BaseEntity {

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 40, nullable = false)
    private String email;

    @Column(length = 200, nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Problem> problems;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SolvedExam> solvedExams;

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }
}
