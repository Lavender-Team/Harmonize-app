package kr.ac.chungbuk.harmonize.ui.home;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kr.ac.chungbuk.harmonize.entity.SimpleMusic;

public class HomeViewModel extends ViewModel {

    private ObservableArrayList<SimpleMusic> homeRecommendMusics = new ObservableArrayList<>();
    private ObservableArrayList<SimpleMusic> artists = new ObservableArrayList<>();
    private ObservableArrayList<SimpleMusic> genreMusics = new ObservableArrayList<>();

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        homeRecommendMusics.add(new SimpleMusic("사건의 지평선", "윤하(YOUNHA)"));
        homeRecommendMusics.add(new SimpleMusic("좋니", "윤종신"));
        homeRecommendMusics.add(new SimpleMusic("기억의 습작", "김동률"));
        homeRecommendMusics.add(new SimpleMusic("좋니", "윤종신"));

        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));
        artists.add(new SimpleMusic("+13", "이무진"));

        genreMusics.add(new SimpleMusic("헤어지자말해요", "박재정"));
        genreMusics.add(new SimpleMusic("비의랩소디", "임재현"));
        genreMusics.add(new SimpleMusic("가질수없는너", "뱅크"));

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public ObservableArrayList<SimpleMusic> getHomeRecommendMusics() {
        return homeRecommendMusics;
    }

    public ObservableArrayList<SimpleMusic> getArtists() {
        return artists;
    }

    public ObservableArrayList<SimpleMusic> getGenreMusics() {
        return genreMusics;
    }

    public LiveData<String> getText() {
        return mText;
    }
}