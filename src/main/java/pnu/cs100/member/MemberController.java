package pnu.cs100.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pnu.cs100.member.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberServiceImpl memberService;

    @PostMapping("/signUp")
    @Operation(summary = "멤버 회원가입", description = "멤버 회원가입 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        memberService.createMember(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 되었습니다.");
    }

    @PostMapping("/signIn")
    @Operation(summary = "멤버 로그인", description = "멤버 로그인 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignInResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest,
                                                 HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.signIn(signInRequest, session));
    }

    @PatchMapping("")
    @Operation(summary = "멤버 정보 수정", description = "멤버 정보 수정 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원정보 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "회원 정보 없음", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> updateMember(@RequestBody UpdateMemberRequest updateMemberRequest,
                                               HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        memberService.updateMember(updateMemberRequest, member);
        return ResponseEntity.status(HttpStatus.OK).body("회원정보 변경됨");
    }


    @GetMapping("/{memberId}")
    @Operation(summary = "멤버 조회", description = "멤버 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchMemberResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "회원 정보 없음", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<SearchMemberResponse> searchMember(@PathVariable(name = "memberId") Long memberId){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.searchMember(memberId));

    }

    @DeleteMapping("")
    @Operation(summary = "멤버 삭제", description = "멤버 삭제 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "회원 정보 없음", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> deleteMember(@RequestBody DeleteMemberRequest deleteMemberRequest,
                                               HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        memberService.deleteMember(deleteMemberRequest, member);
        return ResponseEntity.status(HttpStatus.OK).body("회원 삭제됨");
    }
}
