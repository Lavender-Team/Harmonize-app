package kr.ac.chungbuk.harmonize.uicomponent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        musicListView.setLayoutManager(linearLayoutManager);
        adapter = new MusicListAdapter(items);
        musicListView.setAdapter(adapter);

        return view;
    }

    public void search() {
        musicListView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        networkManager.getSearch(new RequestListener<ArrayList<SimpleMusic>>() {
            @Override
            public void getResult(ArrayList<SimpleMusic> musics) {
                if (musics == null) {
                    // TODO 오류 처리
                    return;
                }

                items.clear();
                for (SimpleMusic m: musics)
                    items.add(m);

                adapter.notifyDataSetChanged();
                musicListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
