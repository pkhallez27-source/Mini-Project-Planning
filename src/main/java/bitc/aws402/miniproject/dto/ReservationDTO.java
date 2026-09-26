package bitc.aws402.miniproject.dto;

import lombok.Data;

//  구정민 -----
@Data
public class ReservationDTO {

  private int rvIdx;
  private String rvCode;
  private int rvRoomIdx;
  private String rvStartDate;
  private String rvEndDate;
  private int rvPersonAdults;
  private int rvPersonKids;
  private int rvMemberIdx;
  private String rvDate;
  private int rvStatus;
  private int rvPay;

//  화면 출력용 JOIN 데이터
  private String roomName;
  private String roomSize;
  private String resourcePath;
  private String memberName;
  private String memberPhone;
  private String memberEmail;
}
