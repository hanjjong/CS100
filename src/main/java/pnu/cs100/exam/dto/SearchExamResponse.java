package pnu.cs100.exam.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchExamResponse(List<SearchOneExamResponse> exams) {
}
