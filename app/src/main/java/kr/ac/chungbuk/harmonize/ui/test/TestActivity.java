package kr.ac.chungbuk.harmonize.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.ui.music.MusicActivity;
import kr.ac.chungbuk.harmonize.ui.profile.FindIdActivity;
import kr.ac.chungbuk.harmonize.ui.profile.FindPasswordActivity;
import kr.ac.chungbuk.harmonize.ui.profile.GenderAgeActivity;
import kr.ac.chungbuk.harmonize.ui.profile.GenreActivity;
import kr.ac.chungbuk.harmonize.ui.profile.LoginActivity;
import kr.ac.chungbuk.harmonize.ui.profile.SignupActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        AppCompatButton btnOpenLogin = findViewById(R.id.btnOpenLogin);
        btnOpenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, LoginActivity.class));
            }
        });

        AppCompatButton btnOpenGenderAge = findViewById(R.id.btnOpenGenderAge);
        btnOpenGenderAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, GenderAgeActivity.class));
            }
        });

        AppCompatButton btnOpenGenre = findViewById(R.id.btnOpenGenre);
        btnOpenGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, GenreActivity.class));
            }
        });

        AppCompatButton btnOpenSignup = findViewById(R.id.btnOpenSignup);
        btnOpenSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, SignupActivity.class));
            }
        });

        AppCompatButton btnOpenFindId = findViewById(R.id.btnOpenFindId);
        btnOpenFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, FindIdActivity.class));
            }
        });

        AppCompatButton btnOpenFindPassword = findViewById(R.id.btnOpenFindPassword);
        btnOpenFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, FindPasswordActivity.class));
            }
        });

        AppCompatButton btnOpenMusic = findViewById(R.id.btnOpenMusic);
        btnOpenMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, MusicActivity.class));
            }
        });
    }
}