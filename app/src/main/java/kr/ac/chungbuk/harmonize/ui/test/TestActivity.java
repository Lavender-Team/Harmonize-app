package kr.ac.chungbuk.harmonize.ui.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.ui.profile.GenderAgeActivity;
import kr.ac.chungbuk.harmonize.ui.profile.GenreActivity;
import kr.ac.chungbuk.harmonize.ui.profile.LoginActivity;

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
    }
}