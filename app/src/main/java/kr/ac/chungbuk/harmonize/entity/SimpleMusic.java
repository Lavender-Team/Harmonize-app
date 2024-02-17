package kr.ac.chungbuk.harmonize.entity;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SimpleMusic {

    @Expose
    private String title;
    @Expose
    private String artist;
    @Expose
    private String coverPath;

    public SimpleMusic(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public SimpleMusic(String title, String artist, String coverPath) {
        this.title = title;
        this.artist = artist;
        this.coverPath = coverPath;
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

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
