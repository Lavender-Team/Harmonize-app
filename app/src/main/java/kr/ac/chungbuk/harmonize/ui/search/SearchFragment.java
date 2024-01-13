package kr.ac.chungbuk.harmonize.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.chungbuk.harmonize.databinding.FragmentSearchBinding;
import kr.ac.chungbuk.harmonize.utility.adapter.OnItemClickListener;
import kr.ac.chungbuk.harmonize.utility.adapter.OnItemRemoveListener;
import kr.ac.chungbuk.harmonize.utility.adapter.SearchHistoryAdapter;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private SearchHistoryAdapter adapter;
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setFragment(this);

        final RecyclerView historyListView = binding.historyListView;
        adapter = new SearchHistoryAdapter(searchViewModel.getHistorys(), new OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                searchViewModel.setKeyword(item);
            }
        }, new OnItemRemoveListener() {
            @Override
            public void onItemRemove(String item) {
                adapter.notifyDataSetChanged();
            }
        });
        historyListView.setAdapter(adapter);

        searchViewModel.getKeyword().observe(getViewLifecycleOwner(), binding.etSearch::setText);

        //final TextView textView = binding.textNotifications;
        //searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showFilter() {
        searchViewModel.addHistory("검색어");
        //Toast.makeText(getContext(), "showFilter", Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }
}