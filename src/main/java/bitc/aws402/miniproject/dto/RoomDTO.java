package bitc.aws402.miniproject.dto;

import lombok.Data;

//  구정민 -----
@Data
public class RoomDTO {

  private int roomIdx;
  private String roomName;
  private String roomSize;
  private int roomBasicPerson;
  private int roomMaxPerson;
  private int roomPriceWeekdays;
  private int roomPriceWeekend;
  private int roomPriceShoulderWeekdays;
  private int roomPriceShoulderWeekend;
  private int roomPricePeakWeekdays;
  private int roomPricePeakWeekend;
  private String roomService;


//  tb_resource 에서 조회한 대표 이미지 1개
  private String resourcePath;

  // 선택한 예약 기간의 총 금액
  private int totalPrice;
}
