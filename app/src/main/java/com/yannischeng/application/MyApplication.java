package com.yannischeng.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.yannischeng.bean.OfficialStudentInfo;
import com.yannischeng.db.UseDBHelper;
import com.yannischeng.util.UtilTools;

import org.xutils.x;

/**
 * Created by 程文佳 on 2016/11/1.
 */

public class MyApplication extends Application {

    private final String TAG = "--MyApplication--";
    private BroadcastReceiver receiver = null;
    private UtilTools utilTools;

    //SharedPreferences声明
    public static SharedPreferences preferences = null;
    public static SharedPreferences.Editor editor = null;
    private final String FILENAME = "user_data";

    //用户登录信息保存
    public static String isREMInfo = "_isRemInfo";
    public static String isAutoLogin = "_isAutoLogin";
    public static String USERNAME = "user_name";
    public static String USERID = "user_id";
    public static String FIRSTUSE = "first_use";

    //（官方学生）用户登陆后个人基本信息保存
    public static String ID_OFFICIAL_STU = "id_official_stu";//1
    public static String ID_OFFICIAL_CARD_STU = "id_official_card_stu";//2
    public static String NATION_OFFICIAL_STU = "nation_official_stu";//3
    public static String SEX_OFFICIAL_STU = "sex_official_stu";//4
    public static String HEIGHT_OFFICIAL_STU = "height_official_stu";//5
    public static String WEIGHT_OFFICIAL_STU = "weight_official_stu";//6
    public static String DATE_OFFICIAL_STU = "date_official_stu";//7
    public static String ADDRESS_OFFICIAL_STU = "address_official_stu";//8
    public static String ID_CLASS_OFFICIAL_STU = "id_class_official_stu";//9
    public static String NAME_OFFICIAL_STU = "name_official_stu";//10
    public static String ID_ALL_OFFICIAL_STU = "id_all_official_stu";//11
    public static String CLASS_OFFICIAL_STU = "class_official_stu";//12
    public static String CATEGORY_OFFICIAL_STU = "category_official_stu";//13
    public static String TALKING_OFFICIAL_STU = "talking_official_stu";//14

    //（学生）用户登陆后个人自定义信息保存
    public static String CUSTOMER_STU_ADDRESS = "address_now";//15
    public static String CUSTOMER_STU_JOB = "job_now";//16
    public static String CUSTOMER_STU_CONNECT = "connect_now";//17
    public static String CUSTOMER_STU_OTHER = "other_now";//18

    public static String GETCLASSID = "";

    public static OfficialStudentInfo studentInfo = null;

    //ip地址
    public static String LOCALHOST = "192.168.7.122";

    public static Handler handler = null;
    public static Message message = null;

    //数据库相关初始化
    public static UseDBHelper useDBHelper = null;
    public static String isInitDBOK = "isInitDBOK";

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false);

        useDBHelper = new UseDBHelper(getApplicationContext());

        //SharedPreferences初始化
        preferences = getSharedPreferences(FILENAME, 0);
        editor = preferences.edit();
        utilTools = new UtilTools();
        netListener();


    }

    //进行网络监听服务
    private void netListener() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobile = connM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = connM.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                utilTools.useLog(TAG, " --服务运行-- mobile is: " + mobile.getExtraInfo() + "/ wifi is :" + wifi.getExtraInfo());
                if (!mobile.isConnected() && !wifi.isConnected()) {

                    utilTools.useToast(getApplicationContext(), "网络状态异常~");

                } else {
                }
            }
        };
        registerReceiver(receiver, filter);
    }

    //程序终止时生命周期函数
    @Override
    public void onTerminate() {
        super.onTerminate();
        useDBHelper.closeDB();
        unregisterReceiver(receiver);
        utilTools.useLog(TAG, "--服务终止--,--SQLite数据库关闭--");
    }
}
