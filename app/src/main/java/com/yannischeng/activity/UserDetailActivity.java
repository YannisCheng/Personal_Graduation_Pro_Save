package com.yannischeng.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yannischeng.R;
import com.yannischeng.application.MyApplication;
import com.yannischeng.model.CustomeInfo;
import com.yannischeng.model.OfficialStudentInfo;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.MyPopupWindow;
import com.yannischeng.util.SetWindowAlpha;
import com.yannischeng.util.UtilTools;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用户详情
 *
 * Created by 程文佳
 */
@ContentView(R.layout.activity_user_detail)
public class UserDetailActivity extends AppCompatActivity {

    private final String TAG = "--UserDetailEditActivity--";
    private UtilTools utilTools = null;
    private Intent intent = null;
    private String getSend = "";
    private InputStream in = null;
    private Bitmap bp = null;
    private OfficialStudentInfo studentInfo;
    private ImageView picFilePathGet;
    private TextView nameGet, sexGet, nationGet, dateGet, addressGet, idStuGet, hightGet, weightGet, classTV, actoinTitle;
    private LinearLayout layout2_edit, layout_2;
    private TextView addressNow, jobNow, connectNow, otherNow;
    private EditText addressNowET, jobNowET, connectNowET, otherNowET;
    private Button editBtn, cancelBtn, saveBtn;
    private MyPopupWindow window = null;
    private boolean isOpen = false;
    private int idTAGTB = 0;
    private int idTAGTEB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        studentInfo = (OfficialStudentInfo) getIntent().getSerializableExtra("obj");
        initView();

    }

    private void initView() {
        utilTools = new UtilTools();
        getSend = getIntent().getStringExtra("sendTAG");
        utilTools.useLog(TAG, "getSend is : " + getSend);

        /*获得组件*/
        picFilePathGet = (ImageView) findViewById(R.id.det_pic);
        nameGet = (TextView) findViewById(R.id.det_name);
        sexGet = (TextView) findViewById(R.id.det_sex);
        nationGet = (TextView) findViewById(R.id.det_nation);
        dateGet = (TextView) findViewById(R.id.det_date);
        addressGet = (TextView) findViewById(R.id.det_address);
        idStuGet = (TextView) findViewById(R.id.det_id);
        hightGet = (TextView) findViewById(R.id.det_height);
        weightGet = (TextView) findViewById(R.id.det_weight);
        classTV = (TextView) findViewById(R.id.set_class);
        layout2_edit = (LinearLayout) findViewById(R.id.info_student_2_edit);
        layout_2 = (LinearLayout) findViewById(R.id.info_student_2);

        /*第二部分控件声明*/
        addressNow = (TextView) findViewById(R.id.address_now);
        jobNow = (TextView) findViewById(R.id.job_now);
        connectNow = (TextView) findViewById(R.id.connect_now);
        otherNow = (TextView) findViewById(R.id.other_now);
        addressNowET = (EditText) findViewById(R.id.address_now_et);
        jobNowET = (EditText) findViewById(R.id.job_now_et);
        connectNowET = (EditText) findViewById(R.id.connect_now_et);
        otherNowET = (EditText) findViewById(R.id.other_now_et);
        editBtn = (Button) findViewById(R.id.edit_detail_btn);
        cancelBtn = (Button) findViewById(R.id.cancel__detail_btn);
        saveBtn = (Button) findViewById(R.id.detail_save);

        actoinTitle = (TextView) findViewById(R.id.action_title);

        idTAGTB = R.id.back_action_bar;
        idTAGTEB = R.id.cancel__detail_btn;

        initViewData();
        editClickListener();
        getData();
    }

    //显示第一部分数据匹配
    private void initViewData() {
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

        setDataForLayout2();
    }

    //显示第二部分数据匹配
    private void setDataForLayout2() {
        addressNow.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_ADDRESS, "未填写"));
        addressNow.setTextColor(getResources().getColor(R.color.black_text_87));
        jobNow.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_JOB, "未填写"));
        jobNow.setTextColor(getResources().getColor(R.color.black_text_87));
        connectNow.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_CONNECT, "未填写"));
        connectNow.setTextColor(getResources().getColor(R.color.black_text_87));
        otherNow.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_OTHER, "未填写"));
        otherNow.setTextColor(getResources().getColor(R.color.black_text_87));
    }

    private Handler handlerl;

    private void editClickListener() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(addressNowET.getText().toString()) && "".equals(jobNowET.getText().toString()) && "".equals(connectNowET.getText().toString()) && "".equals(otherNowET.getText().toString())) {
                    new UtilTools().useToast(UserDetailActivity.this, "修改内容不能全部为空");
                } else {
                    //2、执行网络请求
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendRequest();
                        }
                    }).start();
                    setDataForLayout2();
                    //1、放弃修改 或 成功修改 控件自动消失
                    setVorG();
                    handlerl = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 1:
                                    isOpen = false;
                                    if ("1".equals(((String[]) msg.obj)[0])) {
                                        new UtilTools().useToast(UserDetailActivity.this, "修改成功！");
                                        getData();
                                    } else {
                                        new UtilTools().useToast(UserDetailActivity.this, "修改失败！");
                                    }
                                    actoinTitle.setText("个人详情");
                                    break;
                                case 2:
                                    isOpen = false;
                                    actoinTitle.setText("个人详情");
                                    new UtilTools().useToast(UserDetailActivity.this, "与服务器连接异常,修改失败！");
                                    break;
                            }
                        }
                    };

                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usePopup();
            }
        });
    }

    //发送求情
    private void sendRequest() {
        String requestUrl = "http://" +
                MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutCustomerServlet?tag=insert&stu_id=" +
                studentInfo.getIdOfficialStu() + "&address_now=" +
                addressNowET.getText().toString() + "&job_now=" +
                jobNowET.getText().toString() + "&connect_now=" +
                connectNowET.getText().toString() + "&other_now=" +
                otherNowET.getText().toString();
        HttpRequestClass requestClass = new HttpRequestClass(requestUrl, UserDetailActivity.this, "POST");
        int tag = requestClass.setConn();
        if (1 == tag) {
            //与服务器连接正常
            Message message1 = Message.obtain();
            message1.what = 1;
            //继续请求
            message1.obj = requestClass.openThreadHasParam(null);
            handlerl.sendMessage(message1);
        } else if (2 == tag) {
            //与服务器连接异常
            Message message2 = Message.obtain();
            message2.what = 2;
            handlerl.sendMessage(message2);
        }
    }

    //查询自定义信息
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" + MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutCustomerServlet?tag=query&stu_id=" + studentInfo.getIdOfficialStu();
                HttpRequestClass requestClass = new HttpRequestClass(url, UserDetailActivity.this, "POST");
                int tag = requestClass.setConn();
                if (tag == 1) {
                    CustomeInfo customeInfo = requestClass.openThreadCustomer();
                    Message messageC = Message.obtain();
                    messageC.what = 6;
                    messageC.obj = customeInfo;
                    handlerl.sendMessage(messageC);
                } else {
                    Message messageC2 = Message.obtain();
                    messageC2.what = 62;
                    handlerl.sendMessage(messageC2);
                }

            }
        }).start();

        handlerl = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 6:
                        //初次登录后打开此界面
                        if (msg.obj == null) {
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_ADDRESS, "未填写");
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_JOB, "未填写");
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_CONNECT, "未填写");
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_OTHER, "未填写");
                            MyApplication.editor.commit();
                        } else {
                            CustomeInfo customeInfo = (CustomeInfo) msg.obj;
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_ADDRESS, customeInfo.getAddressNow() == null ? "未填写" : customeInfo.getAddressNow());
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_JOB, customeInfo.getJobNow() == null ? "未填写" : customeInfo.getJobNow());
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_CONNECT, customeInfo.getConnectNow() == null ? "未填写" : customeInfo.getConnectNow());
                            MyApplication.editor.putString(MyApplication.CUSTOMER_STU_OTHER, customeInfo.getOtherNow() == null ? "未填写" : customeInfo.getOtherNow());
                            MyApplication.editor.commit();
                        }

                        setDataForLayout2();
                        break;
                    case 62:
                        Toast.makeText(UserDetailActivity.this, "    与服务器连接异常！\n无法获取最新自定义数据", Toast.LENGTH_SHORT).show();
                        setDataForLayout2();
                        break;
                }
            }
        };
    }

    private void setVorG() {
        layout2_edit.setVisibility(View.GONE);
        layout_2.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.GONE);
    }

    //edit时，空间的显隐性变化
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.edit_detail_btn:
                actoinTitle.setText("编辑详情");
                isOpen = true;
                layout_2.setVisibility(View.GONE);
                layout2_edit.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                addressNowET.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_ADDRESS, "未填写"));
                jobNowET.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_JOB, "未填写"));
                connectNowET.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_CONNECT, "未填写"));
                otherNowET.setText(MyApplication.preferences.getString(MyApplication.CUSTOMER_STU_OTHER, "未填写"));
                break;
            case R.id.back_action_bar:
                back();
                break;
        }
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
        if (isOpen) {
            usePopup();
        } else {
            if ("three".equals(getSend)) {
                intent = new Intent(UserDetailActivity.this, MainActivity.class);
                intent.putExtra("sendTAG", "MainToThread");
                startActivity(intent);
                finish();
            } else if ("one".equals(getSend)) {
                startActivity(new Intent(UserDetailActivity.this, MainActivity.class));
                finish();
            } else if ("two".equals(getSend)) {
                intent = new Intent(UserDetailActivity.this, MainActivity.class);
                intent.putExtra("sendTAG", "MainToSec");
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        }

    }

    private void usePopup() {
        window = new MyPopupWindow(this, LayoutInflater.from(this).inflate(R.layout.tool_action_bar_user_detail_layout, null), 810, 500);
        window.initPopup();
        SetWindowAlpha.setAlpha(0.6f, UserDetailActivity.this);
        window.setTitle("提示");
        window.setMessage("确定放弃修改?");
        window.onNOBtn("不", R.color.black_secondary_text_54);
        window.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actoinTitle.setText("个人详情");
                isOpen = false;
                SetWindowAlpha.setAlpha(1f, UserDetailActivity.this);
                layout2_edit.setVisibility(View.GONE);
                layout_2.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.GONE);
                addressNowET.setText("");
                jobNowET.setText("");
                connectNowET.setText("");
                otherNowET.setText("");
                window.destory();
                if (null == window) {
                } else {
                    window = null;
                    System.out.println("window is null");
                }
            }
        });
        window.onOKBtn("是", R.color.Orange_f60);
        window.noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetWindowAlpha.setAlpha(1f, UserDetailActivity.this);
                window.destory();
                if (null == window) {
                } else {
                    window = null;
                    System.out.println("window is null");
                }
            }
        });
    }

}
