package com.yannischeng.fragment_activity_two_level;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.yannischeng.R;
import com.yannischeng.activity.MainActivity;
import com.yannischeng.activity.StuInfoActivity;
import com.yannischeng.adapter.ListViewAdapter;
import com.yannischeng.application.MyApplication;
import com.yannischeng.bean.MulObjPlus;
import com.yannischeng.bean.OfficialStudentInfo;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.MyPopupWindow;
import com.yannischeng.util.SetWindowAlpha;
import com.yannischeng.util.UtilTools;

import java.util.ArrayList;
import java.util.List;

import static com.yannischeng.application.MyApplication.handler;
import static com.yannischeng.application.MyApplication.isInitDBOK;
import static com.yannischeng.application.MyApplication.preferences;
import static com.yannischeng.application.MyApplication.useDBHelper;

/**
 * 12个行政班的 学生信息填充 和 人数显示
 * <p>
 * Created by 程文佳 on 2016/11/13.
 */
public class FragmentSecondAdmin extends Fragment {

    private View rootView;
    private final String TAG = "--FragmentSecondAdmin--";
    private String classID;
    private ListViewAdapter adapter;
    private XRefreshView refreshView;
    private List<OfficialStudentInfo> lists = null;
    private static int pageSize2 = 1;
    private int lastPos = 0;
    private MyPopupWindow window = null;
    private int pageCount = 0;
    private int positoinItem = 0;
    private HttpRequestClass requestClass;

    //3个布局
    private LinearLayout showSingleClass, showAllClass;
    private ListView showListView;

    //各个控件
    private TextView showClass, showNumber, backBtn, showMNum, showWNum;

    //12个班级布局
    private LinearLayout oneClass, twoClass, threeClass, fourClass, fiveClass, sixClass, sevenClass, eightClass, nineClass, tenClass, elevenClass, twelveClass;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_second_level_admin_class_layout, null);
            initView();
        }

        ViewGroup viewGroup = (ViewGroup) rootView.getRootView();
        if (null != viewGroup) {
            viewGroup.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {

        showSingleClass = (LinearLayout) rootView.findViewById(R.id.single_class_show_layout);
        showListView = (ListView) rootView.findViewById(R.id.list_view_admin_class);
        showAllClass = (LinearLayout) rootView.findViewById(R.id.class_show_layout);

        showClass = (TextView) rootView.findViewById(R.id.show_class);
        showNumber = (TextView) rootView.findViewById(R.id.show_all_number);
        backBtn = (TextView) rootView.findViewById(R.id.show_class_back);

        oneClass = (LinearLayout) rootView.findViewById(R.id.one_class_layout);
        twoClass = (LinearLayout) rootView.findViewById(R.id.two_class_layout);
        threeClass = (LinearLayout) rootView.findViewById(R.id.three_class_layout);
        fourClass = (LinearLayout) rootView.findViewById(R.id.four_class_layout);
        fiveClass = (LinearLayout) rootView.findViewById(R.id.five_class_layout);
        sixClass = (LinearLayout) rootView.findViewById(R.id.six_class_layout);
        sevenClass = (LinearLayout) rootView.findViewById(R.id.seven_class_layout);
        eightClass = (LinearLayout) rootView.findViewById(R.id.eight_class_layout);
        nineClass = (LinearLayout) rootView.findViewById(R.id.nine_class_layout);
        tenClass = (LinearLayout) rootView.findViewById(R.id.ten_class_layout);
        elevenClass = (LinearLayout) rootView.findViewById(R.id.eleven_class_layout);
        twelveClass = (LinearLayout) rootView.findViewById(R.id.twelve_class_layout);

        showMNum = (TextView) rootView.findViewById(R.id.show_m_number);
        showWNum = (TextView) rootView.findViewById(R.id.show_w_number);

        refreshView = (XRefreshView) rootView.findViewById(R.id.custom_view_admin);

        onClickSingle();
        itemClick();
        handleMessage();
    }

    //更新UI界面
    private void handleMessage() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        MulObjPlus mo = (MulObjPlus) msg.obj;
                        pageCount = Integer.parseInt(mo.getWomanCount());
                        showNumber.setText("共:" + mo.getManCount() + "人");
                        showMNum.setText("男:" + mo.getManCAll());
                        showWNum.setText("女:" + mo.getWomanCAll());
                        listToListView(msg);
                        break;
                    case 2:
                        if (((String[]) msg.obj)[0].equals("1")) {
                            Snackbar snackbar = Snackbar.make(showSingleClass, ((String[]) msg.obj)[1].toString(), Snackbar.LENGTH_LONG).setAction("查看", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                }
                            });
                            snackbar.setActionTextColor(Color.WHITE);
                            snackbar.getView().setBackgroundResource(R.color.Blue_09c);
                            snackbar.show();
                        } else {
                            new UtilTools().useToast(getActivity(), ((String[]) msg.obj)[1]);
                        }
                        break;
                    case 4:
                        //处理超时
                        refreshView.stopRefresh();
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "与服务器连接异常，无法添加好友！", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
    }

    //将数据初始化到ListView控件
    private void listToListView(Message msg) {
        if (lists == null || lists.size() == 0) {
        } else {
            lists.clear();
        }

        lists = ((MulObjPlus) msg.obj).getList();
        lastPos = lastPos + lists.size();
        showListView.smoothScrollToPositionFromTop(lastPos, lastPos);
        refreshView.stopLoadMore();
        refreshView.stopRefresh();

        adapter = new ListViewAdapter(lists, getActivity());
        adapter.notifyDataSetChanged();
        showListView.setAdapter(adapter);
    }

    private void itemClick() {
        //点击查看学生详情
        showListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StuInfoActivity.class);
                intent.putExtra("obj", lists.get(position));
                startActivity(intent);
            }
        });

        //点击添加好友
        showListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positoinItem = position;
                window = new MyPopupWindow(getActivity(), LayoutInflater.from(getContext()).inflate(R.layout.tool_action_bar_layout, null), 810, 500);
                window.initPopup();
                SetWindowAlpha.setAlpha(0.6f, getActivity());
                window.setTitle("提示");
                window.setMessage("添加为好友？");
                window.onOKBtn("是的");
                window.onNOBtn("不");
                window.noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.destory();
                        SetWindowAlpha.setAlpha(1f, getActivity());
                    }
                });
                window.okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String url = "http://" +
                                        MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutFriendServletAdd?" +
                                        "user_current_id=" +
                                        MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_STU, "") +
                                        "&friend_id=" +
                                        lists.get(positoinItem).getIdOfficialStu();
                                //String param = "friend_id="+lists.get(position).getIdOfficialStu();
                                HttpRequestClass requestClass = new HttpRequestClass(url, getActivity(), "POST");
                                int tag = requestClass.setConn();
                                if (1 == tag) {
                                    String[] strs = requestClass.openThreadHasParam(null);
                                    Message message1 = Message.obtain();
                                    message1.what = 2;
                                    message1.obj = strs;
                                    handler.sendMessage(message1);
                                    new UtilTools().useLog(TAG, strs[0] + ", " + strs[1]);
                                } else {
                                    Message message2 = Message.obtain();
                                    message2.what = 5;
                                    handler.sendMessage(message2);
                                }
                            }
                        }).start();
                        window.destory();
                        SetWindowAlpha.setAlpha(1f, getActivity());
                    }
                });
                return true;
            }
        });
    }

    //刷新数据
    private void listenerRefresh() {
        refreshView.setPullLoadEnable(true);
        refreshView.setPullRefreshEnable(true);
        refreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                pageSize2--;
                if (pageSize2 < 1) {

                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                        new UtilTools().useToast(getActivity(), "到顶了~");
                        Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                        queryLocalDB(classID, 1);
                        refreshView.stopRefresh();
                        pageSize2 = 1;
                    } else {
                        doHttp(1);
                        new UtilTools().useToast(getActivity(), "到顶了~");
                        refreshView.stopRefresh();
                        pageSize2 = 1;
                    }
                } else {
                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                        Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                        queryLocalDB(classID, pageSize2);
                    } else {
                        doHttp(pageSize2);
                    }
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                pageSize2++;
                if (pageSize2 > pageCount) {
                    pageSize2 = pageCount;

                    //doHttp(pageCount);
                    new UtilTools().useToast(getActivity(), "没有更多数据了~");
                    new UtilTools().useLog(TAG, "pageSize is : " + pageSize2);
                    refreshView.stopLoadMore();
                } else {
                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                        Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                        queryLocalDB(classID, pageSize2);
                    } else {
                        doHttp(pageSize2);
                    }
                }
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }

    //显示12个班级的具体学生信息
    private void onClickSingle() {
        oneClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UtilTools().useLog(TAG, "行政 1 班");
                classID = "1";
                setClassStuInfo(classID);
            }
        });

        twoClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "2";
                setClassStuInfo(classID);
            }
        });

        threeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "3";
                setClassStuInfo(classID);
            }
        });

        fourClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "4";
                setClassStuInfo(classID);
            }
        });

        fiveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "5";
                setClassStuInfo(classID);
            }
        });

        sixClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "6";
                setClassStuInfo(classID);
            }
        });

        sevenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "7";
                setClassStuInfo(classID);
            }
        });

        eightClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "8";
                setClassStuInfo(classID);
            }
        });

        nineClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "9";
                setClassStuInfo(classID);
            }
        });

        tenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "10";
                setClassStuInfo(classID);
            }
        });

        elevenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "11";
                setClassStuInfo(classID);
            }
        });

        twelveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classID = "12";
                setClassStuInfo(classID);
            }
        });
        MyApplication.GETCLASSID = classID;
    }

    //开始设置12个班的学生信息
    private void setClassStuInfo(String classID) {
        /**
         * add in 2017-02-12
         *
         * 判断：数据是否本地化
         *      是：使用本地化数据源
         *      否：使用服务器数据源
         */
        setSingleClassInfo("行政 " + classID + " 班");
        if (1 == preferences.getInt(isInitDBOK, 0)) {
            Log.e(TAG, "setClassStuInfo: 数据已经本地化，使用本地数据库,班级：" + classID);
            queryLocalDB(classID, pageSize2);
        } else {
            Log.e(TAG, "setClassStuInfo: 数据未进行本地化，将会请求网络数据");
            doHttp(1);
        }
        listenerRefresh();
    }

    private void queryLocalDB(String classID, int pageSize2) {
        List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
        //获取学生信息
        studentInfoList = useDBHelper.queryStuInfo(null, null, classID, null, pageSize2);
        //获取各项人数
        int[] ints = useDBHelper.getCounts(classID, null, null, null);
        initDBtoList(studentInfoList, ints);
    }

    //使用查询到的本地数据初始化ListView控件
    private void initDBtoList(List<OfficialStudentInfo> studentInfos, int[] ints) {
        pageCount = 2;
        showNumber.setText("共:" + ints[0] + "人");
        showMNum.setText("男:" + ints[1]);
        showWNum.setText("女:" + ints[2]);

        if (lists == null || lists.size() == 0) {
        } else {
            lists.clear();
        }

        lists = studentInfos;
        lastPos = lastPos + lists.size();
        showListView.smoothScrollToPositionFromTop(lastPos, lastPos);
        refreshView.stopLoadMore();
        refreshView.stopRefresh();

        adapter = new ListViewAdapter(lists, getActivity());
        adapter.notifyDataSetChanged();
        showListView.setAdapter(adapter);
    }

    //设置单个班级：班级名称 + 返回选择界面按钮
    private void setSingleClassInfo(String className) {

        showSingleClass.setVisibility(View.VISIBLE);
        refreshView.setVisibility(View.VISIBLE);
        showClass.setText(className);

        //返回至选择班级界面
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllClass.setVisibility(View.VISIBLE);
                showSingleClass.setVisibility(View.GONE);
                refreshView.setVisibility(View.GONE);
                MyApplication.GETCLASSID = "";
                if (lists == null) {
                } else {
                    lists.clear();
                }
            }
        });
    }

    //执行获取学生信息和人数的网络请求
    private void doHttp(int pSize) {
        pageSize2 = pSize;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" +
                        MyApplication.LOCALHOST +
                        ":8080/FN_Pro_Server/QueryStuInfoCorAServlet?class_id=" +
                        classID + "&page_id=" + String.valueOf(pageSize2);

                requestClass = new HttpRequestClass(url, getActivity(), "POST");

                if (1 == requestClass.setConn()) {
                    Message message3 = Message.obtain();
                    message3.what = 1;
                    message3.obj = requestClass.openThreadMOAdmin();
                    MyApplication.handler.sendMessage(message3);
                } else if (2 == requestClass.setConn()) {
                    Message message4 = Message.obtain();
                    message4.what = 4;
                    MyApplication.handler.sendMessage(message4);
                }
            }
        }).start();
    }
}
