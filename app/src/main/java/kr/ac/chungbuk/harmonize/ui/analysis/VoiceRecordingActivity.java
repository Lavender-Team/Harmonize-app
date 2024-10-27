package kr.ac.chungbuk.harmonize.ui.analysis;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.ActivityVoiceRecordingBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.ui.home.HomeViewModel;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;

public class VoiceRecordingActivity extends AppCompatActivity {

    ActivityVoiceRecordingBinding binding;

    private double highestPitch;
    private double lowestPitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoiceRecordingBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        /* 뒤로 가기 버튼 리스너 */
        binding.btnBack.setOnClickListener((v) -> { finish(); });

        binding.btnManualInput.setOnClickListener((v) -> {
            binding.llInput.setVisibility(View.VISIBLE);
            binding.btnManualInput.setVisibility(View.GONE);
            binding.llButtons.setVisibility(View.GONE);
            binding.flNext.setVisibility(View.VISIBLE);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, PitchConverter.pitchStringList);

        binding.tctvHighestPitch.setAdapter(adapter);
        binding.tctvHighestPitch.setOnClickListener(v -> binding.tctvHighestPitch.showDropDown());

        binding.tctvLowestPitch.setAdapter(adapter);
        binding.tctvLowestPitch.setOnClickListener(v -> binding.tctvLowestPitch.showDropDown());

        binding.tctvHighestPitch.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPitch = (String) parent.getItemAtPosition(position);
            highestPitch = PitchConverter.pitchFrequencyMap.get(selectedPitch);
        });

        binding.tctvLowestPitch.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPitch = (String) parent.getItemAtPosition(position);
            lowestPitch = PitchConverter.pitchFrequencyMap.get(selectedPitch);
        });

        binding.btnNext.setOnClickListener((v) -> {
            postUserAnalysis();
        });
    }

    private void postUserAnalysis() {
        String userId = AuthDao.getUserId();
        if (userId.isEmpty())
            return;

        StringRequest genreMusicRequest = new StringRequest(
                Request.Method.POST,
                Domain.url("/api/user/analysis?userId="+userId+"&highestPitch="+highestPitch+"&lowestPitch="+lowestPitch),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AuthDto authDto = AuthDao.find();
                            authDto.setHighestPitch(highestPitch);
                            authDto.setLowestPitch(lowestPitch);
                            AuthDao.save(authDto);
                        } catch (Exception ignored) { }

                        requestRecommendAgain();

                        Intent intent = new Intent(VoiceRecordingActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getAppContext(),
                                "음역대 분석 결과 업로드 중 오류가 발생하였습니다.",
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

        StringRequest recommendRequest = new StringRequest(
                Request.Method.POST,
                Domain.url("/api/music/recsys/collaborative?userId="+userId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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