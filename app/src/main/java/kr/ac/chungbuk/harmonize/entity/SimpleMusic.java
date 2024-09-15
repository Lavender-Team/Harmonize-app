package kr.ac.chungbuk.harmonize.entity;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleMusic {

    @Expose
    private Long musicId;
    @Expose
    private String title;
    @Expose
    private String artist;
    @Expose
    private String albumCover;

    public SimpleMusic(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public SimpleMusic(String title, String artist, String albumCover) {
        this.title = title;
        this.artist = artist;
        this.albumCover = albumCover;
    }

    public SimpleMusic(Long musicId, String title, String artist, String albumCover) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.albumCover = albumCover;
    }

}
