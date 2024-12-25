package pnu.cs100.examProblem;

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
import pnu.cs100.examProblem.dto.CreateExamProblemRequest;
import pnu.cs100.examProblem.dto.DeleteExamProblem;

@RestController
@RequestMapping("/examProblem")
@RequiredArgsConstructor
@Tag(name = "ExamProblem", description = "문제집에 등록된 문제 API")
public class ExamProblemController {

    private final ExamProblemServiceImpl examProblemService;

    @PostMapping("")
    @Operation(summary = "특정 문제 문제집에 등록", description = "특정 문제를 특정 문제집에 등록하는 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> createExamProblem(@RequestBody CreateExamProblemRequest createExamProblemRequest){
        examProblemService.createExamProblem(createExamProblemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("문제집 문제가 생성되었습니다.");
    }

    @DeleteMapping("")
    @Operation(summary = "특정 문제집의 문제 삭제", description = "특정 문제집의 문제 삭제 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> deleteExamProblem(@RequestBody DeleteExamProblem deleteExamProblem){
        examProblemService.deleteExamProblem(deleteExamProblem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("문제가 삭제되었습니다.");
    }
}
