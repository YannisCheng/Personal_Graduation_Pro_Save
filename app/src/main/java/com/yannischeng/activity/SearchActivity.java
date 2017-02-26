package com.yannischeng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.adapter.ListViewAdapter;
import com.yannischeng.adapter.SearchGridAdapter;
import com.yannischeng.application.MyApplication;
import com.yannischeng.bean.MulObj;
import com.yannischeng.bean.OfficialStudentInfo;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.MyPopupWindow;
import com.yannischeng.util.SetWindowAlpha;
import com.yannischeng.util.UtilTools;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.yannischeng.application.MyApplication.isInitDBOK;
import static com.yannischeng.application.MyApplication.preferences;
import static com.yannischeng.application.MyApplication.useDBHelper;


@ContentView(R.layout.activity_search_view_pager_layout)
public class SearchActivity extends AppCompatActivity {

    private final String TAG = "--SearchActivity--";
    private RadioGroup group;
    private RadioButton nameBtn, idStuBnt, keyBtn;
    private EditText inputET, lastET, keyWordET;
    private Button searchBtn;
    private TextView adminBtn;
    private Switch aSwitch;
    private MyPopupWindow window = null;
    private TextView tempBtn, cancel, clearBtn;
    private String getString = "";
    private String btnStr;
    private String[] adminClassStrs = new String[]{"行政 1 班", "行政 2 班", "行政 3 班", "行政 4 班", "行政 5 班", "行政 6 班", "行政 7 班", "行政 8 班", "行政 9 班", "行政 10 班", "行政 11 班", "行政 12 班"};
    private String[] temp = null;
    private static String paramID = "";
    private ListView showListView;
    private ListViewAdapter listViewAdapter;
    private MulObj mo = null;
    private String getURL = "";
    private Handler handler = null;
    private int positoinItem = 0;
    private TextView showTtotle, showMan, showWoman;
    private RelativeLayout relativeLayout, line_Layout;
    private SearchGridAdapter gridAdapter;
    private List<String> nameStrs = null;
    private List<String> lastNameStrs = null;
    private List<String> keyWordStrs = null;
    private GridView gridView;
    private List<String> listStrs = null;

    @ViewInject(R.id.back_action_bar_add_friends)
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        onClick();
        searchListener();
        doHandle();
    }

    /**
     * if 数据 == null
     * relativeLayout gone
     * else
     * relativeLayout visibility
     */
    //执行“搜索”按钮
    private void searchListener() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UtilTools().hideSoftKeyBoard(SearchActivity.this, v);
                relativeLayout.setVisibility(View.GONE);
                new UtilTools().useLog(TAG, "searchBtn is click");
                if (inputET.getText().toString().equals("") && lastET.getText().toString().equals("") && keyWordET.getText().toString().equals("")) {
                    new UtilTools().useToast(SearchActivity.this, "输入内容不能为空！");
                } else {

                    if (inputET.getText().toString().equals("")) {
                        if (lastET.getText().toString().equals("")) {
                            if (keyWordET.getText().toString().equals("")) {
                            } else {
                                if (getString.equals("")) {
                                    useDBHelper.insertDBHistory(keyWordET.getText().toString(), "kw");
                                    //根据 关键字  查询
                                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                                        Log.e(TAG, "setClassStuInfo: 数据已经本地化，使用本地数据库");
                                        List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
                                        //获取学生信息
                                        studentInfoList = useDBHelper.queryStuInfo(null, null, null, keyWordET.getText().toString(), 0);
                                        //获取各项人数
                                        int[] ints = useDBHelper.getCounts(null, null, null, keyWordET.getText().toString());
                                        initDBtoList(studentInfoList, ints);
                                    } else {
                                        Log.e(TAG, "setClassStuInfo: 数据未进行本地化，将会请求网络数据");
                                        new UtilTools().useLog(TAG, "paramKeyWord : " + keyWordET.getText().toString());
                                        String url = "http://" +
                                                MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutQueryServlet?TAG=KEYWord&keyWord=%25" +
                                                keyWordET.getText().toString() + "%25";
                                        doHttp(url);
                                        //doHandle();
                                    }

                                } else {
                                    //查询 关键字 + 班级
                                    useDBHelper.insertDBHistory(keyWordET.getText().toString(), "kw");
                                    if (1 == preferences.getInt(isInitDBOK, 0)) {
                                        Log.e(TAG, "setClassStuInfo: 数据已经本地化，使用本地数据库");
                                        List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
                                        //获取学生信息
                                        studentInfoList = useDBHelper.queryStuInfo(null, null, getString, keyWordET.getText().toString(), 0);
                                        //获取各项人数
                                        int[] ints = useDBHelper.getCounts(getString, null, null, keyWordET.getText().toString());
                                        initDBtoList(studentInfoList, ints);
                                    } else {
                                        Log.e(TAG, "setClassStuInfo: 数据未进行本地化，将会请求网络数据");
                                        new UtilTools().useLog(TAG, "paramKeyWord : " + keyWordET.getText().toString() + ",getString : " + getString);
                                        String url = "http://" +
                                                MyApplication.LOCALHOST + ":8080/FN_Pro_Server/AboutQueryServlet?TAG=keyWordByClassID&keyWord=%25" +
                                                keyWordET.getText().toString() + "%25&classID=" + getString;
                                        doHttp(url);
                                        //doHandle();
                                    }
                                }
                            }
                        } else {
                            useDBHelper.insertDBHistory(lastET.getText().toString(), "ln");
                            if (getString.equals("")) {
                                //根据 姓氏 查询
                                if (1 == preferences.getInt(isInitDBOK, 0)) {
                                    Log.e(TAG, "setClassStuInfo: 数据已经本地化，使用本地数据库");
                                    List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
                                    //获取学生信息
                                    studentInfoList = useDBHelper.queryStuInfo(lastET.getText().toString(), null, null, null, 0);
                                    //获取各项人数
                                    int[] ints = useDBHelper.getCounts(null, null, lastET.getText().toString(), null);
                                    initDBtoList(studentInfoList, ints);
                                } else {
                                    Log.e(TAG, "setClassStuInfo: 数据未进行本地化，将会请求网络数据");
                                    new UtilTools().useLog(TAG, "paramLastName : " + lastET.getText().toString());
                                    String url = "http://" + MyApplication.LOCALHOST +
                                            ":8080/FN_Pro_Server/AboutQueryServlet?TAG=STUID&stuID=" +
                                            lastET.getText().toString() + "%25";
                                    doHttp(url);
                                    //doHandle();
                                }
                            } else {
                                //根据 姓氏 + 班级
                                useDBHelper.insertDBHistory(lastET.getText().toString(), "ln");
                                if (1 == preferences.getInt(isInitDBOK, 0)) {
                                    Log.e(TAG, "setClassStuInfo: 数据已经本地化，使用本地数据库");
                                    List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
                                    //获取学生信息
                                    studentInfoList = useDBHelper.queryStuInfo(lastET.getText().toString(), null, getString, null, 0);
                                    //获取各项人数
                                    int[] ints = useDBHelper.getCounts(getString, null, lastET.getText().toString(), null);
                                    initDBtoList(studentInfoList, ints);
                                } else {
                                    Log.e(TAG, "setClassStuInfo: 数据未进行本地化，将会请求网络数据");
                                    new UtilTools().useLog(TAG, "paramLastName : " + lastET.getText().toString() + ",getString : " + getString);
                                    String url = "http://" +
                                            MyApplication.LOCALHOST +
                                            ":8080/FN_Pro_Server/AboutQueryServlet?TAG=stuIDByClassID&stuID=" +
                                            lastET.getText().toString() + "%25&classID=" +
                                            getString;
                                    doHttp(url);
                                    //doHandle();
                                }
                            }
                        }
                    } else {
                        useDBHelper.insertDBHistory(inputET.getText().toString(), "name");
                        if (getString.equals("")) {
                            //根据 姓名 查询
                            if (1 == preferences.getInt(isInitDBOK, 0)) {
                                Log.e(TAG, "setClassStuInfo: 数据已经本地化，使用本地数据库");
                                List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
                                //获取学生信息
                                studentInfoList = useDBHelper.queryStuInfo(null, inputET.getText().toString(), null, null, 0);
                                //获取各项人数
                                int[] ints = useDBHelper.getCounts(null, inputET.getText().toString(), null, null);
                                initDBtoList(studentInfoList, ints);
                            } else {
                                Log.e(TAG, "setClassStuInfo: 数据未进行本地化，将会请求网络数据");
                                new UtilTools().useLog(TAG, "parameName : " + inputET.getText().toString());
                                String url = "http://" +
                                        MyApplication.LOCALHOST +
                                        ":8080/FN_Pro_Server/AboutQueryServlet?TAG=Name&name=" +
                                        inputET.getText().toString();
                                doHttp(url);
                                //doHandle();
                            }
                        } else {
                            //根据 姓名 + 班级
                            useDBHelper.insertDBHistory(inputET.getText().toString(), "name");
                            if (1 == preferences.getInt(isInitDBOK, 0)) {
                                Log.e(TAG, "setClassStuInfo: 数据已经本地化，使用本地数据库");
                                List<OfficialStudentInfo> studentInfoList = new ArrayList<OfficialStudentInfo>();
                                //获取学生信息
                                studentInfoList = useDBHelper.queryStuInfo(null, inputET.getText().toString(), getString, null, 0);
                                //获取各项人数
                                int[] ints = useDBHelper.getCounts(getString, inputET.getText().toString(), null, null);
                                initDBtoList(studentInfoList, ints);
                            } else {
                                Log.e(TAG, "setClassStuInfo: 数据未进行本地化，将会请求网络数据");
                                new UtilTools().useLog(TAG, "parameName : " + inputET.getText().toString() + ",getString : " + getString);
                                String url = "http://" +
                                        MyApplication.LOCALHOST +
                                        ":8080/FN_Pro_Server/AboutQueryServlet?TAG=NameByClassID&name=" +
                                        inputET.getText().toString() + "&classID=" +
                                        getString;
                                doHttp(url);
                                //doHandle();
                            }
                        }
                    }
                }

            }
        });
    }

    //本地化数据填充至界面UI
    private void initDBtoList(List<OfficialStudentInfo> studentInfoList, int[] ints) {
        if (studentInfoList == null) {
            new UtilTools().useToast(SearchActivity.this, "与服务器连接异常");
        } else if (studentInfoList == null || studentInfoList.size() == 0) {
            new UtilTools().useToast(SearchActivity.this, "查询内容为空！");
            showListView.setVisibility(View.GONE);
            showTtotle.setText("共:" + 0 + "人");
            showMan.setText("男:" + 0 + "人");
            showWoman.setText("女:" + 0 + "人");
        } else {
            showTtotle.setText("共:" + ints[0] + "人");
            showMan.setText("男:" + ints[1] + "人");
            showWoman.setText("女:" + ints[2] + "人");
            listViewAdapter = new ListViewAdapter(studentInfoList, SearchActivity.this);
            listViewAdapter.notifyDataSetChanged();
            showListView.setAdapter(listViewAdapter);
            showListView.setVisibility(View.VISIBLE);
            itemClick(studentInfoList);
        }
    }

    //请求数据
    private void doHttp(String url) {
        getURL = url;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message1 = Message.obtain();
                message1.what = 12;
                message1.obj = new HttpRequestClass(getURL, SearchActivity.this, "POST").openThreadMO();
                handler.sendMessage(message1);
            }
        }).start();
    }

    //显示数据
    private void doHandle() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 12:
                        mo = (MulObj) msg.obj;
                        if (mo == null) {
                            new UtilTools().useToast(SearchActivity.this, "与服务器连接异常");
                        } else if (mo.getList() == null || mo.getList().size() == 0) {
                            new UtilTools().useToast(SearchActivity.this, "查询内容为空！");
                            showListView.setVisibility(View.GONE);
                            showTtotle.setText("共:" + 0 + "人");
                            showMan.setText("男:" + 0 + "人");
                            showWoman.setText("女:" + 0 + "人");
                        } else {
                            showTtotle.setText("共:" + mo.getList().size() + "人");
                            showMan.setText("男:" + mo.getManCount() + "人");
                            showWoman.setText("女:" + mo.getWomanCount() + "人");
                            listViewAdapter = new ListViewAdapter(mo.getList(), SearchActivity.this);
                            listViewAdapter.notifyDataSetChanged();
                            showListView.setVisibility(View.VISIBLE);
                            showListView.setAdapter(listViewAdapter);
                            itemClick(mo.getList());
                        }
                        break;
                    case 2:
                        if (((String[]) msg.obj)[0].equals("1")) {
                            Snackbar snackbar = Snackbar.make(group, ((String[]) msg.obj)[1].toString(), Snackbar.LENGTH_LONG).setAction("查看", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(SearchActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                            snackbar.setActionTextColor(Color.WHITE);
                            snackbar.getView().setBackgroundResource(R.color.Blue_09c);
                            snackbar.show();
                        } else {
                            new UtilTools().useToast(SearchActivity.this, ((String[]) msg.obj)[1]);
                        }
                        break;
                    case 22:
                        new UtilTools().useToast(SearchActivity.this, "与服务器连接异常");
                        break;

                }
            }
        };
    }

    private void itemClick(final List<OfficialStudentInfo> list) {
        //查看学生详情
        showListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, StuInfoActivity.class);
                intent.putExtra("obj", list.get(position));
                startActivity(intent);
            }
        });

        //否添加好友
        showListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positoinItem = position;
                window = new MyPopupWindow(SearchActivity.this, LayoutInflater.from(SearchActivity.this).inflate(R.layout.tool_action_bar_layout, null), 810, 500);
                window.initPopup();
                SetWindowAlpha.setAlpha(0.5f, SearchActivity.this);
                window.setTitle("提示");
                window.setMessage("添加为好友？");
                window.onOKBtn("是的");
                window.onNOBtn("不");
                window.noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.destory();
                        SetWindowAlpha.setAlpha(1f, SearchActivity.this);
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
                                        list.get(positoinItem).getIdOfficialStu();
                                HttpRequestClass requestClass = new HttpRequestClass(url, SearchActivity.this, "POST");
                                int tag = requestClass.setConn();
                                if (tag == 1) {
                                    String[] strs = requestClass.openThreadHasParam(null);
                                    Message message2 = Message.obtain();
                                    message2.what = 2;
                                    message2.obj = strs;
                                    new UtilTools().useLog(TAG, strs[0] + ", " + strs[1]);
                                    handler.sendMessage(message2);
                                } else {
                                    Message message3 = Message.obtain();
                                    message3.what = 22;
                                    handler.sendMessage(message3);
                                }

                            }
                        }).start();
                        window.destory();
                        SetWindowAlpha.setAlpha(1f, SearchActivity.this);
                    }
                });
                return true;
            }
        });
    }

    private TextView historyNull;

    private void initView() {
        line_Layout = (RelativeLayout) findViewById(R.id.line_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.show_grid);
        group = (RadioGroup) findViewById(R.id.radio_group_three_odds);
        adminBtn = (TextView) findViewById(R.id.admin_class_odd_search_fuzzy);
        aSwitch = (Switch) findViewById(R.id.switch_btn);
        keyBtn = (RadioButton) findViewById(R.id.search_by_stu_last_name);
        nameBtn = (RadioButton) findViewById(R.id.search_by_name);
        idStuBnt = (RadioButton) findViewById(R.id.search_by_stu_id);
        inputET = (EditText) findViewById(R.id.search_et);
        lastET = (EditText) findViewById(R.id.search_et_last_name);
        keyWordET = (EditText) findViewById(R.id.search_et_key_word);
        searchBtn = (Button) findViewById(R.id.search_btn);
        showListView = (ListView) findViewById(R.id.search_list_view);
        showTtotle = (TextView) findViewById(R.id.totle);
        showMan = (TextView) findViewById(R.id.totle_man);
        showWoman = (TextView) findViewById(R.id.totle_woman);
        backBtn.setVisibility(View.VISIBLE);
        cancel = (TextView) findViewById(R.id.cancel_history_btn_grid);
        clearBtn = (TextView) findViewById(R.id.clear_history);
        gridView = (GridView) findViewById(R.id.init_grid_data);
        historyNull = (TextView) findViewById(R.id.history_null);
        gridView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
        nameStrs = new ArrayList<String>();
        lastNameStrs = new ArrayList<String>();
        keyWordStrs = new ArrayList<String>();

        listStrs = new ArrayList<String>();

        if ("全年級".equals(adminBtn.getText().toString())) {
            adminBtn.setClickable(false);
        } else {
            adminBtn.setClickable(true);
        }
    }

    private void onClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.GONE);
            }
        });

        //返回MainActivity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                finish();
            }
        });

        //选择班级
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adminBtn.setClickable(true);
                    adminBtn.setText("行政班");
                    adminBtn.setTextColor(getResources().getColor(R.color.Orange_f60));
                    usePopup(adminClassStrs, adminBtn, "行政班");
                    adminBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            usePopup(adminClassStrs, adminBtn, "行政班");
                        }
                    });
                } else {
                    adminBtn.setClickable(false);
                    adminBtn.setText("全年级");
                    adminBtn.setBackgroundResource(R.drawable.edit_text_bg_focus);
                    adminBtn.setTextColor(getResources().getColor(R.color.Orange_f60));
                    getString = "";
                }
            }
        });

        //具体条件的选择填写
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                relativeLayout.setVisibility(View.GONE);
                showListView.setVisibility(View.GONE);
                showTtotle.setText("共:" + 0 + "人");
                showMan.setText("男:" + 0 + "人");
                showWoman.setText("女:" + 0 + "人");
                switch (checkedId) {
                    case R.id.search_by_stu_last_name:
                        //使用InputFilter 进行过滤,字符最大长度为1
                        keyWordET.setVisibility(View.VISIBLE);
                        keyWordET.setFocusable(true);
                        keyWordET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                        lastET.setVisibility(View.GONE);
                        inputET.setVisibility(View.GONE);
                        lastET.setText("");
                        inputET.setText("");
                        break;
                    case R.id.search_by_name:
                        inputET.setVisibility(View.VISIBLE);
                        inputET.setFocusable(true);
                        inputET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                        lastET.setVisibility(View.GONE);
                        keyWordET.setVisibility(View.GONE);
                        lastET.setText("");
                        keyWordET.setText("");
                        break;
                    case R.id.search_by_stu_id:
                        lastET.setVisibility(View.VISIBLE);
                        lastET.setFocusable(true);
                        lastET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                        inputET.setVisibility(View.GONE);
                        keyWordET.setVisibility(View.GONE);
                        inputET.setText("");
                        keyWordET.setText("");
                        break;
                }
            }
        });

        lastET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                relativeLayout.setVisibility(View.VISIBLE);
                listStrs = useDBHelper.queryDBHistory("ln");
                doGridAdapter(listStrs, "ln");
                if (listStrs.size() > 0) {
                    gridView.setVisibility(View.VISIBLE);
                    historyNull.setVisibility(View.GONE);
                    line_Layout.setVisibility(View.VISIBLE);
                } else {
                    gridView.setVisibility(View.GONE);
                    historyNull.setVisibility(View.VISIBLE);
                    line_Layout.setVisibility(View.GONE);
                }
                return false;
            }
        });

        inputET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                relativeLayout.setVisibility(View.VISIBLE);
                listStrs = useDBHelper.queryDBHistory("name");
                doGridAdapter(listStrs, "name");
                if (listStrs.size() > 0) {
                    gridView.setVisibility(View.VISIBLE);
                    historyNull.setVisibility(View.GONE);
                    line_Layout.setVisibility(View.VISIBLE);
                } else {
                    gridView.setVisibility(View.GONE);
                    historyNull.setVisibility(View.VISIBLE);
                    line_Layout.setVisibility(View.GONE);
                }
                return false;
            }
        });

        keyWordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                relativeLayout.setVisibility(View.VISIBLE);
                listStrs = useDBHelper.queryDBHistory("kw");
                doGridAdapter(listStrs, "kw");
                if (listStrs.size() > 0) {
                    gridView.setVisibility(View.VISIBLE);
                    historyNull.setVisibility(View.GONE);
                    line_Layout.setVisibility(View.VISIBLE);
                } else {
                    gridView.setVisibility(View.GONE);
                    historyNull.setVisibility(View.VISIBLE);
                    line_Layout.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void doGridAdapter(final List<String> lists, String tag) {
        Log.e(TAG, "doGridAdapter: gridview 适配");
        gridAdapter = new SearchGridAdapter(SearchActivity.this, lists);
        gridView.setAdapter(gridAdapter);
        if (tag.equals("name")) {
            Log.e(TAG, "doGridAdapter: name 适配");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    inputET.setText(lists.get(i));
                    relativeLayout.setVisibility(View.GONE);
                    new UtilTools().hideSoftKeyBoard(SearchActivity.this, view);
                }
            });

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "clearBtn: name");
                    useDBHelper.clearHistoryData("name");
                    historyNull.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    line_Layout.setVisibility(View.GONE);
                }
            });
        } else if (tag.equals("ln")) {
            Log.e(TAG, "doGridAdapter: ln 适配");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    lastET.setText(lists.get(i));
                    relativeLayout.setVisibility(View.GONE);
                    new UtilTools().hideSoftKeyBoard(SearchActivity.this, view);
                }
            });
            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "clearBtn: ln");
                    useDBHelper.clearHistoryData("ln");
                    historyNull.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    line_Layout.setVisibility(View.GONE);
                }
            });
        } else if (tag.equals("kw")) {
            Log.e(TAG, "doGridAdapter: kw 适配");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    keyWordET.setText(lists.get(i));
                    relativeLayout.setVisibility(View.GONE);
                    new UtilTools().hideSoftKeyBoard(SearchActivity.this, view);
                }
            });
            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "clearBtn: kw");
                    useDBHelper.clearHistoryData("kw");
                    historyNull.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    line_Layout.setVisibility(View.GONE);
                }
            });
        }
    }

    //使用PopupWindow
    private String usePopup(String[] strings, TextView btn, final String btnStr1) {
        ListView listView = null;
        tempBtn = btn;
        temp = strings;
        btnStr = btnStr1;
        window = new MyPopupWindow(SearchActivity.this,
                LayoutInflater.from(SearchActivity.this).inflate(R.layout.tool_action_bar_user_detail_layout, null), 810, 900);
        window.initPopup();
        SetWindowAlpha.setAlpha(0.5f, SearchActivity.this);
        window.setTitle("选择班级");
        listView = window.doListView(strings);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getString = String.valueOf(position + 1);
                tempBtn.setText(temp[position]);
                tempBtn.setBackgroundResource(R.drawable.edit_text_bg_focus);
                tempBtn.setTextColor(getResources().getColor(R.color.Orange_f60));
                if (null == getString) {
                } else {
                    SetWindowAlpha.setAlpha(1f, SearchActivity.this);
                }
                new UtilTools().useToast(SearchActivity.this, "您选择了 : " + temp[position]);
                new UtilTools().useLog(TAG, "usePopup中的getString is : " + getString);
                window.destory();
            }
        });
        window.onOKBtn("取消");
        window.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aSwitch.setChecked(false);
                window.destory();
                SetWindowAlpha.setAlpha(1f, SearchActivity.this);
            }
        });
        return getString;
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
