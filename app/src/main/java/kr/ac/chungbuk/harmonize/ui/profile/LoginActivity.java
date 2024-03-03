package kr.ac.chungbuk.harmonize.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.ui.test.TestActivity;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etId;
    AppCompatButton btnSignup, btnFindId, btnFindPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = findViewById(R.id.etId);
        btnSignup = findViewById(R.id.btnSignup);
        btnFindId = findViewById(R.id.btnFindId);
        btnFindPassword = findViewById(R.id.btnFindPassword);


        // Event Listener 추가
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                // 입력된 아이디가 있으면 SignupActivity로 전달
                if (!etId.getText().toString().isEmpty())
                    intent.putExtra("id", etId.getText().toString());
                startActivity(intent);
            }
        });

        btnFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindIdActivity.class));
            }
        });

        btnFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
            }
        });
    }



}