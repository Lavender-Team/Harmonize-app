package kr.ac.chungbuk.harmonize.ui.music;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.FragmentPitchdataBinding;
import kr.ac.chungbuk.harmonize.dto.AuthDto;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;


public class PitchdataFragment extends Fragment {

    FragmentPitchdataBinding binding;

    public PitchdataFragment() {
        // Required empty public constructor
    }

    public static PitchdataFragment newInstance() {
        PitchdataFragment fragment = new PitchdataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPitchdataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void setData(MusicDto music) {
        if (music == null || binding == null)
            return;

        if (music.getHighestPitch() != null)
            binding.tvHighestPitch.setText(PitchConverter.freqToPitch(music.getHighestPitch()));

        if (music.getLowestPitch() != null)
            binding.tvLowestPitch.setText(PitchConverter.freqToPitch(music.getLowestPitch()));

        if (music.getHighPitchRatio() != null)
            binding.tvHighPitchRatio.setText((double) Math.round(music.getHighPitchRatio() * 1000) / 10 + "%");

        if (music.getLowPitchRatio() != null)
            binding.tvLowPitchRatio.setText((double) Math.round(music.getLowPitchRatio() * 1000) / 10 + "%");

        // 내 음역대 커버 비율
        if (music.getPitchStat() != null) {
            try {
                AuthDto authDto = AuthDao.find();
                if (authDto.getLowestPitch() != null && authDto.getHighestPitch() != null) {
                    Double cover = music.getPitchStat().getCoverPercentage(
                            PitchConverter.freqToPitch(authDto.getLowestPitch()),
                            PitchConverter.freqToPitch(authDto.getHighestPitch())
                    ) * 10000;

                    cover = Math.round(cover) / (double) 100;

                    binding.tvCoverPercentage.setText(cover + "%");
                }
                else
                    throw new Exception("cannot get pitch range");
            } catch (Exception e) {
                binding.tvCoverPercentage.setText("-");
            }
        }

        if (music.getPitchStat() != null) {
            AuthDto authDto;
            try {
                authDto = AuthDao.find();

                binding.pitchStat.updateChart(
                        music.getPitchStat(),
                        PitchConverter.freqToPitch(authDto.getLowestPitch()),
                        PitchConverter.freqToPitch(authDto.getHighestPitch())
                );
            }
            catch (Exception ignored) {
                // 로그인 정보 없을시
                binding.pitchStat.updateChart(music.getPitchStat(), "C2", "D6");
            }
        }
    }

}