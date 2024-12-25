package pnu.cs100.exam.dto;

public record CreateExamResquest(String title,
                                 String grade,
                                 String description,
                                 Long limit_second) {
}
