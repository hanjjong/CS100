package pnu.cs100.exam.dto;

import lombok.Builder;
import pnu.cs100.problem.dto.SearchProblemResponse;

import java.util.List;

@Builder
public record SearchExamListRequest(Long examId,
        String title,
        String descreption,
        String grade,
        Long limitSecond,
        Long count,

        List<SearchProblemResponse> problems) {
}
