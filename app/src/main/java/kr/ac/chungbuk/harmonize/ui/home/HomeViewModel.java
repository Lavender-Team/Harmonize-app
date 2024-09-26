package kr.ac.chungbuk.harmonize.ui.home;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
import kr.ac.chungbuk.harmonize.dto.CommonMusicResultDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import lombok.Getter;

public class HomeViewModel extends ViewModel {

    public interface OnRecentMusicLoaded {
        void setMusics(List<MusicListDto> musics);
    }

    @Getter
    private ObservableArrayList<SimpleMusic> homeRecommendMusics = new ObservableArrayList<>();
    @Getter
    private ObservableArrayList<SimpleMusic> artists = new ObservableArrayList<>();
    @Getter
    private ObservableArrayList<SimpleMusic> genreMusics = new ObservableArrayList<>();

    public HomeViewModel() {
        homeRecommendMusics.add(new SimpleMusic("사건의 지평선", "윤하(YOUNHA)"));
        homeRecommendMusics.add(new SimpleMusic("좋니", "윤종신"));
        homeRecommendMusics.add(new SimpleMusic("기억의 습작", "김동률"));
        homeRecommendMusics.add(new SimpleMusic("좋니", "윤종신"));

        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));
        artists.add(new SimpleMusic("+13", "잔나비"));

        genreMusics.add(new SimpleMusic("헤어지자말해요", "박재정"));
        genreMusics.add(new SimpleMusic("비의랩소디", "임재현"));
        genreMusics.add(new SimpleMusic("가질수없는너", "뱅크"));

    }

    public void fetchRankMusic(OnRecentMusicLoaded loadedListener) {
        StringRequest rankMusicRequest = new StringRequest(
                Request.Method.GET,
                Domain.url("/api/music/rank"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CommonMusicResultDto rankMusicResult = gson.fromJson(response, CommonMusicResultDto.class);

                        loadedListener.setMusics(rankMusicResult.getContent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getAppContext(),
                                "인기곡 순위를 가져오는 중 오류가 발생하였습니다.",
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
        VolleySingleton.getInstance(getAppContext()).addToRequestQueue(rankMusicRequest);
    }

    public void fetchRecentMusic(OnRecentMusicLoaded loadedListener) {
        StringRequest rankMusicRequest = new StringRequest(
                Request.Method.GET,
                Domain.url("/api/music/recent"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CommonMusicResultDto recentMusicResult = gson.fromJson(response, CommonMusicResultDto.class);

                        loadedListener.setMusics(recentMusicResult.getContent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getAppContext(),
                                "최신 음악 목록을를 가져오는 중 오류가 발생하였습니다.",
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
        VolleySingleton.getInstance(getAppContext()).addToRequestQueue(rankMusicRequest);
    }
}