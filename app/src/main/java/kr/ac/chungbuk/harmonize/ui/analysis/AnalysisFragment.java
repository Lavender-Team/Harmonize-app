package kr.ac.chungbuk.harmonize.ui.analysis;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.FragmentAnalysisBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import kr.ac.chungbuk.harmonize.dto.CommonMusicResultDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.ui.home.HomeViewModel;
import kr.ac.chungbuk.harmonize.ui.music.MusicActivity;
import kr.ac.chungbuk.harmonize.ui.profile.GenderAgeActivity;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListFeedbackAdapter;

public class AnalysisFragment extends Fragment {

    FragmentAnalysisBinding binding;

    LinearLayoutManager listLayoutManager;
    MusicListFeedbackAdapter musicListFeedbackAdapter;

    public AnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // 맞춤 추천 정보 편집
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GenderAgeActivity.class));
            }
        });

        // 내 목소리 분석 열기/닫기
        binding.analysisDataToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.analysisData.getVisibility() == View.VISIBLE) {
                    binding.analysisData.animate().alpha(0.0f).translationY(-20);
                    binding.analysisData.setVisibility(View.GONE);
                    binding.analysisDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_down);
                    binding.analysisDataToggleText.setTextColor(getResources().getColor(R.color.gray_4, getContext().getTheme()));
                }
                else {
                    binding.analysisData.animate().alpha(1.0f).translationY(0);
                    binding.analysisData.setVisibility(View.VISIBLE);
                    binding.analysisDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_up);
                    binding.analysisDataToggleText.setTextColor(getResources().getColor(R.color.black, getContext().getTheme()));
                }
            }
        });

        binding.analysisData.animate().alpha(0.0f).translationY(-20);
        binding.analysisData.setVisibility(View.GONE);
        binding.analysisDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_down);
        binding.analysisDataToggleText.setTextColor(getResources().getColor(R.color.gray_4, getContext().getTheme()));

        // 맞춤 추천 정보 열기/닫기
        binding.userDataToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userData.getVisibility() == View.VISIBLE) {
                    binding.userData.animate().alpha(0.0f).translationY(-20);
                    binding.userData.setVisibility(View.GONE);
                    binding.userDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_down);
                    binding.userDataToggleText.setTextColor(getResources().getColor(R.color.gray_4, getContext().getTheme()));
                }
                else {
                    binding.userData.animate().alpha(1.0f).translationY(0);
                    binding.userData.setVisibility(View.VISIBLE);
                    binding.userDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_up);
                    binding.userDataToggleText.setTextColor(getResources().getColor(R.color.black, getContext().getTheme()));
                }
            }
        });

        binding.userData.animate().alpha(0.0f).translationY(-20);
        binding.userData.setVisibility(View.GONE);
        binding.userDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_down);
        binding.userDataToggleText.setTextColor(getResources().getColor(R.color.gray_4, getContext().getTheme()));


        // 목소리 재분석 버튼
        binding.btnVoiceRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VoiceRecordingActivity.class));
            }
        });


        /* MusicListView */
        listLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        musicListFeedbackAdapter = new MusicListFeedbackAdapter(new ArrayList<>(),
                getActivity(),
                new MusicListFeedbackAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
        binding.musicListView.setLayoutManager(listLayoutManager);
        binding.musicListView.setAdapter(musicListFeedbackAdapter);


        fetchRecommendMusic(new HomeViewModel.OnMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                musicListFeedbackAdapter.addItems(musics);
                musicListFeedbackAdapter.notifyDataSetChanged();
            }
        });

        // 재추천 버튼
        binding.btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRecommendAgain();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            AuthDto authDto = AuthDao.find();

            if (authDto.getHighestPitch() != null)
                binding.tvHighestPitch.setText(
                        PitchConverter.freqToPitch(authDto.getHighestPitch()) +
                                "(" + Math.round(authDto.getHighestPitch()) + ")"
                );
            else
                binding.tvHighestPitch.setText("-");

            if (authDto.getLowestPitch() != null)
                binding.tvLowestPitch.setText(
                        PitchConverter.freqToPitch(authDto.getLowestPitch()) +
                                "(" + Math.round(authDto.getLowestPitch()) + ")"
                );
            else
                binding.tvLowestPitch.setText("-");

            binding.tvGender.setText(authDto.getGender());
            binding.tvAge.setText(authDto.getAge().toString() + "대");

            StringBuilder genreString = new StringBuilder();
            for (int i = 0; i < authDto.getGenreValue().size(); i++) {
                genreString.append(authDto.getGenreValue().get(i));
                if (i != authDto.getGenreValue().size() - 1)
                    genreString.append(", ");
            }
            binding.tvGenre.setText(genreString.toString());
        } catch (Exception e) {
            // 로그인되지 않음
        }
    }

    private void fetchRecommendMusic(HomeViewModel.OnMusicLoaded loadedListener) {
        String userId = AuthDao.getUserId();
        if (userId.isEmpty())
            return;

        StringRequest genreMusicRequest = new StringRequest(
                Request.Method.GET,
                Domain.url("/api/music/recommend?size=8&userId="+userId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CommonMusicResultDto recommendMusicResult = gson.fromJson(response, CommonMusicResultDto.class);

                        loadedListener.setMusics(recommendMusicResult.getContent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getAppContext(),
                                "내 목소리 맞춤 추천곡을 가져오는 중 오류가 발생하였습니다.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        VolleySingleton.getInstance(getAppContext()).addToRequestQueue(genreMusicRequest);
    }

    private void requestRecommendAgain() {
        String userId = AuthDao.getUserId();
        if (userId.isEmpty())
            return;

        musicListFeedbackAdapter.clearItems();
        musicListFeedbackAdapter.notifyDataSetChanged();

        StringRequest recommendRequest = new StringRequest(
                Request.Method.POST,
                Domain.url("/api/music/recsys/collaborative?userId="+userId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fetchRecommendMusic(new HomeViewModel.OnMusicLoaded() {
                            @Override
                            public void setMusics(List<MusicListDto> musics) {
                                musicListFeedbackAdapter.addItems(musics);
                                musicListFeedbackAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getAppContext(),
                                "음악 재추천 요청 중 오류가 발생하였습니다.",
                                Toast.LENGTH_SHORT
                        ).show();

                        fetchRecommendMusic(new HomeViewModel.OnMusicLoaded() {
                            @Override
                            public void setMusics(List<MusicListDto> musics) {
                                musicListFeedbackAdapter.addItems(musics);
                                musicListFeedbackAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        VolleySingleton.getInstance(getAppContext()).addToRequestQueue(recommendRequest);
    }
}