package kr.ac.chungbuk.harmonize.ui.profile;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.databinding.ActivityFeedbackBinding;
import kr.ac.chungbuk.harmonize.dto.CommonMusicResultDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.ui.home.HomeViewModel;
import kr.ac.chungbuk.harmonize.ui.music.MusicActivity;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListFeedbackAdapter;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;

    LinearLayoutManager listLayoutManager;
    MusicListFeedbackAdapter musicListFeedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /* 뒤로 가기 버튼 리스너 */
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        /* MusicListView */
        listLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        musicListFeedbackAdapter = new MusicListFeedbackAdapter(new ArrayList<>(),
                this,
                new MusicListFeedbackAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
        binding.musicListView.setLayoutManager(listLayoutManager);
        binding.musicListView.setAdapter(musicListFeedbackAdapter);

        fetchFirstFeedbackMusic(new HomeViewModel.OnMusicLoaded() {
            @Override
            public void setMusics(List<MusicListDto> musics) {
                musicListFeedbackAdapter.addItems(musics);
                musicListFeedbackAdapter.notifyDataSetChanged();
            }
        });

        // 다음 버튼 리스너
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicListFeedbackAdapter.getFeedbackCount() < 3) {
                    Toast.makeText(getAppContext(), "최소 3개 곡에 대해 선호를 선택하세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                startActivity(intent);
            }
        });
    }

    public void fetchFirstFeedbackMusic(HomeViewModel.OnMusicLoaded loadedListener) {
        StringRequest firstMusicRequest = new StringRequest(
                Request.Method.GET,
                Domain.url("/api/music/first-feedback"),
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
                                "곡을 가져오는 중 오류가 발생하였습니다.",
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
        VolleySingleton.getInstance(getAppContext()).addToRequestQueue(firstMusicRequest);
    }
}