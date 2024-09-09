package kr.ac.chungbuk.harmonize.utility.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.Holder> {

    ArrayList<SimpleMusic> items = new ArrayList<>();
    FragmentActivity activity;

    public MusicListAdapter(ArrayList<SimpleMusic> items) {
        this.items = items;
    }

    public MusicListAdapter(ArrayList<SimpleMusic> items, FragmentActivity activity) {
        this.items = items;
        this.activity = activity;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SimpleMusic item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
        if (activity != null) {
            Glide.with(activity)
                    .load(Domain.url(item.getCoverPath()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(new ColorDrawable(Color.parseColor("#F6F6F6")))
                    .into(holder.ivThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvArtist;
        private final ImageView ivThumbnail;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
