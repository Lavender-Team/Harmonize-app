package kr.ac.chungbuk.harmonize.uicomponent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentSearchResultBinding;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;
import kr.ac.chungbuk.harmonize.utility.network.NetworkManager;
import kr.ac.chungbuk.harmonize.utility.network.event.RequestListener;

public class SearchResultFragment extends Fragment {

    NetworkManager networkManager;

    RecyclerView musicListView;
    ProgressBar progressBar;
    LinearLayout noResultText;
    TextView keywordText;
    TextView errorText;
    LinearLayoutManager linearLayoutManager;
    MusicListAdapter adapter;
    ArrayList<SimpleMusic> items = new ArrayList<>();


    public SearchResultFragment() {
        networkManager = NetworkManager.getInstance(getContext());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        musicListView = view.findViewById(R.id.musicListView);
        progressBar = view.findViewById(R.id.progressBar);
        noResultText = view.findViewById(R.id.noResultText);
        keywordText = view.findViewById(R.id.keywordText);
        errorText = view.findViewById(R.id.errorText);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        musicListView.setLayoutManager(linearLayoutManager);
        adapter = new MusicListAdapter(items);
        musicListView.setAdapter(adapter);

        return view;
    }

    public void search(String keyword) {
        setVisibilityState(LoadState.LOADING);
        keywordText.setText("'" + keyword + "'");

        networkManager.getSearch(new RequestListener<ArrayList<SimpleMusic>>() {
            @Override
            public void getResult(ArrayList<SimpleMusic> musics) {
                if (musics == null) {
                    // 네트워크 문제
                    setVisibilityState(LoadState.ERROR);
                    return;
                }

                items.clear();
                for (SimpleMusic m: musics)
                    items.add(m);

                adapter.notifyDataSetChanged();

                if (items.size() > 0)
                    setVisibilityState(LoadState.IDLE);
                else
                    setVisibilityState(LoadState.NO_RESULT);
            }
        });
    }

    private void setVisibilityState(LoadState state) {
        musicListView.setVisibility(state == LoadState.IDLE ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(state == LoadState.LOADING ? View.VISIBLE : View.GONE);
        noResultText.setVisibility(state == LoadState.NO_RESULT ? View.VISIBLE : View.GONE);
        errorText.setVisibility(state == LoadState.ERROR ? View.VISIBLE : View.GONE);
    }

    private enum LoadState {
        IDLE, LOADING, NO_RESULT, ERROR
    }

}
