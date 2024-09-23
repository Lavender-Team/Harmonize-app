package kr.ac.chungbuk.harmonize.ui.profile;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.databinding.ActivitySignupBinding;
import kr.ac.chungbuk.harmonize.utility.error.ErrorDetail;
import kr.ac.chungbuk.harmonize.utility.error.ErrorResult;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // LoginActivity에서 전달받은 아이디가 있다면 초기 값으로 설정
        String receivedId = getIntent().getStringExtra("id");
        if (receivedId != null) {
            binding.etId.setText(receivedId);
        }

        /* 체크박스 문구 설정 */
        String checkBoxText = "&nbsp;&nbsp;<a href='http://www.naver.com/'>이용 약관</a>과 <a href='http://www.naver.com/'>개인정보 처리방침</a>에 동의합니다.";
        binding.cbAgree.setText(Html.fromHtml(checkBoxText, FROM_HTML_MODE_COMPACT));
        binding.cbAgree.setMovementMethod(LinkMovementMethod.getInstance());


        // Event Listener 추가
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private boolean checkBeforeSubmit() {
        if (!binding.etPassword.getText().toString().equals(binding.etRePassword.getText().toString())) {
            binding.setRePasswordError("비밀번호 재확인이 일치하지 않습니다.");
            return true;
        }

        if (!binding.cbAgree.isChecked()) {
            Toast toast = Toast.makeText(this,"이용약관에 동의하셔야 회원 가입할 수 있습니다.", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return false;
    }

    private void signup() {
        clearError();

        if (checkBeforeSubmit())
            return;

        StringRequest signupRequest = new StringRequest(
                Request.Method.POST,
                Domain.url("/api/user"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // TODO: 회원가입 성공시 로그인 후 액티비티 종료, 나이 및 연령대 설정
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "TODO: 회원가입 성공시 로그인 후 액티비티 종료, 나이 및 연령대 설정", Toast.LENGTH_LONG);
                        toast.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorResponse = new String(error.networkResponse.data);

                            Gson gson = new Gson();
                            ErrorResult errors = gson.fromJson(errorResponse, ErrorResult.class);

                            if (errors.getFieldErrors() != null) {
                                for (ErrorDetail e : errors.getFieldErrors()) {
                                    switch (e.getField()) {
                                        case "loginId":
                                            binding.setIdError(e.getMessage()); break;
                                        case "password":
                                            binding.setPasswordError(e.getMessage()); break;
                                        case "email":
                                            binding.setEmailError(e.getMessage()); break;
                                        case "nickname":
                                            binding.setNicknameError(e.getMessage()); break;
                                    }
                                }
                            }

                            if (errors.getObjectErrors() != null) {
                                for (ErrorDetail e : errors.getObjectErrors()) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            e.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "요청 전송 중 오류가 발생하였습니다.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("loginId", binding.etId.getText().toString());
                params.put("password", binding.etPassword.getText().toString());
                params.put("email", binding.etEmail.getText().toString());
                params.put("nickname", binding.etNickname.getText().toString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(signupRequest);
    }

    private void clearError() {
        binding.setIdError("");
        binding.setPasswordError("");
        binding.setRePasswordError("");
        binding.setEmailError("");
        binding.setNicknameError("");
    }
}