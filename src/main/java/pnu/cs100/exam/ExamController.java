package pnu.cs100.exam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pnu.cs100.exam.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam")
@Tag(name = "Exam", description = "문제집 API")
public class ExamController {
    private final ExamServiceImpl examService;

    @PostMapping("")
    @Operation(summary = "문제집 생성", description = "문제집 생성 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "문제집 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> createExam(@RequestBody CreateExamResquest createExamResquest){
        examService.createExam(createExamResquest);
        return ResponseEntity.status(HttpStatus.CREATED).body("문제집 생성되었습니다.");
    }

    @GetMapping("")
    @Operation(summary = "모든 문제집 조회", description = "모든 문제 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchExamResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<SearchExamResponse> searchExam(){
        return ResponseEntity.status(HttpStatus.OK).body(examService.searchExam());
    }

    @GetMapping("/{examId}")
    @Operation(summary = "특정 문제집 조회", description = "특정 문제 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchExamListRequest.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<SearchExamListRequest> searchOneExam(@PathVariable("examId") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(examService.searchOneExam(id));
    }

    @GetMapping("/member")
    @Operation(summary = "특정 멤버가 풀수 있는 문제 조회", description = "특정 멤버가 아직 풀지 않은 문제 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchExamResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<SearchExamResponse> searchCanSolveExam(@RequestParam("memberId") Long memberId){
        return ResponseEntity.status(HttpStatus.OK).body(examService.searchCanSolveExam(memberId));
    }

    @DeleteMapping("/{examId}")
    @Operation(summary = "특정 문제집 삭제", description = "특정 문제 삭제 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> deleteExam(@PathVariable("examId") Long id){
        examService.deleteExam(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }

//    @PostMapping("/problemList")
//    @Operation(summary = "문제집 생성 후 문제 추가", description = "문제집 생성 후 문제 추가")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
//                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
//            }
//    )
//    public ResponseEntity<String> addProblemListToExam(@RequestBody AddProblemListRequest addProblemListRequest){
//        examService.addProblemList(addProblemListRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body("문제 추가됨");
//    }
    @PostMapping("/problemList")
    @Operation(summary = "문제집 생성 후 문제 추가", description = "문제집 생성 후 문제 추가")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> addProblemListToExam(@RequestBody AddProblemListRequest addProblemListRequest){
        examService.addProblemList(addProblemListRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("문제 추가됨");
    }
}
