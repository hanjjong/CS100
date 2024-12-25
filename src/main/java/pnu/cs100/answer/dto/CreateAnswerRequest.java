package pnu.cs100.answer.dto;

import java.util.List;

public record CreateAnswerRequest(Long memberId,
                                  Long examId,
                                  List<CreateAnswer> answers) {

    public static record CreateAnswer(Long problemId,
                                      String content) {
    }

}


