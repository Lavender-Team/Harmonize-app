package kr.ac.chungbuk.harmonize.uicomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.ac.chungbuk.harmonize.R;

public class SearchHistoryView extends LinearLayout {

    TextView history;

    public SearchHistoryView(Context context, TextView history) {
        super(context);
        this.history = history;
    }

    public SearchHistoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_history, this, true);

        history = (TextView) findViewById(R.id.text_history);
    }

    public void setHistory(String h) {
        history.setText(h);
    }
}
