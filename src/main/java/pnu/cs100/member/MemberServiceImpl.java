package pnu.cs100.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.cs100.member.dto.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void createMember(SignUpRequest signUpRequest) {
        if (findOneMemberByEmail(signUpRequest.email()).isPresent())
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        Member member = Member.builder()
                .email(signUpRequest.email())
                .name(signUpRequest.name())
                .password(signUpRequest.password())
                .build();
        memberRepository.save(member);
    }

    public Optional<Member> findOneMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest, HttpSession session) {
        Optional<Member> loginRequestMember = findOneMemberByEmail(signInRequest.email());
        Member signedInMember = loginRequestMember
                .orElseThrow(() -> new IllegalArgumentException("가입되어 있지 않은 회원입니다."));

        checkMemberPassword(signedInMember, signInRequest.password());
        session.setAttribute("member", signedInMember); //세션에 멤버 셋팅
        return new SignInResponse(signedInMember.getId());
    }

    public void checkMemberPassword(Member member, String inputPassword) {
        if (!member.checkPassword(inputPassword)) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }
    }

    @Transactional
    public void updateMember(UpdateMemberRequest updateMemberRequest, Member sessionMember) {
        Member member = memberRepository.findById(updateMemberRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("가입되어 있지 않은 회원입니다."));
        validateSameMember(member, sessionMember);
        String updateName = updateMemberRequest.name();
        String updateEmail = updateMemberRequest.email();
        String updatePassword = updateMemberRequest.password();

        Optional<Member> optionalMember = memberRepository.findMemberByEmail(updateEmail);
        checkDuplicateEmail(optionalMember);

        member.setName(updateName);
        member.setEmail(updateEmail);
        member.setPassword(updatePassword);
    }

    private void checkDuplicateEmail(Optional<Member> optionalMember) {
        if (optionalMember.isPresent()) {
            throw new IllegalArgumentException("중복 이메일");
        }
    }

    public void deleteMember(DeleteMemberRequest deleteMemberRequest, Member sessionMember) {
        Member member = memberRepository.findById(deleteMemberRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        validateSameMember(member, sessionMember);
        member.setDeleted(true);
    }

    private void validateSameMember(Member member, Member other) {
        if (!member.equals(other)) {
            throw new IllegalArgumentException("mismatched member");
        }
    }

    public Optional<Member> findById(Long id){
        return memberRepository.findById(id);
    }

    public SearchMemberResponse searchMember(Long memberId) {
        Member member = findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
        return SearchMemberResponse.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .email(member.getEmail())
                .build();
    }
}
