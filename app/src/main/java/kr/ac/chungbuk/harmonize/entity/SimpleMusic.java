package kr.ac.chungbuk.harmonize.entity;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
