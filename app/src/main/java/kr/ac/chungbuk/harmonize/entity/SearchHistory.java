package kr.ac.chungbuk.harmonize.entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 *  검색 기록
 */
@Getter
@Setter
public class SearchHistory {

    private String keyword;
    private LocalDateTime createdAt;

    public SearchHistory() {
    }

    public SearchHistory(String keyword, LocalDateTime createdAt) {
        this.keyword = keyword;
        this.createdAt = createdAt;
    }

}
