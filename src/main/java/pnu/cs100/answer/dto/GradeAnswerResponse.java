package pnu.cs100.answer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class GradeAnswerResponse {
    private List<Question> questions;

    @Data
    public static class Question {
        @JsonProperty("question_number")
        private int questionNumber;

        private int score;

        private String answer;

        private String feedback;
    }
}
