package kr.ac.chungbuk.harmonize.ui.bookmark;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.util.Log;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.dto.CommonMusicResultDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookmarkViewModel extends ViewModel {

    public interface OnMusicLoaded {
        void addMusics(List<MusicListDto> musics);
    }

    @Getter
    List<MusicListDto> musicListState = new ArrayList<>();
    int currentPage = -1;
    int totalPages = 0;

    public void fetchBookmarkedMusic(OnMusicLoaded loadedListener) {
        if (currentPage != -1 && currentPage >= totalPages) {
            return; // 마지막 페이지 이상 조회시 반환
        }

        currentPage++;
        StringRequest rankMusicRequest = new StringRequest(
                Request.Method.GET,
                Domain.url("/api/music/bookmarked?page=" + currentPage),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CommonMusicResultDto bookmarkedMusicResult = gson.fromJson(response, CommonMusicResultDto.class);

                        totalPages = bookmarkedMusicResult.getTotalPages();

                        loadedListener.addMusics(bookmarkedMusicResult.getContent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getAppContext(),
                                "좋아요 표시한 노래를 가져오는 중 오류가 발생하였습니다.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", AuthDao.getToken());
                return params;
            }

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

    public void clearMusicState() {
        currentPage = -1;
        totalPages = 0;
    }

}