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
