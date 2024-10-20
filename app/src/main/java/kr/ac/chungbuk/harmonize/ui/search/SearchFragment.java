package kr.ac.chungbuk.harmonize.ui.search;

import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.SearchHistoryDao;
import kr.ac.chungbuk.harmonize.databinding.FragmentSearchBinding;
import kr.ac.chungbuk.harmonize.dto.SearchResultDto;
import kr.ac.chungbuk.harmonize.entity.SearchHistory;
import kr.ac.chungbuk.harmonize.uicomponent.FilterDialog;
import kr.ac.chungbuk.harmonize.uicomponent.SearchResultFragment;
import kr.ac.chungbuk.harmonize.utility.adapter.OnItemClickListener;
import kr.ac.chungbuk.harmonize.utility.adapter.OnItemRemoveListener;
import kr.ac.chungbuk.harmonize.utility.adapter.SearchHistoryAdapter;
import kr.ac.chungbuk.harmonize.utility.adapter.TabFragmentAdapter;
import kr.ac.chungbuk.harmonize.utility.network.NetworkManager;

public class SearchFragment extends Fragment implements IFilterApply {

    NetworkManager networkManager;

    private SearchViewModel searchViewModel;
    private SearchHistoryAdapter historyAdapter;
    private TabFragmentAdapter tabAdapter;

    private FragmentSearchBinding binding;

    private int indicatorWidth;

    FilterValue filterState = new FilterValue();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        networkManager = NetworkManager.getInstance(getContext());

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setFragment(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        /* 검색 항목 TabLayout */
        tabAdapter = new TabFragmentAdapter(getChildFragmentManager());
        tabAdapter.addFragment(new SearchResultFragment(), "전체");
        tabAdapter.addFragment(new SearchResultFragment(), "노래");
        tabAdapter.addFragment(new SearchResultFragment(), "가수");
        tabAdapter.addFragment(new SearchResultFragment(), "노래방번호");
        binding.searchResultViewPager.setOffscreenPageLimit(4);
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
            public void onPageSelected(int position) {
                /* 노래방번호 선택시 숫자 키보드 표시 */
                if (position != 3) { // 3: 노래방 번호
                    binding.etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    binding.etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
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
                setEmptyTextVisibility();
            }
        });
        historyListView.setAdapter(historyAdapter);

        // 검색어 기록 불러오기
        loadSearchHistory();

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
        if (binding.etSearch.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "검색어를 입력하세요.", Toast.LENGTH_LONG).show();
            return;
        }

        // 검색 기록 저장
        SearchHistoryDao.save(
                new SearchHistory(binding.etSearch.getText().toString(), LocalDateTime.now())
        );

        String url = Domain.url(String.format("/api/music/search?query=%1$s",
                binding.etSearch.getText().toString()));
        if (!filterState.getGroupType().isEmpty())
            url += "&groupType=" + filterState.getGroupType();
        if (!filterState.getGenreValue().isEmpty())
            url += "&genre=" + filterState.getGenreValue();

        StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 검색 결과 파싱
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, SearchResultDto>>(){}.getType();
                        Map<String, SearchResultDto> results = gson.fromJson(response, type);

                        // 검색 결과 adapter 적용
                        getAllResultFragment().searchCompleted(Objects.requireNonNull(results.get("all")));
                        getMusicResultFragment().searchCompleted(Objects.requireNonNull(results.get("title")));
                        getArtistResultFragment().searchCompleted(Objects.requireNonNull(results.get("artist")));
                        getKaraokeNumResultFragment().searchCompleted(Objects.requireNonNull(results.get("karaokeNum")));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getAllResultFragment().searchFailed();
                        getMusicResultFragment().searchFailed();
                        getArtistResultFragment().searchFailed();
                        getKaraokeNumResultFragment().searchFailed();
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        getAllResultFragment().searchRequested(binding.etSearch.getText().toString());
        getMusicResultFragment().searchRequested(binding.etSearch.getText().toString());
        getArtistResultFragment().searchRequested(binding.etSearch.getText().toString());
        getKaraokeNumResultFragment().searchRequested(binding.etSearch.getText().toString());

        VolleySingleton.getInstance(getContext()).addToRequestQueue(searchRequest);

        binding.historyArea.setVisibility(View.INVISIBLE);
    }

    public void focusSearchBox() {
        binding.historyArea.setVisibility(View.VISIBLE);
        loadSearchHistory();
    }

    public void showFilter() {
        // 검색 필터 Dialog 띄우기
        FilterDialog filterDialog = new FilterDialog();

        // 전달할 이전 필터 설정 값
        Bundle prevFilterArgs = new Bundle();
        prevFilterArgs.putString("prevFilter", filterState.toString());
        filterDialog.setArguments(prevFilterArgs);

        filterDialog.show(getChildFragmentManager(), "filter");
    }

    @Override
    public void onApplyFilter(FilterValue filterValue) {
        // FilterDialog에서 적용한 필터 설정 값을 받아서 적용
        this.filterState = filterValue;
        if (filterState.isDefaultState()) {
            // 필터 아이콘을 필터 기본값 여부에 따라 바꾸기
            binding.btnFilter.clearColorFilter();
            binding.btnFilterCheck.setVisibility(View.GONE);
        } else {
            binding.btnFilter.setColorFilter(R.color.black);
            binding.btnFilterCheck.setVisibility(View.VISIBLE);
        }
    }

    private SearchResultFragment getAllResultFragment() {
        return (SearchResultFragment) findTabFragmentByPosition(0);
    }

    private SearchResultFragment getMusicResultFragment() {
        return (SearchResultFragment) findTabFragmentByPosition(1);
    }

    private SearchResultFragment getArtistResultFragment() {
        return (SearchResultFragment) findTabFragmentByPosition(2);
    }

    private SearchResultFragment getKaraokeNumResultFragment() {
        return (SearchResultFragment) findTabFragmentByPosition(3);
    }

    private Fragment findTabFragmentByPosition(int position) {
        return getChildFragmentManager().findFragmentByTag("android:switcher:" +
                binding.searchResultViewPager.getId() + ":" + tabAdapter.getItemId(position));
    }

    private void loadSearchHistory() {
        List<SearchHistory> searchHistories = SearchHistoryDao.findAll(10);
        searchViewModel.setHistorys(searchHistories);
        historyAdapter.notifyDataSetChanged();
        setEmptyTextVisibility();
    }

    private void setEmptyTextVisibility() {
        binding.emptyText.setVisibility(searchViewModel.getHistorys().size() > 0 ? View.GONE : View.VISIBLE);
    }
}