package kr.ac.chungbuk.harmonize.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicListDto {

    private Long id;
    private String title;
    private String artist;
    private String genre;
    private String genreName;
    private String status;
    private String albumCover;
    private Long view;
    private Long likes;
    private List<String> themes;

}
