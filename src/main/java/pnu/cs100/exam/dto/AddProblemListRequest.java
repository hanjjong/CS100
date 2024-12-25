package pnu.cs100.exam.dto;

import pnu.cs100.problem.dto.ProblemRequest;
import java.util.List;

public record AddProblemListRequest(String examTitle,
                                    String examGrade,
                                    String examDescription,
                                    Long limitSecond,
                                    List<ProblemRequest> problems) {
}
