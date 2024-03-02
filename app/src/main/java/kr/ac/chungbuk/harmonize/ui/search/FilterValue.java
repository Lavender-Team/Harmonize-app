package kr.ac.chungbuk.harmonize.ui.search;

import androidx.annotation.NonNull;

import java.util.StringTokenizer;

public class FilterValue {

    private Boolean genderMale = false;
    private Boolean genderFemale = false;
    private Boolean genderMixed = false;

    private String genre = "전체";

    // TODO 음역대 추가


    public boolean isGenderMale() {
        return genderMale;
    }

    public void setGenderMale(Boolean genderMale) {
        this.genderMale = genderMale;
    }

    public boolean isGenderFemale() {
        return genderFemale;
    }

    public void setGenderFemale(Boolean genderFemale) {
        this.genderFemale = genderFemale;
    }

    public boolean isGenderMixed() {
        return genderMixed;
    }

    public void setGenderMixed(Boolean genderMixed) {
        this.genderMixed = genderMixed;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @NonNull
    @Override
    public String toString() {
        return genderMale.toString() + "," +
                genderFemale.toString() + "," +
                genderMixed.toString() + "," +
                genre;
    }

    public void setFromString(String filterValue) {
        if (!filterValue.isEmpty()) {
            StringTokenizer st = new StringTokenizer(filterValue, ",");
            genderMale = Boolean.parseBoolean(st.nextToken());
            genderFemale = Boolean.parseBoolean(st.nextToken());
            genderMixed = Boolean.parseBoolean(st.nextToken());
            genre = st.nextToken();
        }
    }

    public Boolean isDefaultState() {
        if (genderMale || genderFemale || genderMale)
            return false;
        if (!genre.equals("전체"))
            return false;

        return true;
    }
}
