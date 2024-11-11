package kr.ac.chungbuk.harmonize.dto;

import android.util.Log;

import java.lang.reflect.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class PitchStatDto {

    Double c2, d2, e2, f2, g2, a2, b2;
    Double c3, d3, e3, f3, g3, a3, b3;
    Double c4, d4, e4, f4, g4, a4, b4;
    Double c5, d5, e5, f5, g5, a5, b5, c6, d6;

    public double getPercentage(int index) {
        if (c2 == null || d6 == null)
            return 0.0;

        if (index == 0) return c2;
        else if (index == 1) return d2;
        else if (index == 2) return e2;
        else if (index == 3) return f2;
        else if (index == 4) return g2;
        else if (index == 5) return a2;
        else if (index == 6) return b2;
        else if (index == 7) return c3;
        else if (index == 8) return d3;
        else if (index == 9) return e3;
        else if (index == 10) return f3;
        else if (index == 11) return g3;
        else if (index == 12) return a3;
        else if (index == 13) return b3;
        else if (index == 14) return c4;
        else if (index == 15) return d4;
        else if (index == 16) return e4;
        else if (index == 17) return f4;
        else if (index == 18) return g4;
        else if (index == 19) return a4;
        else if (index == 20) return b4;
        else if (index == 21) return c5;
        else if (index == 22) return d5;
        else if (index == 23) return e5;
        else if (index == 24) return f5;
        else if (index == 25) return g5;
        else if (index == 26) return a5;
        else if (index == 27) return b5;
        else if (index == 28) return c6;
        else if (index == 29) return d6;

        return 0.0;
    }

    public String getLabel(int index) {
        if (c2 == null || d6 == null)
            return "";

        if (index == 0) return "C2";
        else if (index == 1) return "D2";
        else if (index == 2) return "E2";
        else if (index == 3) return "F2";
        else if (index == 4) return "G2";
        else if (index == 5) return "A2";
        else if (index == 6) return "B2";
        else if (index == 7) return "C3";
        else if (index == 8) return "D3";
        else if (index == 9) return "E3";
        else if (index == 10) return "F3";
        else if (index == 11) return "G3";
        else if (index == 12) return "A3";
        else if (index == 13) return "B3";
        else if (index == 14) return "C4";
        else if (index == 15) return "D4";
        else if (index == 16) return "E4";
        else if (index == 17) return "F4";
        else if (index == 18) return "G4";
        else if (index == 19) return "A4";
        else if (index == 20) return "B4";
        else if (index == 21) return "C5";
        else if (index == 22) return "D5";
        else if (index == 23) return "E5";
        else if (index == 24) return "F5";
        else if (index == 25) return "G5";
        else if (index == 26) return "A5";
        else if (index == 27) return "B5";
        else if (index == 28) return "C6";
        else if (index == 29) return "D6";

        return "";
    }

    public double getCoverPercentage(String lowestPitch, String highestPitch) {
        if (c2 == null || d6 == null)
            return 0.0;

        lowestPitch = lowestPitch;
        highestPitch = highestPitch;

        double sum = 0.0;
        boolean withinRange = false;

        for (int i = 0; i < 30; i++) {
            String fieldName = getLabel(i);

            if (fieldName.equals(lowestPitch)) {
                withinRange = true;
            }

            if (withinRange) {
                Double value = (Double) getPercentage(i);
                if (value != null) {
                    sum += value;
                }
            }

            if (fieldName.equals(highestPitch)) {
                break;
            }
        }

        return sum;
    }
}
