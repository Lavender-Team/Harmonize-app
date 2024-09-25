package kr.ac.chungbuk.harmonize.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.FragmentMoreBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import kr.ac.chungbuk.harmonize.ui.profile.GenderAgeActivity;
import kr.ac.chungbuk.harmonize.ui.profile.LoginActivity;
import kr.ac.chungbuk.harmonize.ui.test.TestActivity;
import kr.ac.chungbuk.harmonize.utility.adapter.MoreMenuAdapter;

public class MoreFragment extends Fragment {

    FragmentMoreBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMoreBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GenderAgeActivity.class));
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        MoreMenuAdapter adapter = new MoreMenuAdapter(getContext());
        binding.menuListView.setAdapter(adapter);

        binding.menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 서비스 소개 클릭시
                        startActivity(new Intent(getActivity(), TestActivity.class)); break;
                    default:
                        break;
                }
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            AuthDto authDto = AuthDao.find();
            setProfileText(authDto);

        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            AuthDto authDto = AuthDao.find();
            setProfileText(authDto);

        } catch (Exception e) {
            return;
        }
    }

    private void setProfileText(AuthDto authDto) {
        if (authDto.getGender().equals("") || authDto.getAge() == 0) {
            binding.tvNickname.setText("");
            binding.tvGender.setText("");
            binding.tvAge.setText("");
        } else {
            binding.tvNickname.setText(authDto.getNickname());
            binding.tvGender.setText(authDto.getGender());
            binding.tvAge.setText(authDto.getAge().toString() + "대");
        }

        binding.llGenres.removeAllViews();
        for (String genre : authDto.getGenreValue()) {
            getLayoutInflater().inflate(R.layout.textview_theme, binding.llGenres);
            TextView tvTheme = (TextView) binding.llGenres.getChildAt(binding.llGenres.getChildCount() - 1);
            tvTheme.setText(genre);
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("로그아웃");
        builder.setMessage("정말로 로그아웃 하시겠습니까?");

        builder.setPositiveButton("확인", (dialog, which) -> {
            // 로그아웃 처리
            logout();
        });

        builder.setNegativeButton("취소", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.create().show();
    }

    private void logout() {
        AuthDao.delete();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null)
            getActivity().finish();
    }
}
