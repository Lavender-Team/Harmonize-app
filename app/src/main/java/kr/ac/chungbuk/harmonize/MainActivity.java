package kr.ac.chungbuk.harmonize;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.ActivityMainBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import kr.ac.chungbuk.harmonize.ui.analysis.VoiceRecordingActivity;
import kr.ac.chungbuk.harmonize.ui.profile.FeedbackActivity;
import kr.ac.chungbuk.harmonize.ui.profile.GenderAgeActivity;
import kr.ac.chungbuk.harmonize.ui.profile.LoginActivity;
import kr.ac.chungbuk.harmonize.ui.test.TestActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Make light theme only */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_analytics, R.id.navigation_search, R.id.navigation_favorite, R.id.navigation_menu)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AuthDto authDto;

        // 로그인되지 않은 상태이면 로그인 액티비티 표시
        try {
            authDto = AuthDao.find();
        } catch (Exception e) {
            openActivityIfNotInStack(this, LoginActivity.class);
            return;
        }

        if (authDto.getAge() == 0 || authDto.getGender().isBlank() || authDto.getGenre().isEmpty()) {
            openActivityIfNotInStack(this, GenderAgeActivity.class);
            return;
        }

        if (authDto.getHighestPitch() == null || authDto.getLowestPitch() == null) {
            openActivityIfNotInStack(this, VoiceRecordingActivity.class);
            return;
        }
    }

    public void openActivityIfNotInStack(Context context, Class<?> targetActivity) {
        boolean isActivityInStack = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();

        for (ActivityManager.AppTask task : tasks) {
            if (task.getTaskInfo().topActivity.getClassName().equals(targetActivity.getName())) {
                isActivityInStack = true;
                break;
            }
        }

        if (!isActivityInStack) {
            Intent intent = new Intent(context, targetActivity);
            context.startActivity(intent);
        }
    }

}