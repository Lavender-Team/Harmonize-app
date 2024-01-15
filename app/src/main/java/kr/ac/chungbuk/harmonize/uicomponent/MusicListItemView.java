package kr.ac.chungbuk.harmonize.uicomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.ac.chungbuk.harmonize.R;

public class MusicListItemView extends LinearLayout {

    TextView tvTitle, tvArtist;
    ImageView ivThumbnail;

    public MusicListItemView(Context context) {
        super(context);
        init(context);
    }

    public MusicListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_list_item, this, true);

        tvTitle = findViewById(R.id.tvTitle);
        tvArtist = findViewById(R.id.tvArtist);
        ivThumbnail = findViewById(R.id.ivThumbnail);
    }

}
