package kr.ac.chungbuk.harmonize.utility.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.ui.music.MusicActivity;

public class MusicListFeedbackAdapter extends RecyclerView.Adapter<MusicListFeedbackAdapter.Holder> {

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, long musicId);
    }

    private OnListItemSelectedInterface mListener;

    List<MusicListDto> items = new ArrayList<>();
    FragmentActivity activity;

    public MusicListFeedbackAdapter(ArrayList<MusicListDto> items) {
        this.items = items;
    }

    public MusicListFeedbackAdapter(ArrayList<MusicListDto> items, FragmentActivity activity) {
        this.items = items;
        this.activity = activity;
    }

    public MusicListFeedbackAdapter(List<MusicListDto> items, FragmentActivity activity,
                                    OnListItemSelectedInterface mListener) {
        this.items = items;
        this.activity = activity;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item_feedback, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MusicListDto item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
        if (activity != null) {
            Glide.with(activity)
                    .load(Domain.url(item.getAlbumCover()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(new ColorDrawable(Color.parseColor("#F6F6F6")))
                    .into(holder.ivThumbnail);
        }

        if (position % 3 == 1) {
            holder.llFeedback.setVisibility(View.VISIBLE);
        }
        else {
            holder.llFeedback.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<MusicListDto> newItems) {
        int startPosition = items.size();
        items.addAll(newItems);
        notifyItemRangeChanged(startPosition, newItems.size());
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvArtist;
        private final ImageView ivThumbnail;
        private final LinearLayout musicListItem;
        private final LinearLayout llFeedback;
        private final LinearLayout btnLike, btnDislike;
        private final ImageButton btnFeedbackMenu;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            musicListItem = itemView.findViewById(R.id.musicListItem);
            btnFeedbackMenu = itemView.findViewById(R.id.btnFeedbackMenu);
            llFeedback = itemView.findViewById(R.id.llFeedback);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnDislike = itemView.findViewById(R.id.btnDislike);

            btnFeedbackMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llFeedback.getVisibility() == View.VISIBLE) {
                        llFeedback.animate().alpha(0.0f).translationY(-20);
                        llFeedback.setVisibility(View.GONE);
                    }
                    else {
                        llFeedback.animate().alpha(1.0f).translationY(0);
                        llFeedback.setVisibility(View.VISIBLE);
                    }
                }
            });

            musicListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, items.get(getAdapterPosition()).getId());
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postFeedback(items.get(getAdapterPosition()).getId(), true);
                    llFeedback.setVisibility(View.GONE);
                    btnFeedbackMenu.setImageResource(R.drawable.ic_like_black_12dp);
                    btnFeedbackMenu.setOnClickListener(null);
                }
            });

            btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postFeedback(items.get(getAdapterPosition()).getId(), false);
                    llFeedback.setVisibility(View.GONE);
                    btnFeedbackMenu.setImageResource(R.drawable.ic_dislike_black_12dp);
                    btnFeedbackMenu.setOnClickListener(null);
                }
            });

        }
    }

    void postFeedback(long musicId, boolean isPositive) {

        if (activity == null)
            return;

        StringRequest feedbackRequest = new StringRequest(
                Request.Method.POST,
                Domain.url("/api/music/" + musicId + "/feedback?isPositive=" + isPositive),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(activity,
                                "피드백 처리 중 오류가 발생하였습니다.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", AuthDao.getToken());
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(feedbackRequest);
    }

}
