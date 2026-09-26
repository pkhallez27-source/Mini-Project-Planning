package bitc.aws402.miniproject.dto;

import lombok.Data;

//  김진형 -----
@Data
public class RestayDTO {

    private int roomIdx;
    private String roomName;
    private String roomService;
    private String roomSize;
    private int roomBasicPerson;
    private int roomMaxPerson;
    private int roomPriceWeekdays;
    private int roomPriceWeekend;
    private int roomPriceShoulderWeekdays;
    private int roomPriceShoulderWeekend;
    private int roomPricePeakWeekdays;
    private int roomPricePeakWeekend;
    private String resourcePath;
}
