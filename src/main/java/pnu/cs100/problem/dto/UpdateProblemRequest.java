package pnu.cs100.problem.dto;

import pnu.cs100.member.Member;

public record UpdateProblemRequest(Long id,
                                   Long member_id,
                                   String content,
                                   String category,
                                   String type,
                                   String reference,
                                   Long difficulty,
                                   String correct_answer) {
}
