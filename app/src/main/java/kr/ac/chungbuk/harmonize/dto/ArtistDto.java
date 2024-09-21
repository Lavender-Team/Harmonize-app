package kr.ac.chungbuk.harmonize.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistDto {

    private Long id;
    private String artistName;
    private String gender;
    private String genderName;
    private String activityPeriod;
    private String nation;
    private String agency;
    private String profileImage;

}
