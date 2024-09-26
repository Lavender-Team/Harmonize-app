package kr.ac.chungbuk.harmonize.utility.adapter;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import lombok.Getter;

public class RecentMusicListAdapter extends RecyclerView.Adapter<RecentMusicListAdapter.Holder> {

    public interface OnItemSelectedInterface {
        void onItemSelected(View v, long musicId);
    }

    private final RecentMusicListAdapter.OnItemSelectedInterface mListener;

    List<MusicListDto> musics = new ArrayList<>();

    public RecentMusicListAdapter(ArrayList<MusicListDto> musics,
                                  RecentMusicListAdapter.OnItemSelectedInterface mListener) {
        this.musics = musics;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_music_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MusicListDto item = musics.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
        Glide.with(getAppContext())
                .load(Domain.url(item.getAlbumCover()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(new ColorDrawable(Color.parseColor("#F6F6F6")))
                .into(holder.ivThumbnail);

        holder.getMView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v, item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    public void setMusics(List<MusicListDto> musics) {
        this.musics = musics;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @Getter
        private View mView;
        private TextView tvTitle, tvArtist;
        private ImageView ivThumbnail;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
