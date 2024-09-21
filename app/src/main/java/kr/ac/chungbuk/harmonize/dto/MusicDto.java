package kr.ac.chungbuk.harmonize.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicDto {

    private Long id;
    private String title;
    private String artist;
    private GroupDto group;
    private String genre;
    private String genreName;
    private String karaokeNum;
    private LocalDateTime releaseDate;
    private String albumCover;
    private String playLink;
    private Long view;
    private Long likes;
    private List<String> themes;
    private String audioFile;
    private String lyrics;

    private String status;
    private Double highestPitch;
    private Double highPitchRatio;
    private Double highPitchCont;
    private Double lowestPitch;
    private Double lowPitchRatio;
    private Double lowPitchCont;
    private Integer steepSlope;
    private Integer level;

}
