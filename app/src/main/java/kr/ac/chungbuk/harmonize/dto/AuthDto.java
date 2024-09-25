package kr.ac.chungbuk.harmonize.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<String> getGenreValue() {
        List<String> values = new ArrayList<>();
        for (String g : genre)
            values.add(names.get(g));

        return values;
    }

    // 한글 장르명(가요, 발라드 등)을 받아 genre List<String>을 설정하는 setter
    public void setGenreValue(List<String> genres) {
        this.genre.clear();
        for (String g : genres) {
            this.genre.add(values.get(g));
        }
    }

    private static final Map<String, String> names = new HashMap<>();
    private static final Map<String, String> values = new HashMap<>();

    static {
        names.put("KPOP", "가요");
        names.put("POP", "팝송");
        names.put("BALLADE", "발라드");
        names.put("RAP", "랩/힙합");
        names.put("DANCE", "댄스");
        names.put("JPOP", "일본곡");
        names.put("RNB", "R&B");
        names.put("FOLK", "포크/블루스");
        names.put("ROCK", "록/메탈");
        names.put("OST", "OST");
        names.put("INDIE", "인디뮤직");
        names.put("TROT", "트로트");
        names.put("KID", "어린이곡");

        for (Map.Entry<String, String> val : names.entrySet()) {
            values.put(val.getValue(), val.getKey());
        }
    }

}
