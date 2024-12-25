package pnu.cs100.examProblem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamProblemRepository extends JpaRepository<ExamProblem, Long> {
}
