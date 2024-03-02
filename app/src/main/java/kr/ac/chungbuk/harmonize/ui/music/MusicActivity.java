package kr.ac.chungbuk.harmonize.ui.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import kr.ac.chungbuk.harmonize.databinding.ActivityMusicBinding;
import kr.ac.chungbuk.harmonize.utility.adapter.TabFragmentAdapter;

public class MusicActivity extends AppCompatActivity {

    private ActivityMusicBinding binding;

    private int indicatorWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /* 음악 정보 TabLayout */
        TabFragmentAdapter tabAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(MetadataFragment.newInstance("param1", "param2"), "노래 정보");
        tabAdapter.addFragment(PitchdataFragment.newInstance("param1", "param2"), "음역대");
        tabAdapter.addFragment(SingFragment.newInstance("param1", "param2"), "따라 부르기");
        binding.detailViewPager.setAdapter(tabAdapter);
        binding.typeTabLayout.setupWithViewPager(binding.detailViewPager);

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

        binding.detailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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







    }
}