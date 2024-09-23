package kr.ac.chungbuk.harmonize.ui.search;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import kr.ac.chungbuk.harmonize.entity.SearchHistory;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> keyword = new MutableLiveData<>();
    private ObservableArrayList<String> historys = new ObservableArrayList<>();

    public SearchViewModel() {
        keyword.setValue("");
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

    public void setHistorys(List<SearchHistory> searchHistories) {
        historys.clear();
        for (var s : searchHistories) {
            historys.add(s.getKeyword());
        }
    }

}