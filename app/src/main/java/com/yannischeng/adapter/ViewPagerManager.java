package com.yannischeng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by YannisCheng
 */
public class ViewPagerManager extends FragmentPagerAdapter {

    /**
     * 声明一个list对象，用来接收FragmentList对象集合
     */
    private final String TAG = "--ViewPagerManager--";
    private List<Fragment> list;

    public ViewPagerManager(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerManager(FragmentManager fm, List<Fragment> list) {
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
