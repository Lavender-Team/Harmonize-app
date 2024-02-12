package kr.ac.chungbuk.harmonize.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentHomeBinding;
import kr.ac.chungbuk.harmonize.utility.adapter.ArtistListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListShadowAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    LinearLayoutManager homeRecommendLinearLayoutManager;
    LinearLayoutManager artistLayoutManager;

    MusicListShadowAdapter homeRecommendAdapter;
    ArtistListAdapter artistAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /* 최상단 추천곡 목록 */
        homeRecommendLinearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        homeRecommendAdapter = new MusicListShadowAdapter(homeViewModel.getHomeRecommendMusics());
        binding.homeRecommendListView.setLayoutManager(homeRecommendLinearLayoutManager);
        binding.homeRecommendListView.setAdapter(homeRecommendAdapter);

        
        /* 비슷한 가수 목록 */
        artistLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        artistAdapter = new ArtistListAdapter(homeViewModel.getArtists());
        binding.artistListView.setLayoutManager(artistLayoutManager);
        binding.artistListView.setAdapter(artistAdapter);



        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}