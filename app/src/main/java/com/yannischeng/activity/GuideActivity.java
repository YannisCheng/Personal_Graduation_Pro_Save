package com.yannischeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yannischeng.R;
import com.yannischeng.fragment_guide.FragmentGuide1;
import com.yannischeng.fragment_guide.FragmentGuide2;
import com.yannischeng.fragment_guide.FragmentGuide3;
import com.yannischeng.fragment_guide.GuideViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YannisCheng
 */
public class GuideActivity extends AppCompatActivity {
    private List<Fragment> fragmentList;
    private GuideViewPager guideViewPager;
    private ViewPager v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        fragmentList = new ArrayList<Fragment>();
        v = (ViewPager) findViewById(R.id.view_guide);

        fragmentList.add(new FragmentGuide1());
        fragmentList.add(new FragmentGuide2());
        fragmentList.add(new FragmentGuide3());

        guideViewPager = new GuideViewPager(getSupportFragmentManager(), fragmentList);
        v.setAdapter(guideViewPager);
        v.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (2 == position) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(GuideActivity.this, SplashActivity.class));
                            finish();
                        }
                    }, 3000);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
