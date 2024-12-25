package pnu.cs100.answer;

import jakarta.persistence.*;
import lombok.*;
import pnu.cs100.BaseEntity;
import pnu.cs100.member.Member;
import pnu.cs100.problem.Problem;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Answer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(length = 300, nullable = false)
    private String content;

    @Column()
    private Integer score;

}
