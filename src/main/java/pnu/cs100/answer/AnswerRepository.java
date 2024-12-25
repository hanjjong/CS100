package pnu.cs100.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pnu.cs100.member.Member;
import pnu.cs100.problem.Problem;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByMemberAndProblem(Member member, Problem problem);
}
