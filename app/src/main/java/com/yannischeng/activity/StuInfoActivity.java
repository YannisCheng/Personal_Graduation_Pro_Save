package com.yannischeng.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.adapter.ListViewAdapterTalk;
import com.yannischeng.application.MyApplication;
import com.yannischeng.bean.CustomeInfo;
import com.yannischeng.bean.OfficialStudentInfo;
import com.yannischeng.bean.Talking;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.RoundImageView;
import com.yannischeng.util.UtilTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StuInfoActivity extends AppCompatActivity {

    private final String TAG = "--StuInfoActivity--";
    private UtilTools utilTools = null;
    private Intent intent = null;
    private String getSend = "";
    private InputStream in = null;
    private Bitmap bp = null;
    private OfficialStudentInfo studentInfo;
    private ImageView picFilePathGet;
    private TextView nameGet, sexGet, nationGet, dateGet, addressGet, idStuGet, hightGet, weightGet, actonTitle, classTV, showNum, showNUll, basic;
    private Button backBtn, editBtn, addBtn, sendBtn, cancelBtn, giveUpBtn;
    private RoundImageView userHead;
    private ListView showListView;
    private ListViewAdapterTalk adapterTalk = null;
    private List<Talking> list = null;
    private Handler handler;
    private LinearLayout stuInfoLayout, secondinfo;
    private boolean isOpen = false;
    private boolean isOpen2 = false;
    private EditText editText;
    private RelativeLayout layout;
    private String getStr = "";
    private TextView addressNow, jobNow, connectNow, otherNow, showNetError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_info);
        studentInfo = (OfficialStudentInfo) getIntent().getSerializableExtra("obj");
        new UtilTools().useLog(TAG, studentInfo.toString());
        initView();
    }

    private void initView() {
        utilTools = new UtilTools();
        getSend = getIntent().getStringExtra("sendTAG");
        utilTools.useLog(TAG, "getSend is : " + getSend);

        /*获得组件*/
        editBtn = (Button) findViewById(R.id.edit_btn);
        editBtn.setVisibility(View.GONE);
        addBtn = (Button) findViewById(R.id.quickly_search);
        addBtn.setVisibility(View.GONE);
        userHead = (RoundImageView) findViewById(R.id.back_action_bar_user_head);
        userHead.setVisibility(View.GONE);
        backBtn = (Button) findViewById(R.id.back_action_bar);
        backBtn.setVisibility(View.VISIBLE);
        picFilePathGet = (ImageView) findViewById(R.id.det_pic);
        nameGet = (TextView) findViewById(R.id.det_name);
        sexGet = (TextView) findViewById(R.id.det_sex);
        nationGet = (TextView) findViewById(R.id.det_nation);
        dateGet = (TextView) findViewById(R.id.det_date);
        addressGet = (TextView) findViewById(R.id.det_address);
        idStuGet = (TextView) findViewById(R.id.det_id);
        hightGet = (TextView) findViewById(R.id.det_height);
        weightGet = (TextView) findViewById(R.id.det_weight);
        actonTitle = (TextView) findViewById(R.id.action_title);
        classTV = (TextView) findViewById(R.id.set_class);
        actonTitle.setText(studentInfo.getNameStu() + " 档案");
        showListView = (ListView) findViewById(R.id.other_student_talking);
        stuInfoLayout = (LinearLayout) findViewById(R.id.info_student);
        showNUll = (TextView) findViewById(R.id.show_null);
        showNum = (TextView) findViewById(R.id.show_number);
        basic = (TextView) findViewById(R.id.basic_info);

        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        sendBtn = (Button) findViewById(R.id.send_btn);
        editText = (EditText) findViewById(R.id.send_msg);
        editText.setHint("@" + studentInfo.getNameStu() + "： ");
        giveUpBtn = (Button) findViewById(R.id.give_up_btn);
        layout = (RelativeLayout) findViewById(R.id.layout_send);
        secondinfo = (LinearLayout) findViewById(R.id.show_basic_info_layout);

        addressNow = (TextView) findViewById(R.id.address_now);
        jobNow = (TextView) findViewById(R.id.job_now);
        connectNow = (TextView) findViewById(R.id.connect_now);
        otherNow = (TextView) findViewById(R.id.other_now);
        showNetError = (TextView) findViewById(R.id.show_net_error_stu_info);


        initViewData();
        initViewDataTwo();
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

    private void initViewDataTwo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" +
                        MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutCustomerServlet?tag=query&stu_id=" + studentInfo.getIdOfficialStu();
                HttpRequestClass requestClass = new HttpRequestClass(url, StuInfoActivity.this, "POST");
                int tag = requestClass.setConn();

                Message message = Message.obtain();
                message.what = 31;
                //连接正常，继续请求
                message.obj = requestClass.openThreadCustomer();
                handler.sendMessage(message);


            }
        }).start();
    }


    private void initViewData() {
        list = new ArrayList<Talking>();
        try {
            in = getAssets().open(studentInfo.getIdOfficialStu() + ".jpg");
            bp = BitmapFactory.decodeStream(in);
            picFilePathGet.setImageBitmap(bp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        nameGet.setText(studentInfo.getNameStu() == null ? "" : studentInfo.getNameStu());
        nationGet.setText(studentInfo.getNationStu() == null ? "" : studentInfo.getNationStu());
        dateGet.setText(studentInfo.getDateStu() == null ? "" : studentInfo.getDateStu().substring(0, 4) + "-" + studentInfo.getDateStu().substring(4, 6));
        addressGet.setText(studentInfo.getAddressStu() == null ? "" : studentInfo.getAddressStu());
        idStuGet.setText(studentInfo.getIdOfficialStu() == null ? "" : studentInfo.getIdOfficialStu());
        hightGet.setText(studentInfo.getHeightStu() == null ? "" : studentInfo.getHeightStu());
        weightGet.setText(studentInfo.getWeightStu() == null ? "" : studentInfo.getWeightStu());
        classTV.setText(String.valueOf(studentInfo.getClassId()) == null ? "" : "行政 " + String.valueOf(studentInfo.getClassId()) + " 班");

        if (studentInfo.getSexStu().equals("男")) {
            sexGet.setBackgroundResource(R.mipmap.man);
        } else if (studentInfo.getSexStu().equals("女")) {
            sexGet.setBackgroundResource(R.mipmap.woman);
        }
        doHttp();
        doHandler();

    }

    private void doHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        //留言适配
                        List<Talking> list1 = new ArrayList<Talking>();
                        list1 = (List<Talking>) msg.obj;
                        showNum.setText("(" + list1.size() + "条）");
                        if (list1.size() == 0) {
                            showNUll.setVisibility(View.VISIBLE);
                            showListView.setVisibility(View.GONE);
                            showNetError.setVisibility(View.GONE);
                        } else if (list1.size() != 0) {
                            showNUll.setVisibility(View.GONE);
                            showNetError.setVisibility(View.GONE);
                            showListView.setVisibility(View.VISIBLE);
                            adapterTalk = new ListViewAdapterTalk(list1, StuInfoActivity.this);
                            adapterTalk.notifyDataSetChanged();
                            adapterTalk.notifyDataSetChanged();
                            showListView.setAdapter(adapterTalk);
                        }

                        break;
                    case 22:
                        String[] args = (String[]) msg.obj;
                        if (args[0].equals("2")) {
                            new UtilTools().useToast(StuInfoActivity.this, args[1]);
                        } else if (args[0].equals("1")) {
                            secondinfo.setVisibility(View.VISIBLE);
                            doHttp();
                            editText.setText("");
                            editText.setHint("@" + studentInfo.getNameStu() + "： ");
                            new UtilTools().useToast(StuInfoActivity.this, args[1]);
                            giveUpBtn.setVisibility(View.INVISIBLE);
                            sendBtn.setVisibility(View.GONE);
                            layout.setVisibility(View.GONE);
                            isOpen2 = false;
                        }
                        break;
                    case 21:
                        new UtilTools().useToast(StuInfoActivity.this, "服务器连接异常！发送失败");
                        break;
                    case 31:
                        //请求成功
                        if (msg.obj == null) {

                        } else {
                            CustomeInfo customeInfo = (com.yannischeng.bean.CustomeInfo) msg.obj;
                            addressNow.setText(customeInfo.getAddressNow() == null ? "未填写" : customeInfo.getAddressNow());
                            addressNow.setTextColor(getResources().getColor(R.color.black_text_87));
                            jobNow.setText(customeInfo.getJobNow() == null ? "未填写" : customeInfo.getJobNow());
                            jobNow.setTextColor(getResources().getColor(R.color.black_text_87));
                            connectNow.setText(customeInfo.getConnectNow() == null ? "未填写" : customeInfo.getConnectNow());
                            connectNow.setTextColor(getResources().getColor(R.color.black_text_87));
                            otherNow.setText(customeInfo.getOtherNow() == null ? "未填写" : customeInfo.getOtherNow());
                            otherNow.setTextColor(getResources().getColor(R.color.black_text_87));
                        }

                        break;
                    case 61:
                        showListView.setVisibility(View.GONE);
                        showNetError.setVisibility(View.VISIBLE);
                        showNUll.setVisibility(View.GONE);
                        new UtilTools().useToast(StuInfoActivity.this, "与服务器连接异常");
                        break;
                }
            }
        };
    }

    private void doHttp() {
        //查询当前学生的留言信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" +
                        MyApplication.LOCALHOST +
                        ":8080/FN_Pro_Server/AboutTalking?method=query&query_talk_id=" + studentInfo.getIdOfficialStu();

                HttpRequestClass requestClass = new HttpRequestClass(url, StuInfoActivity.this, "POST");
                int tag = requestClass.setConn();

                if (1 == tag) {
                    //服务器连接成功！
                    list = requestClass.openThreadTalking();
                    Message message3 = Message.obtain();
                    message3.what = 1;
                    message3.obj = list;
                    handler.sendMessage(message3);
                } else {
                    //服务器连接失败！
                    Message message22 = Message.obtain();
                    message22.what = 61;
                    handler.sendMessage(message22);

                }
            }
        }).start();
    }

    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.back_action_bar:
                back();
                break;
            case R.id.edit_btn_talk:

                if (isOpen2) {
                } else {
                    giveUpBtn.setVisibility(View.VISIBLE);
                    sendBtn.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.GONE);
                    isOpen2 = true;
                    secondinfo.setVisibility(View.GONE);

                }
                //处理数据
                dealData();
                doHandler();
                break;
            case R.id.basic_info:
                if (isOpen) {
                    stuInfoLayout.setVisibility(View.VISIBLE);
                    isOpen = false;
                } else {
                    stuInfoLayout.setVisibility(View.GONE);
                    isOpen = true;
                }
                break;


        }
    }

    private void dealData() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    cancelBtn.setVisibility(View.VISIBLE);
                } else if (s.toString().length() == 0) {
                    cancelBtn.setVisibility(View.GONE);
                }
                getStr = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //清除按钮
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStr = "";
                editText.setText("");
                editText.setHint("@" + studentInfo.getNameStu() + "： ");
            }
        });

        //放弃发送按钮
        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStr = "";
                editText.setText("");
                editText.setHint("@" + studentInfo.getNameStu() + "： ");
                giveUpBtn.setVisibility(View.INVISIBLE);
                sendBtn.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                isOpen2 = false;
                secondinfo.setVisibility(View.VISIBLE);
            }
        });

        //发送按钮
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(editText.getText().toString())) {
                    new UtilTools().useToast(StuInfoActivity.this, "内容不能为空！");
                } else {
                    //进行网络请求
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String stuID = studentInfo.getIdOfficialStu();
                            String vName = MyApplication.preferences.getString(MyApplication.NAME_OFFICIAL_STU, "");
                            String vClassID = MyApplication.preferences.getString(MyApplication.CLASS_OFFICIAL_STU, "");
                            String vID = MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_STU, "");
                            String VDate = new UtilTools().getCurrentTime();
                            String getContent = editText.getText().toString();
                            new UtilTools().useLog(TAG, "stuID " + stuID + " vName " + vName + " vClassID " + vClassID + " vID " + vID + " VDate " + VDate + " getContent " + getContent);

                            String str = "http://" +
                                    MyApplication.LOCALHOST +
                                    ":8080/FN_Pro_Server/AboutTalking?method=insert&insert_talk_id=" +
                                    stuID + "&insert_talk_vname=" +
                                    vName + "&insert_talk_vclass=" +
                                    vClassID + "&insert_talk_vid=" +
                                    vID + "&insert_talk_vdate=" +
                                    VDate + "&insert_talk_vcontent=" +
                                    getContent;

                            int tag = 0;
                            HttpRequestClass requestClass = new HttpRequestClass(str, StuInfoActivity.this, "POST");
                            tag = requestClass.setConn();

                            if (1 == tag) {
                                //服务器连接成功！
                                String[] strs = requestClass.openThreadHasParam(null);
                                Message message11 = Message.obtain();
                                message11.what = 22;
                                message11.obj = strs;
                                handler.sendMessage(message11);
                            } else {
                                //服务器连接失败！
                                Message message22 = Message.obtain();
                                message22.what = 21;
                                handler.sendMessage(message22);
                            }
                        }
                    }).start();
                    //back();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != intent) {
            intent = null;
        }
        if (in != null) {
            in = null;
        }
        if (bp != null) {
            bp = null;
        }
    }

    private void back() {
        if ("one".equals(getSend)) {
            startActivity(new Intent(StuInfoActivity.this, MainActivity.class));
            finish();
        } else {
            finish();
        }
    }
}
