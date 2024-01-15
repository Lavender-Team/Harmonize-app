package kr.ac.chungbuk.harmonize.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchHistory {

    private String keyword;

    public SearchHistory() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
