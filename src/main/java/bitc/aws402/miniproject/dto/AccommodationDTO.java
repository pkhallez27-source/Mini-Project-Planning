package bitc.aws402.miniproject.dto;

import lombok.Data;

//  추형호 -----
@Data
public class AccommodationDTO {
    private Long id;
    private String name;
    private String category;
    private String address;
    private int price;
}
