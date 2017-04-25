package com.yannischeng.fragment_activity_one_level;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.yannischeng.R;
import com.yannischeng.activity.SearchActivity;
import com.yannischeng.activity.StuInfoActivity;
import com.yannischeng.activity.UserDetailActivity;
import com.yannischeng.adapter.ListViewAdapter;
import com.yannischeng.application.MyApplication;
import com.yannischeng.model.OfficialStudentInfo;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.MyPopupWindow;
import com.yannischeng.util.RoundImageView;
import com.yannischeng.util.SetWindowAlpha;
import com.yannischeng.util.UtilTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 关注
 * Created by 程文佳
 */
public class FragmentMain extends Fragment {

    private final String TAG = "--FragmentMain--";
    private View rootView;
    private RadioGroup group;
    private TextView title, showNULL;
    private Button addBtn;
    private RoundImageView userHeadbtn;
    private HttpRequestClass requestClass;
    private Handler handler = null;
    private ListView showListView;
    private ListViewAdapter adapter = null;
    private List<OfficialStudentInfo> list = null;
    private MyPopupWindow window = null;
    private XRefreshView refreshView;
    private int lastPosi = 0;
    private int positonItem = 0;
    public static String path;
    public static String fileName;
    private LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*rootView是否为空*/
        if (null == rootView) {
            /*初次运行进行初始化*/
            rootView = inflater.inflate(R.layout.fragment_main, null);
            initView();
            onClick();
            listenerRefresh();
            itemClick();
        }

        ViewGroup viewGroup = (ViewGroup) rootView.getRootView();
        if (null != viewGroup) {
            viewGroup.removeView(rootView);
        }
        return rootView;
    }

    private void onClick() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
                getActivity().finish();
            }
        });
        userHeadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("sendTAG", "one");
                intent.putExtra("obj", MyApplication.studentInfo);
                startActivity(intent);
                getActivity().finish();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doHttp();
            }
        });
    }

    /*初始化控件*/
    private void initView() {
        linearLayout = (LinearLayout) rootView.findViewById(R.id.show_net_error_main);
        group = (RadioGroup) rootView.findViewById(R.id.radio_group);
        title = (TextView) rootView.findViewById(R.id.action_title);
        addBtn = (Button) rootView.findViewById(R.id.add_btn);
        userHeadbtn = (RoundImageView) rootView.findViewById(R.id.back_action_bar_user_head);
        showListView = (ListView) rootView.findViewById(R.id.list_view_main_friend);
        refreshView = (XRefreshView) rootView.findViewById(R.id.custom_view_friend);
        addBtn.setVisibility(View.VISIBLE);
        group.setVisibility(View.GONE);
        showNULL = (TextView) rootView.findViewById(R.id.show_null);

        list = new ArrayList<OfficialStudentInfo>();
        path = getActivity().getApplicationContext().getFilesDir().getAbsolutePath();
        fileName = "/cache11.txt";
        //设置头像
        String filePath = MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_STU, "") + ".jpg";
        InputStream in = null;
        try {
            in = getActivity().getAssets().open(filePath);
            Bitmap factory = BitmapFactory.decodeStream(in);
            userHeadbtn.setImageBitmap(factory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        doHttp();
        doHandler();

    }

    private void listenerRefresh() {
        refreshView.setPullLoadEnable(false);
        refreshView.setPullRefreshEnable(true);
        //refreshView.
        refreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                list.clear();
                doHttp();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }

    private void doHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10:
                        refreshView.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        Log.i(TAG, "handleMessage: path is " + path);
                        list = (List<OfficialStudentInfo>) msg.obj;
                        listToListView(list);
                        break;
                    case 3:
                        new UtilTools().useToast(getActivity(), (String) msg.obj);
                        list.clear();
                        doHttp();
                        break;
                    case 40:
                        refreshView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), "与服务器连接异常！", Toast.LENGTH_SHORT).show();
                        break;
                    case 61:
                        Toast.makeText(getActivity(), "与服务器连接异常，无法删除好友！", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
    }

    private void listToListView(List<OfficialStudentInfo> list) {

        showListView.setVisibility(View.VISIBLE);
        lastPosi = list.size();
        if (lastPosi > 0) {
            showNULL.setVisibility(View.GONE);
            showListView.setVisibility(View.VISIBLE);
        } else {
            showListView.setVisibility(View.GONE);
            showNULL.setVisibility(View.VISIBLE);
        }
        showListView.smoothScrollToPositionFromTop(0, lastPosi);
        refreshView.stopRefresh();
        adapter = new ListViewAdapter(list, getActivity());
        adapter.notifyDataSetChanged();
        showListView.setAdapter(adapter);

    }

    private void itemClick() {
        showListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StuInfoActivity.class);
                intent.putExtra("obj", list.get(position));
                startActivity(intent);
            }
        });

        showNULL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doHttp();
            }
        });

        showListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positonItem = position;
                window = new MyPopupWindow(getActivity(), LayoutInflater.from(getContext()).inflate(R.layout.tool_action_bar_layout, null), 810, 500);
                window.initPopup();
                SetWindowAlpha.setAlpha(0.6f, getActivity());
                window.setTitle("提示");
                window.setMessage("删除好友？");
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
                                String url = "http://" + MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutFriendServletDel?" +
                                        "friend_id=" + list.get(positonItem).getIdOfficialStu();

                                HttpRequestClass requestClass = new HttpRequestClass(url, getActivity(), "POST");
                                int tag = requestClass.setConn();
                                if (1 == tag) {
                                    //服务器连接成功！
                                    String[] strs = requestClass.openThreadHasParam(null);
                                    Message message = Message.obtain();
                                    message.what = 3;
                                    message.obj = strs[1];
                                    new UtilTools().useLog(TAG, strs[0] + ", " + strs[1]);
                                    handler.sendMessage(message);
                                } else {
                                    //服务器连接失败！
                                    Message message22 = Message.obtain();
                                    message22.what = 61;
                                    handler.sendMessage(message22);
                                }
                            }
                        }).start();
                        doHttp();
                        window.destory();
                        SetWindowAlpha.setAlpha(1f, getActivity());
                    }
                });
                return true;
            }
        });
    }

    //查询当前用户的好友！
    private void doHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<OfficialStudentInfo> lists = new ArrayList<OfficialStudentInfo>();
                String queryFriendUrl = "http://" +
                        MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutFriendServlet?query_friend_id=" +
                        MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_STU, "");
                requestClass = new HttpRequestClass(queryFriendUrl, getActivity(), "POST");

                int tag = requestClass.setConn();
                if (1 == tag) {
                    lists = requestClass.openThread();
                    Message message2 = Message.obtain();
                    message2.what = 10;
                    message2.obj = lists;
                    handler.sendMessage(message2);
                } else if (2 == tag) {
                    Message message3 = Message.obtain();
                    message3.what = 40;
                    handler.sendMessage(message3);
                }

            }
        }).start();
    }
}