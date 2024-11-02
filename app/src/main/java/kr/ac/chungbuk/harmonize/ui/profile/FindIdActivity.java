package kr.ac.chungbuk.harmonize.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.GsonDateSupport;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.ActivityFindIdBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import kr.ac.chungbuk.harmonize.dto.SimpleMessageDto;

public class FindIdActivity extends AppCompatActivity {

    ActivityFindIdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindIdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener((v) -> {
            finish();
        });

        binding.btnSubmit.setOnClickListener((v) -> {
            requestFindId();
        });
    }

    private void requestFindId() {
        if (binding.etEmail.getText() == null || binding.etEmail.getText().toString().isBlank()) {
            return;
        }

        binding.btnSubmit.setEnabled(false);

        binding.tvError.setText("");

        Map<String,String> bodyParams = new HashMap<String,String>();
        bodyParams.put("email", binding.etEmail.getText().toString());

        StringRequest findIdRequest = new StringRequest(
            Request.Method.POST,
            Domain.url("/api/user/find-id"),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String response = new String(error.networkResponse.data);

                        Gson gson = GsonDateSupport.getInstance();
                        SimpleMessageDto message = gson.fromJson(response, SimpleMessageDto.class);

                        if (statusCode == 404 || statusCode == 500) {
                            binding.tvError.setText(message.getMessage());
                        }
                        else {
                            binding.tvError.setText("아이디 찾기 중 오류가 발생하였습니다.");
                        }

                        binding.btnSubmit.setEnabled(true);
                    }
                    else {
                        // 요청 성공시 로직
                        Toast.makeText(FindIdActivity.this, "입력한 이메일로 아이디가 전송되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return bodyParams;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(findIdRequest);
    }
}