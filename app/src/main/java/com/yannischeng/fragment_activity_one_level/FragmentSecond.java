package com.yannischeng.fragment_activity_one_level;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.activity.UserDetailActivity;
import com.yannischeng.adapter.ViewPagerManager;
import com.yannischeng.application.MyApplication;
import com.yannischeng.fragment_activity_two_level.FragmentSecondAdmin;
import com.yannischeng.fragment_activity_two_level.FragmentSecondAll;
import com.yannischeng.util.RoundImageView;
import com.yannischeng.util.UtilTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 同学录UI
 * <p>
 * Created by YannisCheng
 */
public class FragmentSecond extends Fragment {
    private static final String TAG = "--FragmentSecond--";
    private View rootView;
    private RadioGroup group;
    private TextView title;
    private Button addBtn, moreBtnAdmin, moreBtnAll;
    private RoundImageView userHeadbtn;
    private RadioButton oneBtn, twoBtn;
    private List<Fragment> fragmentList = null;
    private ViewPager viewPager;
    private ViewPagerManager viewPagerManager;
    private UtilTools utilTools;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*rootView是否为空*/
        if (null == rootView) {
            /*初次运行进行初始化*/
            rootView = inflater.inflate(R.layout.fragment_second, null);
            initView();
            onClick();
        }

        ViewGroup viewGroup = (ViewGroup) rootView.getRootView();
        if (null != viewGroup) {
            viewGroup.removeView(rootView);
        }
        return rootView;
    }

    private void onClick() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.one_group:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.two_group:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        utilTools.useLog(TAG, "viewPager postion is: " + position);
                        if (group.getCheckedRadioButtonId() != R.id.one_group) {
                            group.check(R.id.one_group);
                        }
                        utilTools.useLog(TAG, "viewPager postion is: " + "行政班");
                        break;
                    case 1:
                        if (group.getCheckedRadioButtonId() != R.id.two_group) {
                            group.check(R.id.two_group);
                            utilTools.useLog(TAG, "viewPager postion is: " + "全年级");
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        userHeadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("sendTAG", "two");
                intent.putExtra("obj", MyApplication.studentInfo);
                startActivity(intent);
                getActivity().finish();
            }
        });

        moreBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        moreBtnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /*初始化控件*/
    private void initView() {

        moreBtnAdmin = (Button) rootView.findViewById(R.id.more_btn_admin);
        //moreBtnAdmin.setVisibility(View.VISIBLE);
        moreBtnAll = (Button) rootView.findViewById(R.id.more_btn_all);
        group = (RadioGroup) rootView.findViewById(R.id.radio_group);
        title = (TextView) rootView.findViewById(R.id.action_title);
        addBtn = (Button) rootView.findViewById(R.id.add_btn);
        addBtn.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        group.setVisibility(View.VISIBLE);
        oneBtn = (RadioButton) rootView.findViewById(R.id.one_group);
        oneBtn.setChecked(true);
        twoBtn = (RadioButton) rootView.findViewById(R.id.two_group);
        viewPager = (ViewPager) rootView.findViewById(R.id.fragment_two_view_page);
        userHeadbtn = (RoundImageView) rootView.findViewById(R.id.back_action_bar_user_head);
        //设置头像
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new FragmentSecondAdmin());
        fragmentList.add(new FragmentSecondAll());
        viewPagerManager = new ViewPagerManager(getActivity().getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerManager);

        utilTools = new UtilTools();

        String filePath = MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_STU, "") + ".jpg";
        InputStream in = null;
        try {
            in = getActivity().getAssets().open(filePath);
            Bitmap factory = BitmapFactory.decodeStream(in);
            userHeadbtn.setImageBitmap(factory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
