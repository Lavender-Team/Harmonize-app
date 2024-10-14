package kr.ac.chungbuk.harmonize.utility;

import android.util.Log;

import java.util.Arrays;

public class PitchConverter {

    private static final double[] freqLevel = new double[] {
            65.41, 65.41, 73.42, 82.41, 87.31, 98.00, 110.00, 123.47, 130.81, 146.83, 164.81, 174.61, 196.00, 220.00, 246.94,
            261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25, 587.33, 659.25, 698.46, 783.99, 880.00, 987.77,
            1046.50, 1174.66, 1174.66
    };

    private static final String[] pitchString = new String[] {
        "C2", "C2", "D2", "E2", "F2", "G2", "A2", "B2", "C3", "D3", "E3", "F3", "G3", "A3", "B3",
        "C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5", "D5", "E5", "F5", "G5", "A5", "B5", "C6", "D6", "D6"
    };


    /**
     * @param value MusicDetail에서 받은 음높이 max, min 값
     * @return C3 D3 등 음계 문자열
     */
    public static String freqToPitch(double value)
    {
        int nearestIndex = searchNearest(value);

        if (0 <= nearestIndex && nearestIndex < 32)
            return pitchString[nearestIndex];
        else
            return pitchString[0];
    }

    private static int searchNearest(double value) {
        int result = Arrays.binarySearch(freqLevel, value);
        if (result >= 0) { return result; }

        int insertionPoint = -result - 1;
        return (freqLevel[insertionPoint] - value) < (value - freqLevel[insertionPoint - 1]) ?
                insertionPoint : insertionPoint - 1;
    }

}
