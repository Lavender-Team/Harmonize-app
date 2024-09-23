package kr.ac.chungbuk.harmonize.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.GsonDateSupport;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.ActivityLoginBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Event Listener 추가
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                // 입력된 아이디가 있으면 SignupActivity로 전달
                if (!binding.etId.getText().toString().isEmpty())
                    intent.putExtra("id", binding.etId.getText().toString());
                startActivity(intent);
            }
        });

        binding.btnFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindIdActivity.class));
            }
        });

        binding.btnFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
            }
        });
    }

    private boolean checkBeforeSubmit() {
        if (binding.etId.getText().toString().equals("")) {
            Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (binding.etPassword.getText().toString().equals("")) {
            Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private void login()
    {
        if (checkBeforeSubmit())
            return;

        Map<String,String> bodyParams = new HashMap<String,String>();
        bodyParams.put("loginId", binding.etId.getText().toString());
        bodyParams.put("password", binding.etPassword.getText().toString());


        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                Domain.url("/api/user/login"),
                new JSONObject(bodyParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseString = response.toString(); // gson 이용하기 위해 다시 문자열 변환

                        Gson gson = GsonDateSupport.getInstance();
                        AuthDto authDto = gson.fromJson(responseString, AuthDto.class);
                        
                        AuthDao.save(authDto);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

        };

        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

}