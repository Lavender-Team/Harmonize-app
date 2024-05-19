package kr.ac.chungbuk.harmonize.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;

public class ExcelReader {
    private Context context;

    public ExcelReader(Context context) {
        this.context = context;
    }

    public void readExcelFile(int resId, ArrayList<Double> timeList, ArrayList<Double> pitchPointList) {


        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(resId);

            Workbook workbook = new HSSFWorkbook(in_s);
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;

            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                Cell timeCell = row.getCell(1); // assuming 'time' is in the second column
                Cell pitchPointCell = row.getCell(2); // assuming 'pitch_point' is in the third column

                Log.d("PitchGraphTest", " " + sheet);

                if (timeCell != null && pitchPointCell != null) {
                    timeList.add(timeCell.getNumericCellValue());
                    pitchPointList.add(pitchPointCell.getNumericCellValue());
                }
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
