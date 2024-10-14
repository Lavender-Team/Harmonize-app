package kr.ac.chungbuk.harmonize.ui.music;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.config.GsonDateSupport;
import kr.ac.chungbuk.harmonize.config.VolleySingleton;
import kr.ac.chungbuk.harmonize.dao.AuthDao;
import kr.ac.chungbuk.harmonize.databinding.ActivityMusicBinding;
import kr.ac.chungbuk.harmonize.dto.MusicDto;
import kr.ac.chungbuk.harmonize.utility.adapter.TabFragmentAdapter;

public class MusicActivity extends AppCompatActivity {

    private ActivityMusicBinding binding;

    MetadataFragment metadataFragment;
    PitchdataFragment pitchdataFragment;
    SingFragment singFragment;

    private int indicatorWidth;

    public MusicDto musicState;

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

        // 노래 정보 Fragment
        metadataFragment = MetadataFragment.newInstance();
        tabAdapter.addFragment(metadataFragment, "노래 정보");

        // 음역대 Fragment
        pitchdataFragment = PitchdataFragment.newInstance();
        tabAdapter.addFragment(pitchdataFragment, "음역대");

        // 따라 부르기 Fragment
        singFragment = SingFragment.newInstance();
        tabAdapter.addFragment(singFragment, "따라 부르기");

        binding.detailViewPager.setAdapter(tabAdapter);
        binding.detailViewPager.setOffscreenPageLimit(3);
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

        binding.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookmark();
            }
        });


        /* Intent로 부터 액티비티에 표시할 음악의 musicId 받기 */
        Intent intent = getIntent();
        long musicId = intent.getLongExtra("musicId", -1);
        if (musicId != -1) { // musicId가 정상적으로 전달되면 음악 상세정보 보기
            fetchMusic(musicId);
        }
    }

    private void fetchMusic(long musicId) {

        StringRequest fetchRequest = new StringRequest(
                Request.Method.GET,
                Domain.url("/api/music/" + musicId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // MusicDto 파싱
                        Gson gson = GsonDateSupport.getInstance();
                        MusicDto music = gson.fromJson(response, MusicDto.class);

                        MusicActivity.this.musicState = music;

                        binding.tvTitle.setText(music.getTitle());
                        binding.tvArtist.setText(music.getArtist());

                        // 좋아요 버튼 상태 설정
                        Drawable leftDrawable;
                        if (music.getIsBookmarked())
                            leftDrawable = AppCompatResources.getDrawable(MusicActivity.this, R.drawable.ic_favorite_purple_18dp);
                        else
                            leftDrawable = AppCompatResources.getDrawable(MusicActivity.this, R.drawable.ic_favorite_border_purple_18dp);
                        binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);


                        // 노래 정보 Fragment 설정
                        if (metadataFragment != null)
                            metadataFragment.setData(music);

                        // 음역대 Fragment 설정
                        if (pitchdataFragment != null)
                            pitchdataFragment.setData(music);

                        // 따라부르기 Fragment 설정
                        if (singFragment != null)
                            singFragment.setData(music);

                        // 앨범 커버 로드
                        Glide.with(getApplicationContext())
                                .load(Domain.url(music.getAlbumCover()))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .placeholder(new ColorDrawable(Color.parseColor("#F6F6F6")))
                                .into(binding.ivThumbnail);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "요청 전송 중 오류가 발생하였습니다.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", AuthDao.getToken());
                return params;
            }

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

        VolleySingleton.getInstance(this).addToRequestQueue(fetchRequest);
    }

    void toggleBookmark() {

        // 좋아요 버튼 상태 설정
        Drawable leftDrawable;
        if (musicState.getIsBookmarked())
            leftDrawable = AppCompatResources.getDrawable(MusicActivity.this, R.drawable.ic_favorite_border_purple_18dp);
        else
            leftDrawable = AppCompatResources.getDrawable(MusicActivity.this, R.drawable.ic_favorite_purple_18dp);

        binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        musicState.setIsBookmarked(!musicState.getIsBookmarked());


        StringRequest bookmarkRequest = new StringRequest(
                (musicState.getIsBookmarked()) ? Request.Method.POST : Request.Method.DELETE,
                Domain.url("/api/music/" + musicState.getId() + "/like"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "좋아요 처리 중 오류가 발생하였습니다.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", AuthDao.getToken());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(bookmarkRequest);
    }
}