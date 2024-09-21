package kr.ac.chungbuk.harmonize.uicomponent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.dto.SearchResultDto;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import kr.ac.chungbuk.harmonize.ui.music.MusicActivity;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;

public class SearchResultFragment extends Fragment {

    RecyclerView musicListView;
    ProgressBar progressBar;
    LinearLayout noResultText;
    TextView keywordText;
    TextView errorText;
    LinearLayoutManager linearLayoutManager;
    MusicListAdapter adapter;
    ArrayList<SimpleMusic> items = new ArrayList<>();


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
        adapter = new MusicListAdapter(items,
                getActivity(),
                new MusicListAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, int position) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", items.get(position).getMusicId());
                        startActivity(intent);
                    }
                });
        musicListView.setAdapter(adapter);

        return view;
    }

    public void searchRequested(String query) {
        setVisibilityState(LoadState.LOADING);
        keywordText.setText("'" + query + "'");
    }

    public void searchFailed() {
        setVisibilityState(LoadState.ERROR);
    }

    public void searchCompleted(SearchResultDto searchResult) {
        items.clear();
        for (MusicListDto m : searchResult.getContent()) {
            items.add(new SimpleMusic(m.getId(), m.getTitle(), m.getArtist(), m.getAlbumCover()));
        }

        adapter.notifyDataSetChanged();

        if (items.size() > 0)
            setVisibilityState(LoadState.IDLE);
        else
            setVisibilityState(LoadState.NO_RESULT);
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
