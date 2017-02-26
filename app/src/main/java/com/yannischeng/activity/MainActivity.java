package com.yannischeng.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.yannischeng.R;
import com.yannischeng.adapter.ViewPagerManager;
import com.yannischeng.application.MyApplication;
import com.yannischeng.bean.OfficialStudentInfo;
import com.yannischeng.fragment_activity_one_level.FragmentMain;
import com.yannischeng.fragment_activity_one_level.FragmentSecond;
import com.yannischeng.fragment_activity_one_level.FragmentThread;
import com.yannischeng.util.UtilTools;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.yannischeng.application.MyApplication.studentInfo;


//代替 setContentView(R.layout.activity_main);
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private final String TAG = "--MainActivity--";
    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private ViewPagerManager viewPagerManager;
    private UtilTools utilTools;
    private String getSendTAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //使用xUtils3框架的注解
        x.view().inject(this);
        initView();
        //接收fragment的返回信息，返回指定的fragment。
        if (null == getIntent().getStringExtra("sendTAG")) {
        } else {
            getSendTAG = getIntent().getStringExtra("sendTAG");
            utilTools.useLog(TAG, "接收返回信息: " + getSendTAG);
            switch (getSendTAG) {
                case "MainToSec":
                    viewPager.setCurrentItem(1);
                    if (radioGroup.getCheckedRadioButtonId() != R.id.two) {
                        radioGroup.check(R.id.two);
                    }
                    break;
                case "MainToThread":
                    viewPager.setCurrentItem(2);
                    if (radioGroup.getCheckedRadioButtonId() != R.id.three) {
                        radioGroup.check(R.id.three);
                    }
                    break;
            }
        }
    }

    /**
     * 初始化view中的所有控件
     */
    private void initView() {
        utilTools = new UtilTools();
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        viewPager = (ViewPager) findViewById(R.id.view_pager_act);

        /* 设置RadioGroup中各个Button的响应 -- 改变ViewPager中的view*/
        radioGroup.setOnCheckedChangeListener(this);

        /* 设置ViewPager中各个Pager的响应 -- 改变RadioButton中的被选中的Button*/
        doChangerPagerListener();

        /* 实现ViewPager Adapter*/
        doViewPagerAdapterFragment();
        doStudentInfo();

    }

    private void doStudentInfo() {
        studentInfo = new OfficialStudentInfo(
                MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_STU, ""),
                MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_CARD_STU, ""),
                MyApplication.preferences.getString(MyApplication.NAME_OFFICIAL_STU, ""),
                MyApplication.preferences.getString(MyApplication.NATION_OFFICIAL_STU, ""),
                MyApplication.preferences.getString(MyApplication.SEX_OFFICIAL_STU, ""),
                MyApplication.preferences.getString(MyApplication.DATE_OFFICIAL_STU, ""),
                MyApplication.preferences.getString(MyApplication.HEIGHT_OFFICIAL_STU, ""),
                MyApplication.preferences.getString(MyApplication.WEIGHT_OFFICIAL_STU, ""),
                MyApplication.preferences.getString(MyApplication.ADDRESS_OFFICIAL_STU, ""),

                Integer.parseInt(MyApplication.preferences.getString(MyApplication.ID_CLASS_OFFICIAL_STU, "")),
                Integer.parseInt(MyApplication.preferences.getString(MyApplication.ID_ALL_OFFICIAL_STU, "")),
                Integer.parseInt(MyApplication.preferences.getString(MyApplication.CLASS_OFFICIAL_STU, "")));
    }

    private void doViewPagerAdapterFragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new FragmentMain());
        fragmentList.add(new FragmentSecond());
        fragmentList.add(new FragmentThread());

        viewPagerManager = new ViewPagerManager(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerManager);
    }

    /**
     * 为RadioGroup设置响应
     *
     * @param group     radioGroup控件
     * @param checkedId 控件中各个button的Id
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.one:
                utilTools.useLog(TAG, "radioGroup id is:" + 0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.two:
                viewPager.setCurrentItem(1);
                break;
            case R.id.three:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    /**
     * viewPager设置监听器，改变radioGroup中的选中button
     */
    private void doChangerPagerListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        utilTools.useLog(TAG, "viewPager postion is: " + position);
                        if (radioGroup.getCheckedRadioButtonId() != R.id.one) {
                            radioGroup.check(R.id.one);
                        }
                        break;
                    case 1:
                        if (radioGroup.getCheckedRadioButtonId() != R.id.two) {
                            radioGroup.check(R.id.two);
                        }
                        break;
                    case 2:
                        if (radioGroup.getCheckedRadioButtonId() != R.id.three) {
                            radioGroup.check(R.id.three);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private long startTime = 0;

    @Override
    public void onBackPressed() {
        long endTime = System.currentTimeMillis();
        if ((endTime - startTime) < 2000) {
            finish();
            System.exit(0);
        } else {
            startTime = endTime;
            new UtilTools().useToast(MainActivity.this, "再按一次退出程序！");
        }
    }
}
