package bitc.aws402.miniproject.dto;

import lombok.Data;

import java.time.LocalDateTime;

//  추형호 -----
@Data
public class QnaDTO {
    private int id;
    private String title;
    private String content;
    private int writer;
    private String memberId;
    private LocalDateTime createdAt;
    private int cnt;

    // 관리자 답변 관련 필드
    private String answer;
    private LocalDateTime answerDate;
    private String answerStatus;
    private LocalDateTime replyCreatedAt;
    private String replied;
}