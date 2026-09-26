package bitc.aws402.miniproject.dto;

import lombok.Data;

@Data
public class MemberDTO {
  private int memberIdx;
  private String memberId;
  private String memberPwd;
  private String memberName;
  private String memberPhone;
  private String memberEmail;
  private String memberGender;
  private String memberJoinDate;
  private int memberLevel;
  private int memberStatus;

  // Thymeleaf 화면에서 ${session.loginUser.userName} 호출 시 에러 방지용 메서드 추가
  public String getUserName() {
    return memberName;
  }

  //  추형호 -----
  public String getRole(){
    return memberLevel == 99 ? "ADMIN":"USER";
  }
}