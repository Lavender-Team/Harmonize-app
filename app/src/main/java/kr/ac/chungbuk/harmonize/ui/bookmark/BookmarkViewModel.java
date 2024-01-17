package kr.ac.chungbuk.harmonize.ui.bookmark;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kr.ac.chungbuk.harmonize.entity.SimpleMusic;

public class BookmarkViewModel extends ViewModel {

    private ObservableArrayList<SimpleMusic> bookmarkedMusics = new ObservableArrayList<>();

    public BookmarkViewModel() {
        bookmarkedMusics.add(new SimpleMusic("사건의 지평선", "윤하(YOUNHA)"));
        bookmarkedMusics.add(new SimpleMusic("좋니", "윤종신"));
        bookmarkedMusics.add(new SimpleMusic("기억의 습작", "김동률"));
    }

    public ObservableArrayList<SimpleMusic> getBookmarkedMusics() {
        return bookmarkedMusics;
    }

    public void addSampleMusic() {
        bookmarkedMusics.add(new SimpleMusic("테스트", "테스트 가수"));
    }
}