package kr.ac.chungbuk.harmonize.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.databinding.FragmentHomeBinding;
import kr.ac.chungbuk.harmonize.dto.CommonMusicResultDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.ui.music.MusicActivity;
import kr.ac.chungbuk.harmonize.utility.adapter.ArtistListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.RecentMusicListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListShadowAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.RankAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    HomeViewModel homeViewModel;

    LinearLayoutManager homeRecommendLinearLayoutManager;
    LinearLayoutManager artistLayoutManager;
    LinearLayoutManager genreMusicLayoutManager;
    LinearLayoutManager recentMusicLayoutManager;

    MusicListShadowAdapter homeRecommendAdapter;
    ArtistListAdapter artistAdapter;
    MusicListAdapter genreMusicAdapter;
    RankAdapter rankMusicAdapter;
    RecentMusicListAdapter recentMusicAdapter;

    Handler sliderHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

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
        List<MusicListDto> rankMusics = new ArrayList<>();
        rankMusicAdapter = new RankAdapter(getContext(), rankMusics,
                new RankAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
        binding.rankViewPager.setAdapter(rankMusicAdapter);
        binding.rankViewPager.setOffscreenPageLimit(3);

        /* 최신 음악 */
        recentMusicLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recentMusicAdapter = new RecentMusicListAdapter(new ArrayList<>(),
                new RecentMusicListAdapter.OnItemSelectedInterface() {
                @Override
                public void onItemSelected(View v, long musicId) {
                    Intent intent = new Intent(getActivity(), MusicActivity.class);
                    intent.putExtra("musicId", musicId);
                    startActivity(intent);
                }
            });
        binding.latestMusicListView.setLayoutManager(recentMusicLayoutManager);
        binding.latestMusicListView.setAdapter(recentMusicAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        homeViewModel.fetchRankMusic(new HomeViewModel.OnRecentMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                rankMusicAdapter.setMusic(musics);
            }
        });

        homeViewModel.fetchRecentMusic(new HomeViewModel.OnRecentMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                recentMusicAdapter.setMusics(musics);
            }
        });
    }
}