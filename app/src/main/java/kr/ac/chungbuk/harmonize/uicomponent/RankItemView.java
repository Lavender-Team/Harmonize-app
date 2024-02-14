package kr.ac.chungbuk.harmonize.uicomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.ac.chungbuk.harmonize.R;

public class RankItemView extends LinearLayout {

    TextView tvRank, tvTitle, tvArtist;
    ImageView ivThumbnail;

    public RankItemView(Context context) {
        super(context);
        init(context, null);
    }

    public RankItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.rank_item, this, true);

        tvRank = findViewById(R.id.tvRank);
        tvTitle = findViewById(R.id.tvTitle);
        tvArtist = findViewById(R.id.tvArtist);
        ivThumbnail = findViewById(R.id.ivThumbnail);

        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RankItemView);
            tvRank.setText(attributes.getString(R.styleable.RankItemView_rank));
            tvTitle.setText(attributes.getString(R.styleable.RankItemView_title));
            tvArtist.setText(attributes.getString(R.styleable.RankItemView_artist));
        }
    }

}
