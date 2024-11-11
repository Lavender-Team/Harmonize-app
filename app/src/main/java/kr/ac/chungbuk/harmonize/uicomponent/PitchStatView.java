package kr.ac.chungbuk.harmonize.uicomponent;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.dto.PitchStatDto;
import kr.ac.chungbuk.harmonize.utility.ExcelReader;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;

public class PitchStatView extends LinearLayout implements SeekBar.OnSeekBarChangeListener {

    private BarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;


    public PitchStatView(Context context) {
        super(context);
        init(context);
    }

    public PitchStatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pitch_stat, this, true);

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);
        seekBarX = findViewById(R.id.seekBar1);
        seekBarY = findViewById(R.id.seekBar2);

        chart = findViewById(R.id.chart);

        // SeekBar 변경 리스너
        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvX.setText("X: " + progress);
                //updateChart(progress, seekBarY.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvY.setText("Y: " + progress);
                //updateChart(seekBarX.getProgress(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Bar 클릭 이벤트 리스너 설정
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                BarDataSet dataSet = (BarDataSet) chart.getData().getDataSetByIndex(0);
                dataSet.setDrawValues(true); // 클릭된 Bar의 값 표시
                chart.invalidate(); // 업데이트
            }

            @Override
            public void onNothingSelected() {
                BarDataSet dataSet = (BarDataSet) chart.getData().getDataSetByIndex(0);
                dataSet.setDrawValues(false); // 클릭 해제 시 값 숨김
                chart.invalidate(); // 업데이트
            }
        });
    }

    public void updateChart(PitchStatDto pitchStatDto, String lowestPitch, String highestPitch) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            entries.add(new BarEntry(i, (float)pitchStatDto.getPercentage(i)));
            xLabels.add(pitchStatDto.getLabel(i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setDrawValues(false);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // 값이 0.0이면 레이블을 빈 문자열로 설정
                if (value == 0.0) {
                    return "";  // 빈 문자열을 반환하여 레이블을 표시하지 않음
                }
                return String.format("%.0f%%", value * 100);  // 값 표시
            }
        });

        // Bar 색 설정
        ArrayList<Integer> colors = new ArrayList<>();
        boolean withinRange = false;

        for (int i = 0; i < 30; i++) {
            if (pitchStatDto.getLabel(i).equals(lowestPitch)) {
                withinRange = true;
            }

            if (withinRange) {
                colors.add(Color.rgb(0x77, 0x0E, 0xF8));
            }
            else {
                colors.add(Color.rgb(0xBB, 0x87, 0xFC));
            }

            if (pitchStatDto.getLabel(i).equals(highestPitch)) {
                withinRange = false;
            }
        }
        dataSet.setColors(colors); // 색상을 설정

        BarData data = new BarData(dataSet);
        chart.setData(data);
        
        // Y축 범위 설정
        chart.getAxisLeft().setAxisMinimum(0.0f);
        //chart.getAxisLeft().setAxisMaximum(1.1f);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f%%", value * 100); // 값에 100 곱하고 % 기호 추가
            }
        });

        // X축 레이블 설정
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);

        // Chart 설명 비활성화 (원한다면 활성화 가능)
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        chart.getLegend().setEnabled(false);

        chart.setExtraOffsets(10, 10, 10, 20); // 여백 추가
        chart.invalidate(); // Chart 업데이트
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
