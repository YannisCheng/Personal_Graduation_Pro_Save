package com.yannischeng.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.adapter.ListViewAdapterPopup;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MyPopupWindow {

    private PopupWindow window = null;
    private View view = null;
    private View viewListener = null;
    private int xWidth, yWidth;
    private Context context = null;
    private TextView tvMsg, tvTitle;
    private EditText editText;
    private LinearLayout inputLayout, tvLayout, listViewLayout;
    private RelativeLayout clickLayout;
    public Button okBtn, noBtn;
    private ListView listView;
    private String getStr;
    private String[] setStrs;
    private boolean isCancel = false;

    /**
     * 构造函数
     *
     * @param con  当前的activity
     * @param v    发出监听的view控件
     * @param xWid 在x轴上的宽度
     * @param yWid 在y轴上的宽度
     */
    public MyPopupWindow(Context con, View v, int xWid, int yWid) {
        context = con;
        view = LayoutInflater.from(context).inflate(R.layout.tool_popup_dialog_layout, null);
        viewListener = v;
        xWidth = xWid;
        yWidth = yWid;
        tvMsg = (TextView) view.findViewById(R.id.popup_message);
        editText = (EditText) view.findViewById(R.id.popup_edit_text);
        inputLayout = (LinearLayout) view.findViewById(R.id.popup_edit_text_layout);
        tvLayout = (LinearLayout) view.findViewById(R.id.popup_message_layout);
        listViewLayout = (LinearLayout) view.findViewById(R.id.list_layout);
        clickLayout = (RelativeLayout) view.findViewById(R.id.click_layout);
        okBtn = (Button) view.findViewById(R.id.popup_ok_btn);
        noBtn = (Button) view.findViewById(R.id.popup_cancel_btn);
        listView = (ListView) view.findViewById(R.id.list_odd_choose);
        tvTitle = (TextView) view.findViewById(R.id.popup_title);
    }

    /**
     * 初始化控件
     */
    public void initPopup() {
        window = new PopupWindow(view, xWidth, yWidth);
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.showAtLocation(viewListener, Gravity.CENTER, 0, 0);
    }


    /**
     * ok按钮
     */
    public void onOKBtn(String str) {
        okBtn.setText(str);
    }

    public void onOKBtn(String str, int idColor) {
        okBtn.setText(str);
        okBtn.setTextColor(context.getResources().getColor(idColor));
    }

    /**
     * cancel按钮
     */
    public void onNOBtn(String str) {
        noBtn.setText(str);
    }

    public void onNOBtn(String str, int idColor) {
        noBtn.setText(str);
        noBtn.setTextColor(context.getResources().getColor(idColor));
    }

    /**
     * @param msg 设置提示信息的内容
     */
    public void setMessage(String msg) {
        clickLayout.setVisibility(View.VISIBLE);
        listViewLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.GONE);
        tvLayout.setVisibility(View.VISIBLE);
        tvMsg.setText(msg);
    }

    /**
     * 仅有信息提示
     *
     * @param msg 设置提示信息的内容
     */
    public void setOnlyMessage(String msg) {
        clickLayout.setVisibility(View.VISIBLE);
        listViewLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.GONE);
        tvLayout.setVisibility(View.VISIBLE);
        tvMsg.setText(msg);
    }

    /**
     * @param edMsg 设置editText的内容
     */
    public void setEdTv(String edMsg) {
        clickLayout.setVisibility(View.VISIBLE);
        listViewLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.VISIBLE);
        tvLayout.setVisibility(View.GONE);
        editText.setText(edMsg);
    }

    /**
     * @param str 设置标题
     */
    public void setTitle(String str) {

        tvTitle.setText(str);
    }

    /**
     * 销毁window对象
     */
    public void destory() {
        if (window != null) {
            window.dismiss();
        }

        if (null != window) {
            window = null;
            System.out.println("window is null");
        }
    }

    public ListView doListView(String[] strings) {
        setStrs = strings;
        clickLayout.setVisibility(View.VISIBLE);
        noBtn.setVisibility(View.GONE);
        inputLayout.setVisibility(View.GONE);
        tvLayout.setVisibility(View.GONE);
        listViewLayout.setVisibility(View.VISIBLE);
        ListViewAdapterPopup adapterPopup = new ListViewAdapterPopup(context, strings);
        listView.setAdapter(adapterPopup);
        return listView;
    }


}
