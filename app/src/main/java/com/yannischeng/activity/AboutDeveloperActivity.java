package com.yannischeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.application.MyApplication;


public class AboutDeveloperActivity extends AppCompatActivity {
    private Button backBtn, editBtn;
    private TextView title;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        initView();
    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.back_action_bar);
        editBtn = (Button) findViewById(R.id.edit_detail_btn);
        editBtn.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.action_title);
        title.setText("关于开发者");
        webView = (WebView) findViewById(R.id.show_developer);
        progressBar = (ProgressBar) findViewById(R.id.show_progress);

        String url = "http://" +
                MyApplication.LOCALHOST +
                ":8080/FN_Pro_Server/developer.html";
        initWeb(url);

        listener();
    }

    private void initWeb(String url) {
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    // 网页数据尚未加载完成
                    progressBar.setProgress(newProgress);
                    progressBar.setVisibility(View.VISIBLE);
                } else if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void listener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("sendTAG", "MainToThread");
        startActivity(intent);
        finish();
    }
}
