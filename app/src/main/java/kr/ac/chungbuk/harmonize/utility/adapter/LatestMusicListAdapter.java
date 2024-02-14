package kr.ac.chungbuk.harmonize.utility.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;

public class LatestMusicListAdapter extends RecyclerView.Adapter<LatestMusicListAdapter.Holder> {

    ArrayList<SimpleMusic> items = new ArrayList<>();

    public LatestMusicListAdapter(ArrayList<SimpleMusic> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_music_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SimpleMusic item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvArtist;
        private ImageView ivAlbum;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            ivAlbum = itemView.findViewById(R.id.ivAlbum);
        }
    }
}
