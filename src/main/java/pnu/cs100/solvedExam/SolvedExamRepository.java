package pnu.cs100.solvedExam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedExamRepository extends JpaRepository<SolvedExam, Long> {
}
