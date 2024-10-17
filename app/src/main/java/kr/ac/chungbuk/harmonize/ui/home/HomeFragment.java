package kr.ac.chungbuk.harmonize.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;
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
import java.util.Arrays;
import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.FragmentHomeBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
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
        homeRecommendAdapter = new MusicListShadowAdapter(homeViewModel.getHomeRecommendMusics(),
                getActivity(),
                new MusicListAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
        binding.homeRecommendListView.setLayoutManager(homeRecommendLinearLayoutManager);
        binding.homeRecommendListView.setAdapter(homeRecommendAdapter);

        
        /* 비슷한 가수 목록 */
        artistLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        artistAdapter = new ArtistListAdapter(homeViewModel.getArtists());
        binding.artistListView.setLayoutManager(artistLayoutManager);
        binding.artistListView.setAdapter(artistAdapter);

        /* 장르별 맞춤 추천곡 */
        genreMusicLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        genreMusicAdapter = new MusicListAdapter(homeViewModel.getGenreMusics(),
                getActivity(),
                new MusicListAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
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

        
        /* 선호 장르 선택 버튼 동적으로 추가 */
        LayoutInflater layoutInflater = getLayoutInflater();
        List<String> genres = new ArrayList<>();
        try {
            AuthDto authDto = AuthDao.find();
            genres = authDto.getGenre();
        } catch (Exception e) {
            // 로그인되지 않은 경우
            genres = new ArrayList<>(Arrays.asList("KPOP", "BALLADE", "DANCE"));
        } finally {
            for (int i = 0; i < genres.size(); i++) {
                layoutInflater.inflate(R.layout.button_genre, binding.llGenreButtons);
                AppCompatToggleButton btnGenre = (AppCompatToggleButton) binding.llGenreButtons.getChildAt(
                        binding.llGenreButtons.getChildCount() - 1
                );
                btnGenre.setText(AuthDto.getGenreValue(genres.get(i)));
                btnGenre.setTextOn(AuthDto.getGenreValue(genres.get(i)));
                btnGenre.setTextOff(AuthDto.getGenreValue(genres.get(i)));
                btnGenre.setTag(genres.get(i));

                if (i == 0) {
                    btnGenre.setChecked(true);
                    homeViewModel.setSelectedGenre(genres.get(i));
                }

                btnGenre.setOnClickListener((v) -> {
                    if (homeViewModel.getSelectedGenre() == (String) v.getTag())
                        return;

                    for (int c = 0; c < binding.llGenreButtons.getChildCount(); c++) {
                        AppCompatToggleButton btn = (AppCompatToggleButton) binding.llGenreButtons.getChildAt(c);

                        if (btn.getTag() == v.getTag()) {
                            homeViewModel.setSelectedGenre((String) v.getTag());
                            homeViewModel.fetchGenreMusic(new HomeViewModel.OnMusicLoaded() {
                                @Override
                                public void setMusics(List<MusicListDto> musics) {
                                    genreMusicAdapter.clearItems();
                                    genreMusicAdapter.addItems(musics);
                                }
                            });

                        }
                        else {
                            btn.setChecked(false);
                        }
                    }
                });
            }
        }

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

        homeViewModel.fetchRecommendMusic(new HomeViewModel.OnMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                homeRecommendAdapter.clearItems();
                homeRecommendAdapter.addItems(musics);
            }
        });

        homeViewModel.fetchGenreMusic(new HomeViewModel.OnMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                genreMusicAdapter.clearItems();
                genreMusicAdapter.addItems(musics);
            }
        });

        homeViewModel.fetchRankMusic(new HomeViewModel.OnMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                rankMusicAdapter.setMusic(musics);
            }
        });

        homeViewModel.fetchRecentMusic(new HomeViewModel.OnMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                recentMusicAdapter.setMusics(musics);
            }
        });
    }
}