package kr.ac.chungbuk.harmonize.dto;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultDto {

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

