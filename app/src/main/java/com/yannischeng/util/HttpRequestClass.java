package com.yannischeng.util;

import android.content.Context;
import android.util.Log;

import com.yannischeng.bean.CustomeInfo;
import com.yannischeng.bean.MulObj;
import com.yannischeng.bean.MulObjPlus;
import com.yannischeng.bean.OfficialStudentInfo;
import com.yannischeng.bean.Talking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.yannischeng.application.MyApplication.useDBHelper;


/**
 * http请求处理中心
 * Created by 程文佳 on 2016/11/24.
 */

public class HttpRequestClass {

    private final String TAG = "--HttpRequestClass--";
    private String urlStr;
    private URL url = null;
    private HttpURLConnection conn = null;
    private String strData = "";
    private Context context = null;
    private String method = null;

    //构造函数
    public HttpRequestClass(String url, Context context1, String method1) {
        this.urlStr = url;
        this.context = context1;
        this.method = method1;
    }

    //进行连接
    public int setConn() {
        int tag = 0;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        if (method.equals("GET")) {
            conn.setDoOutput(false);
        } else {
            conn.setDoOutput(true);
        }

        conn.setDoInput(true);
        conn.setUseCaches(true);
        conn.setRequestProperty("Content-type", "charset=UTF-8");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        try {
            conn.connect();
            tag = 1;
            new UtilTools().useLog(TAG, conn.toString());
        } catch (IOException e) {
            e.printStackTrace();
            tag = 2;
        }
        return tag;
    }

    //数据的接收
    private String getData() {
        int codeState = 0;
        String getStr = "";
        String msg = "";
        String method = "";
        String contentType = "";
        InputStream in = null;
        Reader reader = null;
        try {
            codeState = conn.getResponseCode();
            msg = conn.getResponseMessage();
            method = conn.getRequestMethod();
            contentType = conn.getContentType() + ", Encode : " + conn.getContentEncoding();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (200 == codeState) {
            new UtilTools().useLog(TAG, "codeState is : " + codeState + ", msg is : " + msg + ", method is : " + method + ", " + contentType);
            try {
                in = conn.getInputStream();
                reader = new InputStreamReader(in, "UTF-8");

                /**
                 * 2016年11月28日00:09:30
                 * 重大bug
                 * char[] charBuf = new char[1024];
                 * bug：产生 大量的 null  导致返回数据无法正确的json解析
                 */
                //char[] charBuf = new char[1024];
                int len = 0;
                StringBuilder str = new StringBuilder();
                while (-1 != (len = reader.read())) {
                    str.append((char) len);
                }
                getStr = str.toString();
                Log.e(TAG, "getData is ：" + getStr);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            new UtilTools().useLog(TAG, " " + codeState + ", : " + msg);
        }
        return getStr;
    }

    private String[] dealJsonString(String str2) {
        String[] strs = null;
        try {
            Log.e(TAG, "dealJsonString: dealJsonString str2 is : " + str2);
            JSONArray jsonArray = new JSONArray(str2);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String result = jsonObject.getString("result");
            String returnInfo = jsonObject.getString("returnInfo");

            strs = new String[]{result, returnInfo};

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strs;
    }

    private List<Talking> dealJsonObjectTalking(String str1) {
        List<Talking> lists = new ArrayList<Talking>();
        Talking talking = null;
        try {
            JSONArray jsonArray = new JSONArray(str1);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray array1 = jsonObject.getJSONArray("obj");
            for (int i = 0; i < array1.length(); i++) {
                talking = new Talking(
                        array1.getJSONObject(i).getString("stuID"),
                        array1.getJSONObject(i).getString("vName"),
                        array1.getJSONObject(i).getString("date"),
                        array1.getJSONObject(i).getString("content"),
                        array1.getJSONObject(i).getString("vClassID"),
                        array1.getJSONObject(i).getString("vID"));
                new UtilTools().useLog(TAG, "studentInfo is : " + talking.toString());
                lists.add(talking);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

    public List<OfficialStudentInfo> dealJsonObject(String str1) {
        List<OfficialStudentInfo> lists = new ArrayList<OfficialStudentInfo>();
        try {
            //获取学生信息
            JSONArray jsonArray = new JSONArray(str1);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray array1 = jsonObject.getJSONArray("obj");
            dealStudent(array1, lists);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }


    //行政班、全年级
    private MulObjPlus dealJsonObjectMO(String str1) {
        List<OfficialStudentInfo> lists = new ArrayList<OfficialStudentInfo>();
        MulObjPlus mo = null;
        int count = 0;
        int pageCount = 0;
        int Mc = 0;
        int Wc = 0;

        try {
            //获取学生信息
            JSONArray array = new JSONArray(str1);
            JSONObject object = array.getJSONObject(0);
            count = object.getInt("pageSize");
            pageCount = object.getInt("pageCount");
            Mc = object.getInt("mc");
            Wc = object.getInt("wc");
            JSONArray array1 = object.getJSONArray("obj");
            dealStudent(array1, lists);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mo = new MulObjPlus(lists, String.valueOf(count), String.valueOf(pageCount), String.valueOf(Mc), String.valueOf(Wc));
        return mo;
    }

    public List<OfficialStudentInfo> openThread() {
        //setConn();
        return dealJsonObject(getData());
    }


    public MulObjPlus openThreadMOAdmin() {
        return dealJsonObjectMO(getData());
    }


    public List<Talking> openThreadTalking() {
        setConn();
        return dealJsonObjectTalking(getData());
    }

    public MulObj openThreadMO() {
        setConn();
        return dealJsonMOObject(getData());
    }

    public String[] openThreadHasParam(String param) {
        return dealJsonString(getData());
    }


    private MulObj dealJsonMOObject(String str1) {
        MulObj mo = null;
        List<OfficialStudentInfo> lists = new ArrayList<OfficialStudentInfo>();

        try {
            //获取学生信息
            JSONArray jsonArray = new JSONArray(str1);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject object1 = jsonObject.getJSONObject("obj");
            JSONArray array1 = object1.getJSONArray("list");
            dealStudent(array1, lists);
            String Mcount = object1.getString("manCount");
            String Wcount = object1.getString("womanCount");
            mo = new MulObj(lists, Mcount, Wcount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mo;
    }

    //无用
    private void dealStudent(JSONArray array1, List<OfficialStudentInfo> lists) {
        OfficialStudentInfo studentInfo = null;
        for (int i = 0; i < array1.length(); i++) {

            try {
                studentInfo = new OfficialStudentInfo(
                        array1.getJSONObject(i).getString("idOfficialStu"),
                        array1.getJSONObject(i).getString("idCardOfficialStu"),
                        array1.getJSONObject(i).getString("nameStu"),
                        array1.getJSONObject(i).getString("nationStu"),
                        array1.getJSONObject(i).getString("sexStu"),
                        array1.getJSONObject(i).getString("dateStu"),
                        array1.getJSONObject(i).getString("heightStu"),
                        array1.getJSONObject(i).getString("weightStu"),
                        array1.getJSONObject(i).getString("addressStu"),
                        Integer.parseInt(array1.getJSONObject(i).getString("numId")),
                        Integer.parseInt(array1.getJSONObject(i).getString("idAll")),
                        Integer.parseInt(array1.getJSONObject(i).getString("classId")));
                //new UtilTools().useLog(TAG,"studentInfo is : " + studentInfo.toString());
                lists.add(studentInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void dealDB(String str1) {
        try {
            //获取学生信息
            JSONArray jsonArray = new JSONArray(str1);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray array1 = jsonObject.getJSONArray("obj");
            for (int i = 0; i < array1.length(); i++) {
                useDBHelper.insertDB(array1, i);
            }
            Log.i(TAG, "dealDB: 数据库初始化结束！");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据库
     */
    public void initDB() {
        dealDB(getData());
    }

    public CustomeInfo openThreadCustomer() {
        return dealJsonCustomeInfo(getData());
    }


    private CustomeInfo dealJsonCustomeInfo(String str1) {
        CustomeInfo customeInfo = null;
        try {
            Log.e(TAG, "dealJsonCustomeInfo  dealJsonCustomeInfo str1 is : " + str1);
            //获取学生信息
            JSONArray jsonArray = new JSONArray(str1);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject object1 = jsonObject.getJSONObject("obj");
            customeInfo = dealCustomer(object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customeInfo;
    }

    private CustomeInfo dealCustomer(JSONObject object1) {
        CustomeInfo customeInfo = null;
        try {
            customeInfo = new CustomeInfo(
                    object1.getString("addressNow"),
                    object1.getString("jobNow"),
                    object1.getString("connectNow"),
                    object1.getString("otherNow"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customeInfo;
    }
}