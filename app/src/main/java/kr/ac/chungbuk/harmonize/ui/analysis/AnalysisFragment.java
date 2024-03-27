package kr.ac.chungbuk.harmonize.ui.analysis;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.Resource;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentAnalysisBinding;

public class AnalysisFragment extends Fragment {

    FragmentAnalysisBinding binding;

    public AnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // 내 목소리 분석 열기/닫기
        binding.analysisDataToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.analysisData.getVisibility() == View.VISIBLE) {
                    binding.analysisData.animate().alpha(0.0f).translationY(-20);
                    binding.analysisData.setVisibility(View.GONE);
                    binding.analysisDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_down);
                    binding.analysisDataToggleText.setTextColor(getResources().getColor(R.color.gray_4, getContext().getTheme()));
                }
                else {
                    binding.analysisData.animate().alpha(1.0f).translationY(0);
                    binding.analysisData.setVisibility(View.VISIBLE);
                    binding.analysisDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_up);
                    binding.analysisDataToggleText.setTextColor(getResources().getColor(R.color.black, getContext().getTheme()));
                }
            }
        });

        // 맞춤 추천 정보 열기/닫기
        binding.userDataToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userData.getVisibility() == View.VISIBLE) {
                    binding.userData.animate().alpha(0.0f).translationY(-20);
                    binding.userData.setVisibility(View.GONE);
                    binding.userDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_down);
                    binding.userDataToggleText.setTextColor(getResources().getColor(R.color.gray_4, getContext().getTheme()));
                }
                else {
                    binding.userData.animate().alpha(1.0f).translationY(0);
                    binding.userData.setVisibility(View.VISIBLE);
                    binding.userDataToggleIcon.setImageResource(R.drawable.ic_arrow_drop_up);
                    binding.userDataToggleText.setTextColor(getResources().getColor(R.color.black, getContext().getTheme()));
                }
            }
        });
    }
}