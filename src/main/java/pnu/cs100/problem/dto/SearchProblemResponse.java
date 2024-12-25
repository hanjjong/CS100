package pnu.cs100.problem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchProblemResponse(Long id,
                                    String content,
                                    String type,
                                    String reference,
                                    String authorName,
                                    String correctAnswer,
                                    String category,
                                    Long difficulty,
                                    LocalDateTime createdAt) {
}
