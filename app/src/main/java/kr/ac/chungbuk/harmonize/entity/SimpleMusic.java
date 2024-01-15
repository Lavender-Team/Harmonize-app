package kr.ac.chungbuk.harmonize.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SimpleMusic {
    private String title;
    private String artist;

    public SimpleMusic(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
