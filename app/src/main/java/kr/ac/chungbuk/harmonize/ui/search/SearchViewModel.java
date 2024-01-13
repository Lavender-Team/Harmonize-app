package kr.ac.chungbuk.harmonize.ui.search;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> keyword;
    private ObservableArrayList<String> historys;

    public SearchViewModel() {
        keyword = new MutableLiveData<>();
        keyword.setValue("");

        historys = new ObservableArrayList<>();
        historys.add("아이유");
        historys.add("소주 한잔");
        historys.add("장범준");
        historys.add("신호등");
    }

    public LiveData<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword.setValue(keyword);
    }

    public ObservableArrayList<String> getHistorys() {
        return historys;
    }

    public void addHistory(String keyword) {
        historys.add(keyword);
    }
}