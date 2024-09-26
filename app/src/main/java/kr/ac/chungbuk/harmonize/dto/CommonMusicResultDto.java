package kr.ac.chungbuk.harmonize.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonMusicResultDto {

    @Expose
    List<MusicListDto> content;
    @Expose
    Pageable pageable;
    @Expose
    Integer totalPages;

    @Getter
    @Setter
    public static class Pageable {
        @Expose
        Integer pageNumber;
        @Expose
        Integer pageSize;
    }
}

