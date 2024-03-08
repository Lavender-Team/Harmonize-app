package kr.ac.chungbuk.harmonize.ui.music;

import android.os.Bundle;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import kr.ac.chungbuk.harmonize.utility.adapter.LatestMusicListAdapter;

public class MetadataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetadataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetadataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetadataFragment newInstance(String param1, String param2) {
        MetadataFragment fragment = new MetadataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    RecyclerView relatedMusicListView;
    LinearLayoutManager relatedMusicLayoutManager;
    LatestMusicListAdapter relatedMusicAdapter;

    private ObservableArrayList<SimpleMusic> relatedMusics = new ObservableArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_metadata, container, false);


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
}