package kr.ac.chungbuk.harmonize.ui.analysis;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
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
import com.google.gson.Gson;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.GsonDateSupport;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.ActivityVoiceRecordingBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.dto.VoiceAnalysisDto;
import kr.ac.chungbuk.harmonize.ui.home.HomeViewModel;
import kr.ac.chungbuk.harmonize.utility.AudioRecorder;
import kr.ac.chungbuk.harmonize.utility.CustomMultipartRequest;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;

public class VoiceRecordingActivity extends AppCompatActivity {

    ActivityVoiceRecordingBinding binding;

    AudioRecorder audioRecorder;

    private double highestPitch;
    private double lowestPitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoiceRecordingBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        audioRecorder = new AudioRecorder();

        /* 뒤로 가기 버튼 리스너 */
        binding.btnBack.setOnClickListener((v) -> { finish(); });

        binding.btnManualInput.setOnClickListener((v) -> {
            binding.llRecording.setVisibility(View.GONE);
            binding.llInput.setVisibility(View.VISIBLE);
            binding.btnManualInput.setVisibility(View.GONE);
            binding.llButtons.setVisibility(View.GONE);
            binding.flNext.setVisibility(View.VISIBLE);
        });

        binding.btnRecord.setOnClickListener((v) -> {
            audioRecorder.startRecording(new AudioRecorder.RecordingCallback() {
                @Override
                public void onRecordingStarted() {
                    binding.tvRecordingLabel.setTextColor(Color.GRAY);
                    binding.tvRecordingLabel.setText("듣고 있어요...");
                }

                @Override
                public void onVoiceDetacted() {
                    binding.tvRecordingLabel.setTextColor(Color.BLACK);
                    binding.llActionLabel.setVisibility(View.VISIBLE);
                    startCountdown();
                }

                @Override
                public void onRecordingStopped() {
                    binding.llActionLabel.setVisibility(View.GONE);
                    binding.tvRecordingLabel.setText("녹음 완료");
                    uploadUserVoice();
                    binding.tvRecordingLabel.setText("서버로 전송 중...");
                }
            });
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

    private void uploadUserVoice() {
        String userId = AuthDao.getUserId();
        if (userId.isEmpty() || audioRecorder == null || audioRecorder.getFilePath() == null)
            return;

        File audioFile = new File(audioRecorder.getFilePath());

        CustomMultipartRequest request = new CustomMultipartRequest(
                Domain.url("/api/user/uasys/analyze?userId="+userId),
                audioFile,
                response -> {
                    // 결과 파싱
                    Gson gson = GsonDateSupport.getInstance();
                    VoiceAnalysisDto voiceAnalysis = gson.fromJson(response, VoiceAnalysisDto.class);

                    // 결과로 받은 Pitch 선택
                    binding.tctvHighestPitch.setText(PitchConverter.getPitchMenuString(voiceAnalysis.getMaxPitch()), false);
                    binding.tctvLowestPitch.setText(PitchConverter.getPitchMenuString(voiceAnalysis.getMinPitch()), false);

                    try {
                        highestPitch = PitchConverter.pitchFrequencyMap.get(binding.tctvHighestPitch.getText().toString());
                        lowestPitch = PitchConverter.pitchFrequencyMap.get(binding.tctvLowestPitch.getText().toString());
                    } catch (Exception e) {
                        binding.tctvHighestPitch.setText("", false);
                        binding.tctvLowestPitch.setText("", false);
                    }

                    binding.llRecording.setVisibility(View.GONE);
                    binding.llInput.setVisibility(View.VISIBLE);
                    binding.btnManualInput.setVisibility(View.GONE);
                    binding.llButtons.setVisibility(View.GONE);
                    binding.flNext.setVisibility(View.VISIBLE);
                },
                error -> {
                    // 파일 업로드에 실패했을 때
                    binding.tvRecordingLabel.setTextColor(Color.GRAY);
                    binding.tvRecordingLabel.setText("오류 발생!\n다시 시도하려면 아래 버튼을 눌러주세요.");
//                    Toast.makeText(
//                            getAppContext(),
//                            "분석 업로드 중 오류가 발생하였습니다.",
//                            Toast.LENGTH_SHORT
//                    ).show();
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

        VolleySingleton.getInstance(getAppContext()).addToRequestQueue(request);
    }

    private void postUserAnalysis() {
        String userId = AuthDao.getUserId();
        if (userId.isEmpty())
            return;

        StringRequest genreMusicRequest = new StringRequest(
                Request.Method.POST,
                Domain.url("/api/user/" + userId + "/analysis?highestPitch="+highestPitch+"&lowestPitch="+lowestPitch),
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
        VolleySingleton.getInstance(getAppContext()).addToRequestQueue(recommendRequest);
    }

    private void startCountdown() {
        new CountDownTimer(19000, 1000) { // 19초 동안 1초 간격
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000) + 1;
                binding.tvRecordingLabel.setText(
                        "하모나이즈는 목소리 분석을 통해\n" +
                        "부르기 좋은 음악을 추천합니다.\n\n" +
                        "목소리 톤에서 적합한 음역대를\n" +
                        "알아내고, 선호하시는 음악을\n" +
                        "찾아드리겠습니다. " +
                        "(" + secondsRemaining + "초)");
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}