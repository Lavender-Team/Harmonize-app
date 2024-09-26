package kr.ac.chungbuk.harmonize.uicomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;

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

    public void setRank(Integer rank) {
        tvRank.setText(rank.toString());
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setArtist(String artist) {
        tvArtist.setText(artist);
    }

    public void setThumbnail(String albumCover) {
        // 앨범 커버 로드
        Glide.with(getContext())
                .load(Domain.url(albumCover))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(new ColorDrawable(Color.parseColor("#F6F6F6")))
                .into(ivThumbnail);
    }
}
