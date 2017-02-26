package com.yannischeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.yannischeng.R;
import com.yannischeng.application.MyApplication;
import com.yannischeng.util.HttpRequestClass;
import com.yannischeng.util.UtilTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


/**
 * 用户登录
 * 使用xUtils框架
 * created by 程文佳 2016年11月1日19:49:57
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.login_ok)
    private Button loginBtn;
    @ViewInject(R.id.input_id_card)
    private EditText idCardET;
    @ViewInject(R.id.checkBoxRem)
    private CheckBox chRem;
    @ViewInject(R.id.checkBoxAuto)
    private CheckBox chAuto;
    @ViewInject(R.id.cancel_btn_edit)
    private Button cancelBtn;

    private UtilTools utilTools = null;
    private String userName = null;
    private String userID = null;
    private JSONObject object1 = null;

    private final String TAG = "--LoginActivity--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        utilTools = new UtilTools();
        userName = MyApplication.preferences.getString(MyApplication.USERNAME, "");
        userID = MyApplication.preferences.getString(MyApplication.USERID, "");

        judgeCB();
        checkEdit();
        onClick();

    }

    //实时检测EditText的输入
    private void checkEdit() {
        idCardET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.toString().length() > 0)) {
                    cancelBtn.setVisibility(View.VISIBLE);
                } else {
                    cancelBtn.setVisibility(View.GONE);
                }
                if ((s.toString().length() < 18)) {
                    loginBtn.setVisibility(View.GONE);
                    idCardET.setError("还差" + (18 - (s.toString().length())) + "位");
                } else {
                    loginBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //从sp中读取数据
    private void judgeCB() {
        //记住用户信息
        if (MyApplication.preferences.getBoolean(MyApplication.isREMInfo, false)) {
            chRem.setChecked(true);
            cancelBtn.setVisibility(View.VISIBLE);
            idCardET.setText(MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_CARD_STU, ""));
            idCardET.setSelection(idCardET.getText().length());
            loginBtn.setVisibility(View.VISIBLE);
        } else {
            loginBtn.setVisibility(View.GONE);
            chRem.setChecked(false);
            cancelBtn.setVisibility(View.GONE);
        }

        //自动登录
        if (MyApplication.preferences.getBoolean(MyApplication.isAutoLogin, false)) {
            chAuto.setChecked(true);
            System.out.println("judgeCB isAutoLogin true");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            chAuto.setChecked(false);
            System.out.println("judgeCB isAutoLogin false");
        }

        utilTools.useLog(TAG, "id_card is : " + MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_CARD_STU, ""));

    }

    private void loginBtn() {
        RequestParams params = new RequestParams("http://" + MyApplication.LOCALHOST + ":8080/FN_Pro_Server/LoginServlet?method=login");
        params.addBodyParameter("user_id_stu", idCardET.getText().toString());
        new UtilTools().useLog(TAG, params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONArray jsonArray = null;
                JSONObject object = null;
                try {
                    jsonArray = new JSONArray(new String(result));
                    object = jsonArray.getJSONObject(0);
                    int resultCode = object.getInt("result");
                    new UtilTools().useLog(TAG, "开始获取请求码");
                    if (1 == resultCode) {
                        //用户存在，登录成功，处理数据
                        dealData(object);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (2 == resultCode) {
                        //用户不存在
                        String msg = object.getString("returnInfo");
                        new UtilTools().useToast(LoginActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {
                    Toast.makeText(LoginActivity.this, "与服务器连接异常", Toast.LENGTH_LONG).show();
                } else if (ex instanceof ConnectException) {
                    Toast.makeText(LoginActivity.this, "与服务器连接异常", Toast.LENGTH_LONG).show();
                } else if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "与服务器连接异常", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void dealData(JSONObject object) {

        if (chRem.isChecked()) {
            MyApplication.editor.putBoolean(MyApplication.isREMInfo, true);
        } else {
            MyApplication.editor.putBoolean(MyApplication.isREMInfo, false);
        }

        if (chAuto.isChecked()) {
            MyApplication.editor.putBoolean(MyApplication.isAutoLogin, true);
            System.out.println("dealData isAutoLogin true");
        } else {
            MyApplication.editor.putBoolean(MyApplication.isAutoLogin, false);
        }

        //个人信息存储
        try {
            String msg = object.getString("returnInfo");
            object1 = object.getJSONObject("obj");
            new UtilTools().useLog(TAG, "dealData " + object1.toString());

            /**
             * 2016年11月25日17:12:29
             * 当前用户check
             */
            CheckStu checkStu = new CheckStu();
            Thread thread = new Thread(checkStu);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (object1.getString("idCardOfficialStu").equals(MyApplication.preferences.getString(MyApplication.ID_OFFICIAL_CARD_STU, null))) {
            } else {
                utilTools.useLog(TAG, "再执行MyApplication.editor");
                MyApplication.editor.putString(MyApplication.ID_OFFICIAL_STU, object1.getString("idOfficialStu"));
                MyApplication.editor.putString(MyApplication.ID_OFFICIAL_CARD_STU, object1.getString("idCardOfficialStu"));
                MyApplication.editor.putString(MyApplication.NATION_OFFICIAL_STU, object1.getString("nationStu"));
                MyApplication.editor.putString(MyApplication.SEX_OFFICIAL_STU, object1.getString("sexStu"));
                MyApplication.editor.putString(MyApplication.HEIGHT_OFFICIAL_STU, object1.getString("heightStu"));
                MyApplication.editor.putString(MyApplication.WEIGHT_OFFICIAL_STU, object1.getString("weightStu"));
                MyApplication.editor.putString(MyApplication.DATE_OFFICIAL_STU, object1.getString("dateStu"));
                MyApplication.editor.putString(MyApplication.ADDRESS_OFFICIAL_STU, object1.getString("addressStu"));
                MyApplication.editor.putString(MyApplication.ID_CLASS_OFFICIAL_STU, object1.getString("numId"));
                MyApplication.editor.putString(MyApplication.NAME_OFFICIAL_STU, object1.getString("nameStu"));
                MyApplication.editor.putString(MyApplication.ID_ALL_OFFICIAL_STU, object1.getString("idAll"));
                MyApplication.editor.putString(MyApplication.CLASS_OFFICIAL_STU, object1.getString("classId"));
            }

            new UtilTools().useToast(LoginActivity.this,/*msg*/"欢迎 " + object1.getString("nameStu") + "  同学  >v<");
            utilTools.useLog(TAG, "editor.commit()" + userName + ",/," + userID);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyApplication.editor.commit();
    }

    private class CheckStu implements Runnable {
        @Override
        public void run() {
            //查询当前用户是否存在
            String getCurUser = null;
            try {
                getCurUser = object1.getString("idOfficialStu");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = "http://" +
                    MyApplication.LOCALHOST +
                    ":8080/FN_Pro_Server/LoginServlet?method=check&user_check_id=" + getCurUser;
            HttpRequestClass requestClass = new HttpRequestClass(url, LoginActivity.this, "POST");
            int tag = requestClass.setConn();
            String[] strs = requestClass.openThreadHasParam(null);
            new UtilTools().useLog(TAG, "tag is : " + strs[0] + ", msg is : " + strs[1]);

            if (Integer.parseInt(strs[0]) == 1) {
                new UtilTools().useLog(TAG, "提示 此用户已存在");

            } else if (Integer.parseInt(strs[0]) == 2) {

                new UtilTools().useLog(TAG, "提示 此用户不存在");
                String url2 = "http://" +
                        MyApplication.LOCALHOST +
                        ":8080/FN_Pro_Server/LoginServlet?method=insert&user_suc_id=" + getCurUser;
                HttpRequestClass requestClass1 = new HttpRequestClass(url2, LoginActivity.this, "POST");
                int tag2 = requestClass1.setConn();
                requestClass1.openThreadHasParam(null);
            }
            utilTools.useLog(TAG, "首先执行checkStudent 子线程 join()方法");
        }
    }

    private void onClick() {

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCardET.setText("");
                idCardET.setHint("输入身份证号");
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idCardET.getText().toString().length() > 18 || idCardET.getText().toString().length() < 18) {
                    idCardET.setError("位数不对哦");
                } else {
                    //登录
                    loginBtn();
                }

            }
        });
    }
}
