package kr.ac.chungbuk.harmonize.ui.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentPitchdataBinding;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;


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
    }

}