package kr.ac.chungbuk.harmonize.ui.search;

import androidx.annotation.NonNull;

import java.util.StringTokenizer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterValue {

    private Boolean typeSolo = false;
    private Boolean typeGroup = false;

    private String genre = "전체";

    // TODO 음역대 추가


    @NonNull
    @Override
    public String toString() {
        return typeSolo.toString() + "," +
                typeGroup.toString() + "," +
                genre;
    }

    public void setFromString(String filterValue) {
        if (!filterValue.isEmpty()) {
            StringTokenizer st = new StringTokenizer(filterValue, ",");
            typeSolo = Boolean.parseBoolean(st.nextToken());
            typeGroup = Boolean.parseBoolean(st.nextToken());
            genre = st.nextToken();
        }
    }

    public String getGroupType() {
        if (typeSolo && !typeGroup)
            return "SOLO";
        else if (!typeSolo && typeGroup)
            return "GROUP";
        else
            return "";
    }

    public String getGenreValue() {
        switch (genre) {
            case "가요":
                return "KPOP";
            case "팝송":
                return "POP";
            case "발라드":
                return "BALLADE";
            case "랩/힙합":
                return "RAP";
            case "댄스":
                return "DANCE";
            case "일본곡":
                return "JPOP";
            case "R&B":
                return "RNB";
            case "포크/블루스":
                return "FOLK";
            case "록/메탈":
                return "ROCK";
            case "OST":
                return "OST";
            case "인디뮤직":
                return "INDIE";
            case "트로트":
                return "TROT";
            case "어린이곡":
                return "KID";
            default:
                return "";
        }

    }

    public Boolean isDefaultState() {
        if (typeSolo || typeGroup)
            return false;
        if (!genre.equals("전체"))
            return false;

        return true;
    }

    public void setDefault() {
        typeSolo = typeGroup = false;
        genre = "전체";
    }
}
