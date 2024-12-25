package pnu.cs100.problem.dto;

public record CreateProblemRequest(String title,
                                   String content,
                                   String type,
                                   String category,
                                   String reference,
                                   Long authorId,
                                   Long difficulty,
                                   String correctAnswer) {
}
