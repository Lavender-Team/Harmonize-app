package kr.ac.chungbuk.harmonize.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.databinding.ActivityGenderAgeBinding;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;

public class GenderAgeActivity extends AppCompatActivity {

    ActivityGenderAgeBinding binding;

    String selectedGender;
    String selectedAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenderAgeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이벤트 리스너
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 성별 선택 리스너
        View.OnClickListener genderListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectedGender = ((ThemedButton) v).getText();
            }
        };
        binding.genderMale.setOnClickListener(genderListener);
        binding.genderFemale.setOnClickListener(genderListener);

        // 나이대 선택 리스너
        View.OnClickListener ageListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectedAge = ((ThemedButton) v).getText();
            }
        };
        binding.age10.setOnClickListener(ageListener);
        binding.age20.setOnClickListener(ageListener);
        binding.age30.setOnClickListener(ageListener);
        binding.age40.setOnClickListener(ageListener);
        binding.age50.setOnClickListener(ageListener);
        binding.age60.setOnClickListener(ageListener);

        // 다음 버튼 리스너
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Toast toast = Toast.makeText(getApplicationContext(),
                        "TODO: 지금 로그인과 상관 없이 userId 2번 유저를 수정하도록 구현돼있음", Toast.LENGTH_LONG);
                toast.show();

                saveGenderAge();
            }
        });
    }

    private void saveGenderAge() {
        if (selectedGender.isEmpty() || selectedAge.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "성별과 나이대를 반드시 선택해야 합니다!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        StringRequest updateRequest = new StringRequest(
                Request.Method.PUT,
                Domain.url("/api/user/2"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 성공시 현재 액티비티 종료 및 선호 장르 선택 액티비티 열기
                        Intent intent = new Intent(GenderAgeActivity.this, GenreActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gender", getSelectedGenderValue());
                params.put("age", getSelectedAgeValue());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(updateRequest);
    }

    private String getSelectedGenderValue() {
        if (selectedGender.equals("남성"))
            return "MALE";
        else
            return "FEMALE";
    }

    private String getSelectedAgeValue() {
        return switch (selectedAge) {
            case "10대" -> "10";
            case "20대" -> "20";
            case "30대" -> "30";
            case "40대" -> "40";
            case "50대" -> "50";
            default -> "60";
        };
    }
}