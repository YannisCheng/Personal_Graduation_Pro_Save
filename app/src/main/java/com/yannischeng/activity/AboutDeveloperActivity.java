package com.yannischeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.application.MyApplication;
import com.yannischeng.bean.Talking;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.UtilTools;

import java.util.List;

import static com.yannischeng.application.MyApplication.handler;


public class AboutDeveloperActivity extends AppCompatActivity {

    private static final String TAG = "AboutDeveloperActivity";
    private Button backBtn, editBtn;
    private TextView title;
    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout showNetError;
    private String url;
    private List<Talking> talkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        initView();
        doHttp();
        hoHandler();
        onclick();
    }

    private void initView() {
        showNetError = (LinearLayout) findViewById(R.id.show_net_error_developer);
        backBtn = (Button) findViewById(R.id.back_action_bar);
        editBtn = (Button) findViewById(R.id.edit_detail_btn);
        editBtn.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.action_title);
        title.setText("关于开发者");
        webView = (WebView) findViewById(R.id.show_developer);
        progressBar = (ProgressBar) findViewById(R.id.show_progress);

        url = "http://" +
                MyApplication.LOCALHOST +
                ":8080/FN_Pro_Server/developer.html";

        listener();
    }

    //-----------------------------------------

    private void onclick() {
        showNetError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doHttp();
            }
        });
    }

    private void hoHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 11:
                        Log.e(TAG, "handleMessage: " + msg.what );
                        showNetError.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        initWeb(url);
                        break;
                    case 21:
                        Log.e(TAG, "handleMessage: " + msg.what );
                        showNetError.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                        new UtilTools().useToast(AboutDeveloperActivity.this, "与服务器连接异常");
                        break;
                }
            }
        };
    }

    private void doHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" +
                        MyApplication.LOCALHOST +
                        ":8080/FN_Pro_Server/AboutTalking?method=query&query_talk_id=" + MyApplication.studentInfo.getIdOfficialStu();

                HttpRequestClass requestClass = new HttpRequestClass(url, AboutDeveloperActivity.this, "POST");
                int tag = requestClass.setConn();

                if (1 == tag) {
                    //服务器连接成功！
                    talkingList = requestClass.openThreadTalking();
                    Message message11 = Message.obtain();
                    message11.what = 11;
                    handler.sendMessage(message11);
                } else {
                    //服务器连接失败！
                    Message message22 = Message.obtain();
                    message22.what = 21;
                    handler.sendMessage(message22);
                }
            }
        }).start();
    }

    //-----------------------------------------

    private void initWeb(String url) {
        webView.getSettings().setBuiltInZoomControls(false);
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
