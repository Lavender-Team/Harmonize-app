package kr.ac.chungbuk.harmonize.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.chungbuk.harmonize.databinding.FragmentBookmarkBinding;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;

public class BookmarkFragment extends Fragment {

    private FragmentBookmarkBinding binding;

    LinearLayoutManager linearLayoutManager;
    MusicListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BookmarkViewModel bookmarkViewModel =
                new ViewModelProvider(this).get(BookmarkViewModel.class);

        binding = FragmentBookmarkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new MusicListAdapter(bookmarkViewModel.getBookmarkedMusics());
        binding.musicListView.setLayoutManager(linearLayoutManager);
        binding.musicListView.setAdapter(adapter);


        /* 테스트 코드 */
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkViewModel.addSampleMusic();
                adapter.notifyDataSetChanged();
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}