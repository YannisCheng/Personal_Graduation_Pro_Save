package com.yannischeng.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yannis
 */
public class UtilTools {

    private Context context;

    /**
     * 调用Toast
     *
     * @param context 上下文
     * @param msg     信息
     */
    public void useToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 调用Log
     *
     * @param TAG 各个Activity的名称
     * @param msg 信息
     */
    public void useLog(String TAG, String msg) {
        Log.i(TAG, msg);
    }

    /**
     * 获取时间
     *
     * @return 返回时间
     */
    public String getCurrentTime() {
        String curtTime = "";
        curtTime = new SimpleDateFormat("yyyy-MM-dd/HH:mm").format(new Date());
        return curtTime;
    }

    public void hideSoftKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
