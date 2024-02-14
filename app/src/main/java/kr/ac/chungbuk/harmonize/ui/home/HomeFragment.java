package kr.ac.chungbuk.harmonize.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentHomeBinding;
import kr.ac.chungbuk.harmonize.utility.adapter.ArtistListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListShadowAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.RankAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    LinearLayoutManager homeRecommendLinearLayoutManager;
    LinearLayoutManager artistLayoutManager;
    LinearLayoutManager genreMusicLayoutManager;

    MusicListShadowAdapter homeRecommendAdapter;
    ArtistListAdapter artistAdapter;
    MusicListAdapter genreMusicAdapter;

    Handler sliderHandler = new Handler();

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

        /* 장르별 맞춤 추천곡 */
        genreMusicLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        genreMusicAdapter = new MusicListAdapter(homeViewModel.getGenreMusics());
        binding.genreMusicListView.setLayoutManager(genreMusicLayoutManager);
        binding.genreMusicListView.setAdapter(genreMusicAdapter);

        /* 인기곡 순위 */
        List<String> sliderItems = new ArrayList<>();
        sliderItems.add("https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg");
        sliderItems.add("https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg");
        sliderItems.add("https://cdn.pixabay.com/photo/2020/11/10/01/34/pet-5728249_1280.jpg");
        binding.rankViewPager.setAdapter(new RankAdapter(getContext(), sliderItems));
        binding.rankViewPager.setOffscreenPageLimit(3);



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