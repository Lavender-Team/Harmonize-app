package kr.ac.chungbuk.harmonize.uicomponent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.entity.SimpleMusic;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;

public class FragmentOne extends Fragment {

    RecyclerView musicListView;
    LinearLayoutManager linearLayoutManager;
    MusicListAdapter adapter;
    ArrayList<SimpleMusic> items = new ArrayList<>();

    public static FragmentOne newInstance() {
        return new FragmentOne();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        musicListView = view.findViewById(R.id.musicListView);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new MusicListAdapter(items);
        musicListView.setLayoutManager(linearLayoutManager);
        musicListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        items.add(new SimpleMusic("사건의 지평선", "윤하(YOUNHA)"));
        items.add(new SimpleMusic("좋니", "윤종신"));
        items.add(new SimpleMusic("기억의 습작", "김동률"));
        adapter.notifyDataSetChanged();
    }
}
