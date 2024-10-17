package kr.ac.chungbuk.harmonize.ui.music;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentPitchdataBinding;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;


public class PitchdataFragment extends Fragment {

    FragmentPitchdataBinding binding;

    LinearLayoutManager similarMusicLayoutManager;
    MusicListAdapter similarMusicAdapter;

    private ObservableArrayList<MusicListDto> similarMusics = new ObservableArrayList<>();

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

        /* 이 노래와 비슷한 곡들 */
        similarMusicLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        similarMusicAdapter = new MusicListAdapter(similarMusics,
                getActivity(),
                new MusicListAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
        binding.similarMusicListView.setLayoutManager(similarMusicLayoutManager);
        binding.similarMusicListView.setAdapter(similarMusicAdapter);

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

        if (music.getSimilarMusics() == null || music.getSimilarMusics().isEmpty()) {
            binding.llSimilarMusic.setVisibility(View.GONE);
        } else {
            similarMusics.addAll(music.getSimilarMusics());
            similarMusicAdapter.notifyDataSetChanged();
        }
    }

}