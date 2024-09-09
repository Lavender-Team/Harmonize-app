package kr.ac.chungbuk.harmonize.entity;

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
    private OffsetDateTime createdAt;

    public SearchHistory() {
    }

    public SearchHistory(String keyword, OffsetDateTime createdAt) {
        this.keyword = keyword;
        this.createdAt = createdAt;
    }

}
