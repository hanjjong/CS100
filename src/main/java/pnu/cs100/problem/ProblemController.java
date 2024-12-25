package pnu.cs100.problem;

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
import pnu.cs100.problem.dto.CreateProblemRequest;
import pnu.cs100.problem.dto.SearchProblemResponse;
import pnu.cs100.problem.dto.UpdateProblemRequest;

import java.util.List;

@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
@Tag(name = "Problem", description = "문제 API")
public class ProblemController {

    private final ProblemServiceImpl problemService;

    @PostMapping("")
    @Operation(summary = "문제 생성", description = "문제 생성 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "문제 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> createProblem(@RequestBody CreateProblemRequest createProblemRequset){
        problemService.createProblem(createProblemRequset);
        return ResponseEntity.status(HttpStatus.CREATED).body("문제 생성되었습니다.");
    }

    @PatchMapping("")
    @Operation(summary = "문제 업데이트", description = "문제 수정 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "문제 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> updateProblem(@RequestBody UpdateProblemRequest updateProblemRequest){
        problemService.updateProblem(updateProblemRequest);
        return ResponseEntity.status(HttpStatus.OK).body("문제 업데이트");
    }

    @GetMapping("")
    @Operation(summary = "모든 문제 조회", description = "문제 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "문제 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchProblemResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<List<SearchProblemResponse>> searchAllProblem(){
        return ResponseEntity.status(HttpStatus.OK).body(problemService.searchAllProblem());
    }

    @GetMapping("/{id}")
    @Operation(summary = "문제 조회", description = "특정 문제 조회 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "문제 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchProblemResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<SearchProblemResponse> searchProblem(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(problemService.searchProblem(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "문제 삭제", description = "특정 문제 삭제 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "문제 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("문제 삭제되었습니다.");
    }
}
