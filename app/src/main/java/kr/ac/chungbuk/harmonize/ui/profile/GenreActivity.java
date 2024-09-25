package kr.ac.chungbuk.harmonize.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.ActivityGenreBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;

public class GenreActivity extends AppCompatActivity {

    ActivityGenreBinding binding;

    List<String> selectedGenres = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이벤트 리스너
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 장르 선택 리스너
        View.OnClickListener genreClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemedButton button = (ThemedButton) view;
                String genre = button.getText();

                if (selectedGenres.contains(genre)) {
                    selectedGenres.remove(genre);
                } else {
                    selectedGenres.add(genre);
                }
            }
        };

        // 장르 버튼에 클릭 리스너 등록
        for (int i = 0; i < binding.genres.getChildCount(); i++) {
            View child = binding.genres.getChildAt(i);
            if (child instanceof ThemedButton) {
                child.setOnClickListener(genreClickListener);
            }
        }

        // 저장 버튼 리스너
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSelectedGenres();
            }
        });
    }

    private void saveSelectedGenres() {
        if (selectedGenres.size() != 3) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "선호 장르를 반드시 3개 선택해야 합니다!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                Domain.url("/api/user/"+AuthDao.getUserId()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 로컬(SQLite DB)에도 선호 장르를 저장
                        try {
                            AuthDto authDto = AuthDao.find();
                            authDto.setGenreValue(selectedGenres);
                            AuthDao.save(authDto);
                        } catch (Exception ignored) {}

                        // 성공시 메인 액티비티 위 모든 액티비티 종료 및 메인 액티비티 열기
                        Intent intent = new Intent(GenreActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "요청 전송 중 오류가 발생하였습니다.", Toast.LENGTH_LONG);
                        toast.show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("genre", getSelectedGenreString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(updateRequest);
    }

    private String getSelectedGenreString() {
        StringBuilder ret = new StringBuilder();

        for (String g : selectedGenres) {
            ret.append(switch (g) {
                case "가요" -> "KPOP,";
                case "팝송" -> "POP,";
                case "발라드" -> "BALLADE,";
                case "랩/힙합" -> "RAP,";
                case "댄스" -> "DANCE,";
                case "일본곡" -> "JPOP,";
                case "R&B" -> "RNB,";
                case "포크/블루스" -> "FOLK,";
                case "록/메탈" -> "ROCK,";
                case "OST" -> "OST,";
                case "인디뮤직" -> "INDIE,";
                case "트로트" -> "TROT,";
                default -> "KID,";
            });
        }
        ret.deleteCharAt(ret.length() - 1);
        return ret.toString();
    }
}