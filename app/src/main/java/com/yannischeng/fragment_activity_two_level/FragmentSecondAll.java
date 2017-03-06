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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.yannischeng.R;
import com.yannischeng.activity.MainActivity;
import com.yannischeng.activity.StuInfoActivity;
import com.yannischeng.adapter.GridViewAdapter;
import com.yannischeng.adapter.ListViewAdapter;
import com.yannischeng.application.MyApplication;
import com.yannischeng.bean.MulObjPlus;
import com.yannischeng.bean.OfficialStudentInfo;
import com.yannischeng.db.UseDBHelper;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.MyPopupWindow;
import com.yannischeng.util.SetWindowAlpha;
import com.yannischeng.util.UtilTools;

import java.util.ArrayList;
import java.util.List;

import static com.yannischeng.application.MyApplication.isInitDBOK;
import static com.yannischeng.application.MyApplication.preferences;
import static com.yannischeng.application.MyApplication.useDBHelper;


/**
 * 查看全年级学生信息
 * 添加好友
 * <p>
 * Created by YannisCheng on 2016/11/13.
 */

public class FragmentSecondAll extends Fragment {

    private View rootView;
    private ListViewAdapter adapter;
    private ListView showListView;
    private XRefreshView refreshView;
    private List<OfficialStudentInfo> lists = null;
    private int pageSize = 1;
    private int lastPos = 0;
    private MyPopupWindow window = null;
    private final String TAG = "--FragmentSecondAll--";
    private int positoinItem = 0;
    private TextView totle, totleM, totleW, page;
    private HttpRequestClass requestClass;
    private static int pageSize2 = 1;
    private Handler handler;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter = null;
    private String[] strs = null;
    private boolean isOpen = true;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout,topLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_second_level_all_class_layout, null);
            initView();
        }

        ViewGroup viewGroup = (ViewGroup) rootView.getRootView();
        if (null != viewGroup) {
            viewGroup.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        linearLayout = (LinearLayout) rootView.findViewById(R.id.show_net_error_all_class);
        topLayout = (LinearLayout) rootView.findViewById(R.id.top);
        useDBHelper = new UseDBHelper(getContext());
        showListView = (ListView) rootView.findViewById(R.id.all_class_list_view);
        refreshView = (XRefreshView) rootView.findViewById(R.id.custom_view);
        totle = (TextView) rootView.findViewById(R.id.totle_all);
        totleM = (TextView) rootView.findViewById(R.id.totle_all_m);
        totleW = (TextView) rootView.findViewById(R.id.totle_all_w);
        page = (TextView) rootView.findViewById(R.id.page_num);
        gridView = (GridView) rootView.findViewById(R.id.grid);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.grid_layout);

        strs = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};

        doGridView();

        /**
         * add in 2017-02-12
         *
         * 判断：数据是否本地化
         *      是：使用本地化数据源
         *      否：使用服务器数据源
         */
        if (1 == preferences.getInt(isInitDBOK, 0)) {
            Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
            queryLocalDB(pageSize);
        } else {
            Log.e(TAG, "initView: 数据未进行本地化，将会请求网络数据");
            doHttp(1);
        }

        handleMessage();
        listenerRefresh();
        itemClick();
    }

    private void queryLocalDB(int pageSize2) {
        List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
        studentInfoList = useDBHelper.queryStuInfo(null, null, null, null, pageSize2);
        //获取各项人数
        int[] ints = useDBHelper.getCounts(null, null, null, null);
        initDBtoList(studentInfoList, ints);
    }

    //使用查询到的本地数据初始化ListView控件
    private void initDBtoList(List<OfficialStudentInfo> studentInfos, int[] ints) {
        totle.setText("共计:" + ints[0] + "人");
        totleM.setText("男:" + ints[1] + "人");
        totleW.setText("女:" + ints[2] + "人");
        if (pageSize < 1) {
            pageSize = 1;
        }
        page.setText("第 " + pageSize + " 页");
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

    //初始化“网格布局”
    private void doGridView() {
        gridViewAdapter = new GridViewAdapter(strs, getActivity());
        gridView.setAdapter(gridViewAdapter);
    }

    private void itemClick() {

        //网格布局点击相应事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pageSize = (position + 1);
                if (pageSize < 1) {
                    pageSize = 1;
                }
                page.setText("第 " + pageSize + " 页");
                if (1 == preferences.getInt(isInitDBOK, 0)) {
                    Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                    queryLocalDB(pageSize);
                    relativeLayout.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    isOpen = true;
                } else {
                    Log.e(TAG, "initView: 数据未进行本地化，将会请求网络数据");
                    doHttp(pageSize);
                    relativeLayout.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    isOpen = true;
                }


            }
        });

        //页码点击事件相应
        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    refreshView.setVisibility(View.GONE);
                    isOpen = false;
                } else {
                    relativeLayout.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    isOpen = true;
                }
            }
        });

        //点击查看详情
        showListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StuInfoActivity.class);
                intent.putExtra("obj", lists.get(position));
                startActivity(intent);
            }
        });

        //长按添加好友
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
                                        preferences.getString(MyApplication.ID_OFFICIAL_STU, "") +
                                        "&friend_id=" +
                                        lists.get(positoinItem).getIdOfficialStu();


                                HttpRequestClass requestClass = new HttpRequestClass(url, getActivity(), "POST");
                                int tag = requestClass.setConn();
                                if (1 == tag) {
                                    //服务器连接成功！
                                    String[] strs = requestClass.openThreadHasParam(null);
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    message.obj = strs;
                                    handler.sendMessage(message);
                                } else {
                                    //服务器连接失败！
                                    Message message22 = Message.obtain();
                                    message22.what = 61;
                                    handler.sendMessage(message22);
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

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doHttp(1);
            }
        });
    }

    //UI数据更新
    private void handleMessage() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 11:
                        topLayout.setVisibility(View.VISIBLE);
                        refreshView.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        MulObjPlus mo = (MulObjPlus) msg.obj;
                        totle.setText("共计:" + mo.getManCount() + "人");
                        totleM.setText("男:" + mo.getManCAll() + "人");
                        totleW.setText("女:" + mo.getWomanCAll() + "人");
                        if (pageSize < 1) {
                            pageSize = 1;
                        }
                        page.setText("第 " + pageSize + " 页");
                        listToListView(msg);
                        break;
                    case 2:
                        if (((String[]) msg.obj)[0].equals("1")) {
                            Snackbar snackbar = Snackbar.make(showListView, ((String[]) msg.obj)[1].toString(), Snackbar.LENGTH_LONG).setAction("查看", new View.OnClickListener() {
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
                    case 61:
                        new UtilTools().useToast(getActivity(), "与服务器连接异常，无法添加好友！");

                        break;
                    case 44:
                        //处理超时
                        if (1 == preferences.getInt(isInitDBOK, 0)) {
                            Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                            queryLocalDB(pageSize);
                        } else {
                            Log.e(TAG, "initView: 数据未进行本地化");
                            topLayout.setVisibility(View.GONE);
                            refreshView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            refreshView.stopRefresh();
                        }
                        break;
                }

            }
        };
    }

    //初始化listView数据
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

    //刷新控件监听
    private void listenerRefresh() {
        refreshView.setPullLoadEnable(true);
        refreshView.setPullRefreshEnable(true);
        refreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                pageSize--;
                if (pageSize < 1) {

                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                        new UtilTools().useToast(getActivity(), "到顶了~");
                        Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                        queryLocalDB(1);
                        refreshView.stopRefresh();
                        pageSize = 1;
                    } else {
                        doHttp(1);
                        new UtilTools().useToast(getActivity(), "到顶了~");
                        refreshView.stopRefresh();
                        pageSize = 1;
                    }

                } else {
                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                        Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                        queryLocalDB(pageSize);
                    } else {
                        doHttp(pageSize);
                    }

                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                pageSize++;
                if (pageSize > 20) {
                    pageSize = 20;
                    new UtilTools().useToast(getActivity(), "没有更多数据了~");
                    refreshView.stopLoadMore();
                } else {
                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                        Log.e(TAG, "initView: 数据已经本地化，使用本地数据库");
                        queryLocalDB(pageSize);
                    } else {
                        doHttp(pageSize);
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

    //进行网络数据请求
    private void doHttp(int pSize) {
        pageSize2 = pSize;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" +
                        MyApplication.LOCALHOST +
                        ":8080/FN_Pro_Server/QueryStuInfoCorAServlet?class_id=&page_id=" +
                        String.valueOf(pageSize2);

                requestClass = new HttpRequestClass(url, getActivity(), "POST");

                if (1 == requestClass.setConn()) {
                    Message message2 = Message.obtain();
                    message2.what = 11;
                    message2.obj = requestClass.openThreadMOAdmin();
                    handler.sendMessage(message2);
                } else if (2 == requestClass.setConn()) {
                    Message message3 = Message.obtain();
                    message3.what = 44;
                    handler.sendMessage(message3);
                }

            }
        }).start();
    }
}
