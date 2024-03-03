package kr.ac.chungbuk.harmonize.ui.profile;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;

import kr.ac.chungbuk.harmonize.R;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageButton btnBack = findViewById(R.id.btnBack);
        etId = findViewById(R.id.etId);

        // LoginActivity에서 전달받은 아이디가 있다면 초기 값으로 설정
        String receivedId = getIntent().getStringExtra("id");
        if (receivedId != null) {
            etId.setText(receivedId);
        }
        
        // Event Listener 추가
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /* 체크박스 문구 설정 */
        CheckBox cbAgree = findViewById(R.id.cbAgree);
        String checkBoxText = "&nbsp;&nbsp;<a href='http://www.naver.com/'>이용 약관</a>과 <a href='http://www.naver.com/'>개인정보 처리방침</a>에 동의합니다.";
        cbAgree.setText(Html.fromHtml(checkBoxText, FROM_HTML_MODE_COMPACT));
        cbAgree.setMovementMethod(LinkMovementMethod.getInstance());



    }
}