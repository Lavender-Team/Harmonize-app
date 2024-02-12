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

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.Holder> {

    ArrayList<SimpleMusic> items = new ArrayList<>();

    public ArtistListAdapter(ArrayList<SimpleMusic> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SimpleMusic item = items.get(position);
        holder.tvArtistName.setText(item.getArtist());
        holder.tvCount.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvArtistName, tvCount;
        private ImageView ivAlbum;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivAlbum = itemView.findViewById(R.id.ivAlbum);
        }
    }
}
