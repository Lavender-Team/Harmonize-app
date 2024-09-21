package kr.ac.chungbuk.harmonize.ui.music;

import static kr.ac.chungbuk.harmonize.config.AppContext.getAppContext;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.flexbox.FlexboxLayout;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.util.StringUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.databinding.FragmentMetadataBinding;
import kr.ac.chungbuk.harmonize.dto.ArtistDto;
import kr.ac.chungbuk.harmonize.dto.GroupDto;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import kr.ac.chungbuk.harmonize.utility.adapter.LatestMusicListAdapter;

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

    RecyclerView relatedMusicListView;
    LinearLayoutManager relatedMusicLayoutManager;
    LatestMusicListAdapter relatedMusicAdapter;

    private ObservableArrayList<SimpleMusic> relatedMusics = new ObservableArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMetadataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        relatedMusicListView = root.findViewById(R.id.relatedMusicListView);

        /* 이 노래와 비슷한 곡들 */
        relatedMusicLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        relatedMusicAdapter = new LatestMusicListAdapter(relatedMusics);
        relatedMusicListView.setLayoutManager(relatedMusicLayoutManager);
        relatedMusicListView.setAdapter(relatedMusicAdapter);


        relatedMusics.clear();
        relatedMusics.add(new SimpleMusic("사랑할 수 밖에", "볼빨간 사춘기"));
        relatedMusics.add(new SimpleMusic("화이트(White)", "폴킴"));
        relatedMusics.add(new SimpleMusic("주저하는 연인들을 위해", "잔나비"));
        relatedMusics.add(new SimpleMusic("사랑할 수 밖에", "볼빨간 사춘기"));
        relatedMusics.add(new SimpleMusic("화이트(White)", "폴킴"));
        relatedMusicAdapter.notifyDataSetChanged();

        return root;
    }

    public void setData(MusicDto music) {
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
    }
}