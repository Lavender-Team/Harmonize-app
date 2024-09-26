package kr.ac.chungbuk.harmonize.utility.adapter;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentRankBinding;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.Holder> {

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, long musicId);
    }

    private final RankAdapter.OnListItemSelectedInterface mListener;
    private List<MusicListDto> musics;

    public RankAdapter(Context context, List<MusicListDto> musics,
                       RankAdapter.OnListItemSelectedInterface mListener) {
        this.musics = musics;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(FragmentRankBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(musics, position);
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(musics.size() / 4f);
    }

    public void setMusic(List<MusicListDto> musics) {
        this.musics = musics;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder {

        private FragmentRankBinding binding;

        public Holder(FragmentRankBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(List<MusicListDto> musics, int position) {
            if (position * 4 < musics.size()) {
                binding.firstView.setRank(position * 4 + 1);
                binding.firstView.setTitle(musics.get(position * 4).getTitle());
                binding.firstView.setArtist(musics.get(position * 4).getArtist());
                binding.firstView.setThumbnail(musics.get(position * 4).getAlbumCover());
                binding.firstView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelected(v, musics.get(position * 4).getId());
                    }
                });
            }

            if (position * 4 + 1 < musics.size()) {
                binding.secondView.setRank(position * 4 + 2);
                binding.secondView.setTitle(musics.get(position * 4 + 1).getTitle());
                binding.secondView.setArtist(musics.get(position * 4 + 1).getArtist());
                binding.secondView.setThumbnail(musics.get(position * 4 + 1).getAlbumCover());
                binding.secondView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelected(v, musics.get(position * 4 + 1).getId());
                    }
                });
            }

            if (position * 4 + 2 < musics.size()) {
                binding.thirdView.setRank(position * 4 + 3);
                binding.thirdView.setTitle(musics.get(position * 4 + 2).getTitle());
                binding.thirdView.setArtist(musics.get(position * 4 + 2).getArtist());
                binding.thirdView.setThumbnail(musics.get(position * 4 + 2).getAlbumCover());
                binding.thirdView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelected(v, musics.get(position * 4 + 2).getId());
                    }
                });
            }

            if (position * 4 + 3 < musics.size()) {
                binding.fourthView.setRank(position * 4 + 4);
                binding.fourthView.setTitle(musics.get(position * 4 + 3).getTitle());
                binding.fourthView.setArtist(musics.get(position * 4 + 3).getArtist());
                binding.fourthView.setThumbnail(musics.get(position * 4 + 3).getAlbumCover());
                binding.fourthView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelected(v, musics.get(position * 4 + 3).getId());
                    }
                });
            }
        }
    }
}
