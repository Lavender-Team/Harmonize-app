package kr.ac.chungbuk.harmonize.ui.search;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import kr.ac.chungbuk.harmonize.databinding.FragmentSearchBinding;
import kr.ac.chungbuk.harmonize.uicomponent.FilterDialog;
import kr.ac.chungbuk.harmonize.uicomponent.FragmentOne;
import kr.ac.chungbuk.harmonize.uicomponent.FragmentTwo;
import kr.ac.chungbuk.harmonize.utility.adapter.OnItemClickListener;
import kr.ac.chungbuk.harmonize.utility.adapter.OnItemRemoveListener;
import kr.ac.chungbuk.harmonize.utility.adapter.SearchHistoryAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.TabFragmentAdapter;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private SearchHistoryAdapter historyAdapter;
    private FragmentSearchBinding binding;

    private int indicatorWidth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setFragment(this);

        /* 검색 항목 TabLayout */
        TabFragmentAdapter tabAdapter = new TabFragmentAdapter(getActivity().getSupportFragmentManager());
        tabAdapter.addFragment(FragmentOne.newInstance(), "전체");
        tabAdapter.addFragment(FragmentTwo.newInstance(), "노래");
        tabAdapter.addFragment(FragmentOne.newInstance(), "가수");
        tabAdapter.addFragment(FragmentTwo.newInstance(), "노래방 번호");
        binding.searchResultViewPager.setAdapter(tabAdapter);
        binding.typeTabLayout.setupWithViewPager(binding.searchResultViewPager);

        // Determine tab indicator width at runtime
        binding.typeTabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = binding.typeTabLayout.getWidth() / binding.typeTabLayout.getTabCount();

                // Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                binding.indicator.setLayoutParams(indicatorParams);
            }
        });

        binding.searchResultViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)binding.indicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+position) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                binding.indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) { }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });



        /* 검색 기록 ListView */
        final RecyclerView historyListView = binding.historyListView;
        historyAdapter = new SearchHistoryAdapter(searchViewModel.getHistorys(), new OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                searchViewModel.setKeyword(item);
            }
        }, new OnItemRemoveListener() {
            @Override
            public void onItemRemove(String item) {
                historyAdapter.notifyDataSetChanged();
            }
        });
        historyListView.setAdapter(historyAdapter);

        searchViewModel.getKeyword().observe(getViewLifecycleOwner(), binding.etSearch::setText);


        /* 검색 EditText */
        binding.tilSearch.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        binding.etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    search();
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        binding.searchResultViewPager.setCurrentItem(0);
        super.onResume();
    }

    public void search() {
        binding.historyListView.setVisibility(View.INVISIBLE);
    }

    public void focusSearchBox() {
        binding.historyListView.setVisibility(View.VISIBLE);
    }

    public void showFilter() {
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.show(getActivity().getSupportFragmentManager(), "filter");

        //searchViewModel.addHistory("검색어");
        //historyAdapter.notifyDataSetChanged();
    }
}