package kr.ac.chungbuk.harmonize.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {

    private String token;
    private LocalDateTime createdAt;

    private Long userId;
    private String nickname;
    private String gender;
    private Integer age;
    private List<String> genre;

}
