package pnu.cs100.member.dto;

import lombok.Builder;

@Builder
public record SearchMemberResponse(Long memberId,
                                   String memberName,
                                   String email) {
}
