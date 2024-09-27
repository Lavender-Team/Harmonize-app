package kr.ac.chungbuk.harmonize.ui.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.chungbuk.harmonize.databinding.FragmentBookmarkBinding;
import kr.ac.chungbuk.harmonize.dto.MusicListDto;
import kr.ac.chungbuk.harmonize.ui.music.MusicActivity;
import kr.ac.chungbuk.harmonize.utility.adapter.MusicListAdapter;

public class BookmarkFragment extends Fragment {

    private FragmentBookmarkBinding binding;

    BookmarkViewModel bookmarkViewModel;

    LinearLayoutManager linearLayoutManager;
    MusicListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookmarkViewModel = new ViewModelProvider(this).get(BookmarkViewModel.class);

        binding = FragmentBookmarkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new MusicListAdapter(
                bookmarkViewModel.musicListState,
                getActivity(),
                new MusicListAdapter.OnListItemSelectedInterface() {
                    @Override
                    public void onItemSelected(View v, long musicId) {
                        Intent intent = new Intent(getActivity(), MusicActivity.class);
                        intent.putExtra("musicId", musicId);
                        startActivity(intent);
                    }
                });
        binding.musicListView.setLayoutManager(linearLayoutManager);
        binding.musicListView.setAdapter(adapter);

        // 무한 스크롤
        binding.musicListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 현재 RecyclerView의 레이아웃 매니저 가져오기
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                // 아이템 개수와 마지막에 보이는 아이템의 포지션
                try {
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    // 끝에 도달했는지 체크
                    if (totalItemCount <= lastVisibleItemPosition + 3) { // +3은 여유값
                        bookmarkViewModel.fetchBookmarkedMusic(new BookmarkViewModel.OnMusicLoaded() {
                            @Override
                            public void addMusics(List<MusicListDto> musics) {
                                adapter.addItems(musics);
                            }
                        });
                    }
                } catch (Exception ignored) { }

            }
        });


        /* 테스트 코드 */
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bookmarkViewModel.addSampleMusic();
//                adapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        bookmarkViewModel.fetchBookmarkedMusic(new BookmarkViewModel.OnMusicLoaded() {
            @Override
            public void addMusics(List<MusicListDto> musics) {
                adapter.addItems(musics);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.clearItems();
        bookmarkViewModel.clearMusicState();
        bookmarkViewModel.fetchBookmarkedMusic(new BookmarkViewModel.OnMusicLoaded() {
            @Override
            public void addMusics(List<MusicListDto> musics) {
                adapter.addItems(musics);
            }
        });
    }
}