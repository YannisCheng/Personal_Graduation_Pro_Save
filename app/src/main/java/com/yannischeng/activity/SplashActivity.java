package com.yannischeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.yannischeng.R;

import static com.yannischeng.application.MyApplication.FIRSTUSE;
import static com.yannischeng.application.MyApplication.editor;
import static com.yannischeng.application.MyApplication.preferences;

/**
 *
 * 闪屏界面
 *
 * Created by 程文佳
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirst = true;
        isFirst = preferences.getBoolean(FIRSTUSE, false);
        if (!isFirst) {
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            editor.putBoolean(FIRSTUSE, true);
            editor.commit();
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);

    }


}
