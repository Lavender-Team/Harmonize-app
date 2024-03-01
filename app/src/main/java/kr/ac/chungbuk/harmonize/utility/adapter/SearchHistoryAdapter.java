package kr.ac.chungbuk.harmonize.utility.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.SearchHistoryBinding;
import kr.ac.chungbuk.harmonize.service.SearchHistoryService;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private List<String> localDataSet;
    public final OnItemClickListener clickListener;

    public final OnItemRemoveListener removeListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SearchHistoryBinding binding;

        public ViewHolder(@NonNull SearchHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public SearchHistoryAdapter(List<String> dataSet, OnItemClickListener clickListener, OnItemRemoveListener removeListener) {
        localDataSet = dataSet;
        this.clickListener = clickListener;
        this.removeListener = removeListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SearchHistoryBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.search_history, parent, false);
        return new ViewHolder(binding);
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.binding.textHistory.setText(localDataSet.get(position));
        bindEvent(viewHolder, localDataSet.get(position));
    }

    private void bindEvent(ViewHolder viewHolder, final String item) {
        viewHolder.binding.layoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(item);
            }
        });

        viewHolder.binding.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localDataSet.remove(item);
                SearchHistoryService.delete(item);
                removeListener.onItemRemove(item);
            }
        });
    }

    public int getItemCount() {
        return localDataSet.size();
    }


}
