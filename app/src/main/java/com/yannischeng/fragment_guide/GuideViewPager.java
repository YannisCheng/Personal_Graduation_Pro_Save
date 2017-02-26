package com.yannischeng.fragment_guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 程文佳 2016年11月14日20:40:24
 */
public class GuideViewPager extends FragmentPagerAdapter {

    private List<Fragment> list;

    public GuideViewPager(FragmentManager fm) {
        super(fm);
    }

    public GuideViewPager(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

}