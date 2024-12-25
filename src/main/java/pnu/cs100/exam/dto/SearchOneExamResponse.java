package pnu.cs100.exam.dto;

import lombok.Builder;
import pnu.cs100.exam.Exam;

@Builder
public record SearchOneExamResponse(Long id,
                                    String title,
                                    String description,
                                    String grade,
                                    Long limit_second,
                                    Long count) {

    public static SearchOneExamResponse from(Exam exam){
        return SearchOneExamResponse.builder().id(exam.getId())
                .description(exam.getDescription())
                .title(exam.getTitle())
                .grade(exam.getGrade())
                .limit_second(exam.getLimitSecond())
                .count((long) exam.getExamProblems().size())
                .build();
    }
}
