package kr.ac.chungbuk.harmonize.ui.music;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.databinding.FragmentMetadataBinding;
import kr.ac.chungbuk.harmonize.dto.ArtistDto;
import kr.ac.chungbuk.harmonize.dto.GroupDto;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import kr.ac.chungbuk.harmonize.utility.adapter.RecentMusicListAdapter;

public class MetadataFragment extends Fragment {

    FragmentMetadataBinding binding;

    public MetadataFragment() {
        // Required empty public constructor
    }

    public static MetadataFragment newInstance() {
        MetadataFragment fragment = new MetadataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMetadataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void setData(MusicDto music) {
        if (music == null || binding == null)
            return;

        // 노래방 번호
        binding.tvKaraokeNum.setText(music.getKaraokeNum());
        // 장르
        binding.tvGenre.setText(music.getGenreName());
        // 발매일
        binding.tvReleaseDate.setText(
                music.getReleaseDate() == null ?
                        "-" :
                        music.getReleaseDate().format(DateTimeFormatter.ISO_DATE)
        );

        GroupDto group = music.getGroup();

        // 그룹 이름
        binding.tvArtist.setText(group.getGroupName());
        // 그룹 프로필 이미지
        Glide.with(getAppContext())
                .load(Domain.url(group.getProfileImage()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(new ColorDrawable(Color.parseColor("#F6F6F6")))
                .into(binding.ivProfileImage);

        // 멤버
        List<ArtistDto> members = group.getMembers();
        StringBuilder memberNames = new StringBuilder();
        int[] genderCount = new int[2];

        for (int i = 0; i < members.size(); i++) {
            if (i != members.size() - 1) // 마지막 멤버가 아니면
                memberNames.append(members.get(i).getArtistName()).append(", ");
            else
                memberNames.append(members.get(i).getArtistName());

            if (members.get(i).getGender().equals("MALE"))
                genderCount[0]++;
            else
                genderCount[1]++;
        }
        binding.tvMembers.setText(memberNames.toString());

        if (group.getGroupType().equals("SOLO"))
            binding.llMembers.setVisibility(View.GONE);

        // 그룹 유형
        StringBuilder groupType = new StringBuilder();
        if (genderCount[0] > 0 && genderCount[1] > 0)
            groupType.append("혼성 ");
        else if (genderCount[0] > 0)
            groupType.append("남성 ");
        else
            groupType.append("여성 ");

        if (group.getGroupType().equals("SOLO"))
            groupType.append("솔로");
        else
            groupType.append("그룹");
        binding.tvGroupType.setText(groupType.toString());

        // 소속사
        if (group.getAgency().isBlank()) {
            binding.llAgency.setVisibility(View.GONE);
        }
        else {
            binding.tvAgency.setText(group.getAgency());
        }

        // 테마 (노래 특징)
        if (music.getThemes().size() == 0) {
            binding.llTheme.setVisibility(View.GONE);
        }
        else {
            for (String theme : music.getThemes()) {
                getLayoutInflater().inflate(R.layout.textview_theme, binding.fbTheme);
                TextView tvTheme = (TextView) binding.fbTheme.getChildAt(binding.fbTheme.getChildCount() - 1);
                tvTheme.setText(theme);
            }
        }

        // 가사
        if (music.getLyrics() != null && !music.getLyrics().isBlank()) {
            binding.tvLyrics.setText(music.getLyrics());
        }
    }
}