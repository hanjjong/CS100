package pnu.cs100.solvedExam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.cs100.exam.Exam;
import pnu.cs100.member.Member;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolvedExamServiceImpl implements SolvedExamService {

    private final SolvedExamRepository solvedExamRepository;
    @Transactional
    public void createOrUpdateSolvedExam(Exam exam, Member member) {
        // Member 객체를 통해 이미 풀었던 Exam을 가져옴
        Optional<SolvedExam> existingSolvedExam = member.getSolvedExams().stream()
                .filter(solvedExam -> solvedExam.getExam().equals(exam))
                .findFirst();

        if (existingSolvedExam.isPresent()) {
            // 기존 데이터가 존재하면 업데이트
            // updated_at 필드만 변경하면 되므로 save를 다시 호출하여 갱신
            SolvedExam solvedExamToUpdate = existingSolvedExam.get();
            solvedExamRepository.save(solvedExamToUpdate);
        } else {
            // 기존 데이터가 없으면 새로 생성
            SolvedExam newSolvedExam = SolvedExam.builder()
                    .member(member)
                    .exam(exam)
                    .build();
            solvedExamRepository.save(newSolvedExam);  // 연관관계 편의 메서드를 사용할 수도 있음
        }
    }
}
