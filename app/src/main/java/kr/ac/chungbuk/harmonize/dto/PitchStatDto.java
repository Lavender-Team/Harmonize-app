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


    public double getCoverPercentage(String lowestPitch, String highestPitch) {
        if (c2 == null || d6 == null)
            return 0.0;

        lowestPitch = lowestPitch.toLowerCase();
        highestPitch = highestPitch.toLowerCase();

        double sum = 0.0;
        boolean withinRange = false;

        for (Field field : this.getClass().getDeclaredFields()) {
            String fieldName = field.getName();

            if (fieldName.equals(lowestPitch)) {
                withinRange = true;
            }

            if (withinRange) {
                try {
                    Double value = (Double) field.get(this);
                    if (value != null) {
                        sum += value;
                    }
                } catch (IllegalAccessException e) {
                    Log.d("PitchStatDto", e.getMessage());
                }
            }

            if (fieldName.equals(highestPitch)) {
                break;
            }
        }

        return sum;
    }
}
