package com.yannischeng.fragment_activity_one_level;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yannischeng.R;
import com.yannischeng.activity.AboutDeveloperActivity;
import com.yannischeng.activity.LoginActivity;
import com.yannischeng.activity.MyTalkingActivity;
import com.yannischeng.activity.UserDetailActivity;
import com.yannischeng.application.MyApplication;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.RoundImageView;
import com.yannischeng.util.UtilTools;

import java.io.IOException;
import java.io.InputStream;

import static com.yannischeng.application.MyApplication.CUSTOMER_STU_ADDRESS;
import static com.yannischeng.application.MyApplication.CUSTOMER_STU_CONNECT;
import static com.yannischeng.application.MyApplication.CUSTOMER_STU_JOB;
import static com.yannischeng.application.MyApplication.CUSTOMER_STU_OTHER;
import static com.yannischeng.application.MyApplication.isInitDBOK;
import static com.yannischeng.application.MyApplication.preferences;
import static com.yannischeng.application.MyApplication.studentInfo;
import static com.yannischeng.application.MyApplication.useDBHelper;

/**
 * 我
 * Created by YannisCheng
 */

public class FragmentThread extends Fragment {

    private static final String TAG = "FragmentThread";
    private LinearLayout itemLayout, initDBLayout, talkLayout, exitLayout, developerLayout, aboutMeLayout;
    private RelativeLayout sendMSGLayout;
    private View rootView;
    private RadioGroup group;
    private TextView title, name, sex, category, classID, talking;
    private Button addBtn, moreBtn, sendBtn, cancelBtn, giveUpBtn;
    private RoundImageView userHead;
    private ImageView userHeadSingle, dbImg;
    private EditText editText;
    private boolean isOpen = false;
    private String getStr = "";
    private Handler handler;
    private int initOK = 0;
    private HttpRequestClass requestClass = null;
    private TextView initText;
    private ProgressDialog dialog = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*rootView是否为空*/
        if (null == rootView) {
            /*初次运行进行初始化*/
            rootView = inflater.inflate(R.layout.fragment_thread, null);
            initView();
            onClick();
            doHandler();
        }

        ViewGroup viewGroup = (ViewGroup) rootView.getRootView();
        if (null != viewGroup) {
            viewGroup.removeView(rootView);
        }
        return rootView;
    }

    /*初始化控件*/
    private void initView() {
        handler = new Handler();
        //bar
        group = (RadioGroup) rootView.findViewById(R.id.radio_group);
        title = (TextView) rootView.findViewById(R.id.action_title);
        addBtn = (Button) rootView.findViewById(R.id.add_btn);
        addBtn.setVisibility(View.GONE);
        title.setText("我");
        group.setVisibility(View.GONE);
        userHead = (RoundImageView) rootView.findViewById(R.id.back_action_bar_user_head);
        userHead.setVisibility(View.GONE);

        //---------各个layout--------
        itemLayout = (LinearLayout) rootView.findViewById(R.id.item_single_info_setting);
        initDBLayout = (LinearLayout) rootView.findViewById(R.id.my_teacher);
        talkLayout = (LinearLayout) rootView.findViewById(R.id.my_talking);
        exitLayout = (LinearLayout) rootView.findViewById(R.id.exit);
        developerLayout = (LinearLayout) rootView.findViewById(R.id.about_developer);

        //------student个人简略信息-----
        userHeadSingle = (ImageView) rootView.findViewById(R.id.item_user_head_setting);
        name = (TextView) rootView.findViewById(R.id.list_view_item_user_name_setting);
        sex = (TextView) rootView.findViewById(R.id.list_view_item_user_sex_setting);
        classID = (TextView) rootView.findViewById(R.id.list_view_item_user_class_admin_setting);
        category = (TextView) rootView.findViewById(R.id.list_view_item_user_category_setting);
        talking = (TextView) rootView.findViewById(R.id.list_view_item_user_talking_setting);

        //-------联系开发者----------
        giveUpBtn = (Button) rootView.findViewById(R.id.give_up_btn);
        sendMSGLayout = (RelativeLayout) rootView.findViewById(R.id.sender_send);
        sendBtn = (Button) rootView.findViewById(R.id.send_btn);
        editText = (EditText) rootView.findViewById(R.id.send_msg_to_sender);
        cancelBtn = (Button) rootView.findViewById(R.id.cancel_btn);
        editText.setHint("@程文佳： ");
        //---------aboutMe---------
        aboutMeLayout = (LinearLayout) rootView.findViewById(R.id.about_me);
        //-----------数据本地化---------------
        initText = (TextView) rootView.findViewById(R.id.set_initdb_text);
        dbImg = (ImageView) rootView.findViewById(R.id.db_img);
        if (1 == preferences.getInt(isInitDBOK, 0)) {
            //已经初始化
            initText.setText("清除本地数据");
            dbImg.setImageDrawable(getResources().getDrawable(R.mipmap.delete_db));
        } else {
            //未进行初始化
            initText.setText("数据本地化");
            dbImg.setImageDrawable(getResources().getDrawable(R.mipmap.cuoti));
        }

        //add in 2017-02-11
        initData();
    }

    private void initData() {
        try {
            InputStream in = getActivity().getAssets().open(MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_STU, "") + ".jpg");
            Bitmap bm = BitmapFactory.decodeStream(in);
            userHeadSingle.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText(MyApplication.preferences.getString(MyApplication.NAME_OFFICIAL_STU, ""));
        sex.setText(MyApplication.preferences.getString(MyApplication.SEX_OFFICIAL_STU, ""));
        classID.setText("行政 " + MyApplication.preferences.getString(MyApplication.CLASS_OFFICIAL_STU, "") + " 班");
        category.setText(MyApplication.preferences.getString(MyApplication.CATEGORY_OFFICIAL_STU, "未编辑"));
        talking.setText(MyApplication.preferences.getString(MyApplication.TALKING_OFFICIAL_STU, "未编辑"));
    }

    private void onClick() {
        //数据本地化
        initDBLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initOK = MyApplication.preferences.getInt(MyApplication.isInitDBOK, 0);
                if (0 == initOK) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.mipmap.cuoti).
                            setTitle("数据本地化").
                            setMessage("数据本地化后可以离线查询同学个人信息，但不能进行网络相关的处理").
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).
                            setPositiveButton("本地化", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    //初始化数据库
                                    dialog = new ProgressDialog(getContext());
                                    dialog.setIcon(R.mipmap.cuoti);
                                    dialog.setTitle("提示");
                                    dialog.setMessage("正在初始化中……");
                                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                    doData();
                                }

                            }).create();
                    builder.show();
                } else {
                    //添加dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.mipmap.delete_db).
                            setTitle("清除本地数据").
                            setMessage("清除本地数据后，查询同学信息将会通过网络，消耗流量。确定要清除本地数据？").
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).
                            setPositiveButton("清除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    //初始化数据库
                                    dialog = new ProgressDialog(getContext());
                                    dialog.setIcon(R.mipmap.delete_db);
                                    dialog.setTitle("提示");
                                    dialog.setMessage("正在清除中……");
                                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                    //删除本地数据库
                                    dropDB();
                                }

                            }).create();
                    builder.show();
                }
            }
        });
        //关于开发者
        aboutMeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutDeveloperActivity.class));
                getActivity().finish();
            }
        });

        //退出登录
        exitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.tuichu_one).
                        setTitle("退出登录").
                        setMessage("确定要退出登录？").
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).
                        setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                //清除已经本地化的数据
                                useDBHelper.clearHistoryData("ln");
                                useDBHelper.clearHistoryData("kw");
                                useDBHelper.clearHistoryData("name");
                                useDBHelper.deleteDB();
                                MyApplication.editor.putInt(isInitDBOK, 0);
                                //清除sp中的文件
                                MyApplication.editor.putString(CUSTOMER_STU_ADDRESS, "");
                                MyApplication.editor.putString(CUSTOMER_STU_JOB, "");
                                MyApplication.editor.putString(CUSTOMER_STU_CONNECT, "");
                                MyApplication.editor.putString(CUSTOMER_STU_OTHER, "");
                                //改变sp中的登录时的状态值
                                MyApplication.editor.putBoolean(MyApplication.isAutoLogin, false);
                                MyApplication.editor.commit();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }

                        }).create();
                builder.show();

            }
        });

        //当前用户详情
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("sendTAG", "three");
                intent.putExtra("obj", studentInfo);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //我的“同学留言”
        talkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyTalkingActivity.class));
                getActivity().finish();
            }
        });

        //用户反馈
        developerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //控件显示
                if (isOpen) {
                } else {
                    giveUpBtn.setVisibility(View.VISIBLE);
                    sendBtn.setVisibility(View.VISIBLE);
                    sendMSGLayout.setVisibility(View.VISIBLE);
                    isOpen = true;
                }
                //处理数据
                dealData();
                doHandler();
            }
        });
    }

    //清除 本地数据
    private void dropDB() {
        int tag = useDBHelper.deleteDB();
        if (tag != 0) {
            //dialog消失
            dialog.dismiss();
            Toast.makeText(getContext(), "清除成功！", Toast.LENGTH_SHORT).show();
            MyApplication.editor.putInt(MyApplication.isInitDBOK, 0);
            MyApplication.editor.commit();
            initText.setText("数据本地化");
            dbImg.setImageDrawable(getResources().getDrawable(R.mipmap.cuoti));
        } else if (tag == 0) {
            //dialog消失
            dialog.dismiss();
            Toast.makeText(getContext(), "清除失败！", Toast.LENGTH_SHORT).show();
            MyApplication.editor.putInt(MyApplication.isInitDBOK, 1);
            MyApplication.editor.commit();
            initText.setText("清除本地数据");
            dbImg.setImageDrawable(getResources().getDrawable(R.mipmap.delete_db));
        }
    }

    //初始化 本地数据
    private void doData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*初始化数据库*/
                String url = "http://" +
                        MyApplication.LOCALHOST +
                        ":8080/FN_Pro_Server/LoginServlet?method=initDB";

                requestClass = new HttpRequestClass(url, getActivity(), "POST");
                if (1 == requestClass.setConn()) {
                    //数据库初始化
                    requestClass.initDB();
                    Message message2 = Message.obtain();
                    message2.what = 1;
                    handler.sendMessage(message2);
                } else if (2 == requestClass.setConn()) {
                    Message message3 = Message.obtain();
                    message3.what = 2;
                    handler.sendMessage(message3);
                }
                //dialog消失
                dialog.dismiss();
            }
        }).start();
    }

    //UI更新
    private void doHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        MyApplication.editor.putInt(MyApplication.isInitDBOK, 1);
                        MyApplication.editor.commit();
                        Log.e(TAG, " 1 handleMessage: " + preferences.getInt(isInitDBOK, 0));
                        dbImg.setImageDrawable(getResources().getDrawable(R.mipmap.delete_db));
                        initText.setText("清除本地数据");
                        Toast.makeText(getActivity(), "数据初始化完毕", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        MyApplication.editor.putInt(MyApplication.isInitDBOK, 0);
                        MyApplication.editor.commit();
                        Log.e(TAG, " 2 handleMessage: " + preferences.getInt(isInitDBOK, 0));
                        initText.setText("数据本地化");
                        dbImg.setImageDrawable(getResources().getDrawable(R.mipmap.cuoti));
                        Toast.makeText(getActivity(), "网络状态异常,数据初始化失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 21:
                        new UtilTools().useToast(getActivity(), "服务器连接异常！发送失败");
                        break;
                    case 22:
                        String[] args = (String[]) msg.obj;
                        if (args[0].equals("2")) {
                            new UtilTools().useToast(getActivity(), args[1]);
                        } else if (args[0].equals("1")) {
                            editText.setText("");
                            editText.setHint("@程文佳： ");
                            new UtilTools().useToast(getActivity(), args[1]);
                            giveUpBtn.setVisibility(View.INVISIBLE);
                            sendBtn.setVisibility(View.INVISIBLE);
                            sendMSGLayout.setVisibility(View.GONE);
                            isOpen = false;
                        }
                        break;
                }
            }
        };
    }

    //用户反馈执行过程
    private void dealData() {
        //EditText监听
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

        //取消按钮
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStr = "";
                editText.setText("");
                editText.setHint("@程文佳： ");
            }
        });

        //放弃反馈按钮
        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStr = "";
                editText.setText("");
                editText.setHint("@程文佳： ");
                giveUpBtn.setVisibility(View.INVISIBLE);
                sendBtn.setVisibility(View.INVISIBLE);
                sendMSGLayout.setVisibility(View.GONE);
                isOpen = false;
            }
        });

        //发送用户反馈按钮
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getStr.equals("")) {
                    new UtilTools().useToast(getActivity(), "内容不能为空！");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://" +
                                    MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutSenderServlet?insert_sender_name=" +
                                    MyApplication.studentInfo.getNameStu() + "&insert_sender_content=" +
                                    getStr + "&insert_sender_classID=" +
                                    MyApplication.studentInfo.getClassId() + "&insert_sender_date=" +
                                    new UtilTools().getCurrentTime();
                            Log.e(TAG, "run: url is ： " + url);
                            int tag = 0;
                            HttpRequestClass requestClass = new HttpRequestClass(url, getActivity(), "POST");
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
                }
            }
        });

    }

}
