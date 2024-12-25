package pnu.cs100.member.dto;

public record UpdateMemberRequest(Long id, String email, String password, String name) {
}
