package com.yannischeng.fragment_guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yannischeng.R;


/**
 * Created by 程文佳 2016年11月14日20:41:24
 */
public class FragmentGuide3 extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.guide_three_layout, null);
        }
        ViewGroup viewGroup = (ViewGroup) rootView.getRootView();
        if (null != viewGroup) {
            viewGroup.removeView(rootView);
        }
        return rootView;
    }
}
