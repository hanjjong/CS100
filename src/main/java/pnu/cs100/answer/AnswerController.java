package pnu.cs100.answer;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.cs100.answer.dto.CreateAnswerRequest;
import pnu.cs100.answer.dto.GradeAnswerRequest;
import pnu.cs100.answer.dto.GradeAnswerResponse;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
@Tag(name = "Answer", description = "Answer API")
public class AnswerController {
    private final AnswerServiceImpl answerService;

    @PostMapping("")
    @Operation(summary = "답안 저장", description = "답안 저장 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "답안 저장 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<String> createAnswer(@RequestBody CreateAnswerRequest createAnswerRequest) {
        answerService.createAnswer(createAnswerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("답안 제출되었습니다.");
    }

    @PostMapping("/grade")
    @Operation(summary = "GPT 채점 및 답안 요청", description = "GPT API 채점 및 답안 요청 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "GPT 요청 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeAnswerResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<GradeAnswerResponse> gradeAnswer(@RequestBody GradeAnswerRequest gradeAnswerRequest) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(answerService.gradeAnswer(gradeAnswerRequest));
    }
}
