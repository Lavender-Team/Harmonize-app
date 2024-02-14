package kr.ac.chungbuk.harmonize.utility.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentRankBinding;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.Holder> {

    private static final String TAG = "RankAdapter";

    private Context mContext;
    private List<String> sliderItems;

    public RankAdapter(Context context, List<String> sliderImage) {
        mContext = context;
        this.sliderItems = sliderImage;
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
        //holder.bind(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }



    class Holder extends RecyclerView.ViewHolder {

        private FragmentRankBinding mBinding;

        public Holder(FragmentRankBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(String sliderItem) {
            try {
                //Glide.with(mContext).load(sliderItem).into(mBinding.imageSlider);
            } catch (Exception e) {
                Log.d(TAG, "ERROR: " + e.getMessage());
            }
        }
    }
}
