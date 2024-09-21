package kr.ac.chungbuk.harmonize.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDto {

    Long id;
    private String groupName;
    private String groupType;
    private String groupTypeName;
    private Integer groupSize;
    private String agency;
    private String profileImage;
    private List<ArtistDto> members;

}
