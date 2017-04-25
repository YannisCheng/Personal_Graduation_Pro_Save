package com.yannischeng.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 创建本地数据库
 *
 * Created by 程文佳 on 2017-2-7.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * id,stu_id,idcard_stu,name_stu,nation_stu,sex_stu,date,height,weight,class_id,address.id_allq
     * 说明：
     * id：     在所在班级中的id值
     * stu_id:  学号
     * id_all:  在所有学生中的id值
     */

    private static final String TAG = "MyDatabaseHelper";
    private Context context;
    private static final String DBNAME = "STUDENT_INFO";
    private static final int VERSION = 1;

    public static final String TABLE_NAME = "table_all";
    public static final String TABLE_NAME_NAME = "table_name";
    public static final String TABLE_NAME_LASTNAME = "table_lastName";
    public static final String TABLE_NAME_KEYWORD = "table_keyWord";

    public static final String LINE = "data";
    private static final String ID_HISTORY = "_id";

    public static final String ID = "id";
    public static final String STU_ID = "stu_id";
    public static final String IDCARD_STU = "idCard_stu";
    public static final String NAME_STU = "name_stu";
    public static final String NATION_STU = "nation_stu";
    public static final String SEX_STU = "sex_stu";
    public static final String DATE = "date";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String CLASS_ID = "class_id";
    public static final String ADDRESS = "address";
    public static final String ID_ALL = "id_all";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTabel_All(sqLiteDatabase);
        createHistoryRecName(sqLiteDatabase);
        createHistoryRecLastName(sqLiteDatabase);
        createHistoryRecKeyWord(sqLiteDatabase);
    }

    private void createHistoryRecKeyWord(SQLiteDatabase sqLiteDatabase) {
        String strSql = "create table if not exists " + TABLE_NAME_KEYWORD + "(" +
                LINE + " TEXT NOT NULL," +
                ID_HISTORY + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" + ")";
        Log.i(TAG, "表table_keyword创建语句为 : " + strSql);
        sqLiteDatabase.execSQL(strSql);
        Log.e(TAG, "表table_keyword创建成功！ ");
    }

    private void createHistoryRecLastName(SQLiteDatabase sqLiteDatabase) {
        String strSql = "create table if not exists " + TABLE_NAME_LASTNAME + "(" +
                LINE + " TEXT NOT NULL," +
                ID_HISTORY + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" + ")";
        Log.i(TAG, "表table_lastname创建语句为 : " + strSql);
        sqLiteDatabase.execSQL(strSql);
        Log.e(TAG, "表table_lastname创建成功！ ");
    }

    private void createHistoryRecName(SQLiteDatabase sqLiteDatabase) {
        String strSql = "create table if not exists " + TABLE_NAME_NAME + "(" +
                LINE + " TEXT NOT NULL," +
                ID_HISTORY + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" + ")";
        Log.i(TAG, "表table_name创建语句为 : " + strSql);
        sqLiteDatabase.execSQL(strSql);
        Log.e(TAG, "表table_name创建成功！ ");
    }

    private void createTabel_All(SQLiteDatabase sqLiteDatabase) {
        String strSql = "create table if not exists " + TABLE_NAME + "(" +
                ID + " TEXT NOT NULL," +
                STU_ID + " TEXT NOT NULL," +
                IDCARD_STU + " TEXT NOT NULL," +
                NAME_STU + " TEXT NOT NULL," +
                NATION_STU + " TEXT NOT NULL," +
                SEX_STU + " TEXT NOT NULL," +
                DATE + " TEXT NOT NULL," +
                HEIGHT + " TEXT NOT NULL," +
                WEIGHT + " TEXT NOT NULL," +
                CLASS_ID + " TEXT NOT NULL," +
                ADDRESS + " TEXT NOT NULL," +
                ID_ALL + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" + ")";

        Log.i(TAG, "表table_all创建语句为 : " + strSql);

        sqLiteDatabase.execSQL(strSql);
        Log.e(TAG, "表table_all创建成功！ ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
