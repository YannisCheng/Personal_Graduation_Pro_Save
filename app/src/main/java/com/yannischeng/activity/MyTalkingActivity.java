package com.yannischeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.adapter.ListViewAdapterTalk;
import com.yannischeng.application.MyApplication;
import com.yannischeng.model.Talking;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.UtilTools;

import java.util.ArrayList;
import java.util.List;


/**
 * 同学留言
 *
 * Created by 程文佳
 */
public class MyTalkingActivity extends AppCompatActivity {

    private Button backBtn, editBtn;
    private ListView showListView;
    private ListViewAdapterTalk adapterTalk;
    private Handler handler;
    private List<Talking> talkingList;
    private TextView title, showNUll, showNum;
    private LinearLayout showData, showNetError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_talking);

        initView();
        doHttp();
        hoHandler();
        onclick();
    }

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
                        showNetError.setVisibility(View.GONE);
                        showData.setVisibility(View.VISIBLE);
                        List<Talking> list1 = new ArrayList<Talking>();
                        list1 = (List<Talking>) msg.obj;
                        showNum.setText("共计 (" + list1.size() + "条)");
                        if (list1.size() == 0) {
                            showNUll.setVisibility(View.VISIBLE);
                            showListView.setVisibility(View.GONE);
                        } else if (list1.size() != 0) {
                            showNUll.setVisibility(View.GONE);
                            showListView.setVisibility(View.VISIBLE);
                            adapterTalk = new ListViewAdapterTalk(list1, MyTalkingActivity.this);
                            adapterTalk.notifyDataSetChanged();
                            showListView.setAdapter(adapterTalk);
                        }
                        break;
                    case 21:
                        showNetError.setVisibility(View.VISIBLE);
                        showData.setVisibility(View.GONE);
                        new UtilTools().useToast(MyTalkingActivity.this, "与服务器连接异常");
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

                HttpRequestClass requestClass = new HttpRequestClass(url, MyTalkingActivity.this, "POST");
                int tag = requestClass.setConn();

                if (1 == tag) {
                    //服务器连接成功！
                    talkingList = requestClass.openThreadTalking();
                    Message message11 = Message.obtain();
                    message11.what = 11;
                    message11.obj = talkingList;
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

    private void initView() {
        showData = (LinearLayout) findViewById(R.id.show_data);
        showNetError = (LinearLayout) findViewById(R.id.show_net_error);
        showListView = (ListView) findViewById(R.id.show_talking);
        backBtn = (Button) findViewById(R.id.back_action_bar);
        editBtn = (Button) findViewById(R.id.edit_detail_btn);
        editBtn.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.action_title);
        showNUll = (TextView) findViewById(R.id.show_null);
        showNum = (TextView) findViewById(R.id.show_number_my);
        title.setText("同学留言");
        talkingList = new ArrayList<Talking>();
        listener();
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
