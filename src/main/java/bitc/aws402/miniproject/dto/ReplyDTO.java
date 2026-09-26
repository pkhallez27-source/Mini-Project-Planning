package bitc.aws402.miniproject.dto;

import lombok.Data;

@Data
public class ReplyDTO {
  private int replyIdx;
  private String replyContents;
  private int replyBoardidx;
  private int replyCreateIdx;
  private String replyCreateDate;
  private String memberName;
}
