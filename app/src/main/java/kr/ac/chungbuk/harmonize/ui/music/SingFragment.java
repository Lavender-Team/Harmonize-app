package kr.ac.chungbuk.harmonize.ui.music;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentSingBinding;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.utility.adapter.RecentMusicListAdapter;

public class SingFragment extends Fragment {

    FragmentSingBinding binding;

    LinearLayoutManager relatedMusicLayoutManager;
    RecentMusicListAdapter relatedMusicAdapter;

    private ObservableArrayList<MusicListDto> relatedMusics = new ObservableArrayList<>();

    public SingFragment() {
        // Required empty public constructor
    }

    public static SingFragment newInstance() {
        SingFragment fragment = new SingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /* 이 노래와 비슷한 곡들 */
        relatedMusicLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        relatedMusicAdapter = new RecentMusicListAdapter(relatedMusics,
                new RecentMusicListAdapter.OnItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
        binding.relatedMusicListView.setLayoutManager(relatedMusicLayoutManager);
        binding.relatedMusicListView.setAdapter(relatedMusicAdapter);


        relatedMusics.clear();
        /*relatedMusics.add(new SimpleMusic("사랑할 수 밖에", "볼빨간 사춘기"));
        relatedMusics.add(new SimpleMusic("화이트(White)", "폴킴"));*/
        relatedMusicAdapter.notifyDataSetChanged();

        return root;
    }

    public void setData(MusicDto music) {

        if (music.getPlayLink() != null && !music.getPlayLink().isBlank()) {
            binding.btnListenOnYoutube.setOnClickListener((View v) -> {
                if (getActivity() != null)
                    openWebPage(music.getPlayLink(), getActivity());
            });
        } else {
            binding.llListenOnYoutube.setVisibility(View.GONE);
        }

    }


    private void openWebPage(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent); // 해당 URL을 열 수 있는 앱이 있을 때 실행
        } else {
            // URL을 열 수 있는 앱이 없을 경우 처리
            Toast.makeText(context, "No application can handle this request", Toast.LENGTH_LONG).show();
        }
    }
}