package com.yannischeng.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yannischeng.model.OfficialStudentInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.yannischeng.db.MyDatabaseHelper.ADDRESS;
import static com.yannischeng.db.MyDatabaseHelper.CLASS_ID;
import static com.yannischeng.db.MyDatabaseHelper.DATE;
import static com.yannischeng.db.MyDatabaseHelper.HEIGHT;
import static com.yannischeng.db.MyDatabaseHelper.ID;
import static com.yannischeng.db.MyDatabaseHelper.IDCARD_STU;
import static com.yannischeng.db.MyDatabaseHelper.ID_ALL;
import static com.yannischeng.db.MyDatabaseHelper.LINE;
import static com.yannischeng.db.MyDatabaseHelper.NAME_STU;
import static com.yannischeng.db.MyDatabaseHelper.NATION_STU;
import static com.yannischeng.db.MyDatabaseHelper.SEX_STU;
import static com.yannischeng.db.MyDatabaseHelper.STU_ID;
import static com.yannischeng.db.MyDatabaseHelper.TABLE_NAME;
import static com.yannischeng.db.MyDatabaseHelper.TABLE_NAME_KEYWORD;
import static com.yannischeng.db.MyDatabaseHelper.TABLE_NAME_LASTNAME;
import static com.yannischeng.db.MyDatabaseHelper.TABLE_NAME_NAME;
import static com.yannischeng.db.MyDatabaseHelper.WEIGHT;

/**
 * 执行数据库的查询
 *
 * Created by 程文佳 on 17-2-7.
 */

public class UseDBHelper {
    private static final String TAG = "UseDBHelper";
    private MyDatabaseHelper helper = null;
    private SQLiteDatabase database = null;
    private ContentValues values = null;
    private Cursor cursor = null;
    private String[] items = null;
    private int PAGE_SIZE = 40;

    public UseDBHelper(Context context) {
        helper = new MyDatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void closeDB() {
        database.close();
    }

    /**
     * private static final String ID = "id";
     * private static final String STU_ID = "stu_id";
     * private static final String IDCARD_STU = "idCard_stu";
     * private static final String NAME_STU = "name_stu";
     * private static final String NATION_STU = "nation_stu";
     * private static final String SEX_STU = "sex_stu";
     * private static final String DATE = "date";
     * private static final String HEIGHT = "height";
     * private static final String WEIGHT = "weight";
     * private static final String CLASS_ID = "class_id";
     * private static final String ADDRESS = "address";
     * private static final String ID_ALL = "id_all";
     * <p>
     * private String idOfficialStu;	//学号 - 1
     * private String idCardOfficialStu;	//身份证号 - 2
     * private String nameStu;	//姓名 - 3
     * private String nationStu;	//民族 - 4
     * private String sexStu;	//性别 - 5
     * private String dateStu;	//出生日期 - 6
     * private String heightStu;	//身高 - 7
     * private String weightStu;	//体重 - 8
     * private String addressStu;	//家庭住址 - 9
     * private int stuId;	//在数据库中的序号 - 10
     * private int IdAll;//在所有学生中的ID值 - 11
     * private int classId;//所在班级的ID值 - 12
     */

    //插入数据
    public void insertDB(JSONArray array1, int i) {
        values = new ContentValues();
        try {
            values.put(ID, array1.getJSONObject(i).getString("numId"));
            values.put(STU_ID, array1.getJSONObject(i).getString("idOfficialStu"));
            values.put(IDCARD_STU, array1.getJSONObject(i).getString("idCardOfficialStu"));
            values.put(NAME_STU, array1.getJSONObject(i).getString("nameStu"));
            values.put(NATION_STU, array1.getJSONObject(i).getString("nationStu"));
            values.put(SEX_STU, array1.getJSONObject(i).getString("sexStu"));
            values.put(DATE, array1.getJSONObject(i).getString("dateStu"));
            values.put(HEIGHT, array1.getJSONObject(i).getString("heightStu"));
            values.put(WEIGHT, array1.getJSONObject(i).getString("weightStu"));
            values.put(CLASS_ID, array1.getJSONObject(i).getString("classId"));
            values.put(ADDRESS, array1.getJSONObject(i).getString("addressStu"));
            values.put(ID_ALL, array1.getJSONObject(i).getString("idAll"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "insertDB: json 解析异常");
        }

        database.insert(TABLE_NAME, null, values);
        Log.i(TAG, "insertDB: 第 " + i + "条数据初始化结束");
    }

    /**
     * add 2017-02-14
     * <p>
     * 将历史搜索记录存入数据库中
     */
    public void insertDBHistory(String data, String tag) {
        values = new ContentValues();
        if (tag.equals("name")) {
            values.put(LINE, data);
            database.insert(TABLE_NAME_NAME, null, values);
            Log.i(TAG, "TABLE_NAME_NAME: 数据初始化结束");
        } else if (tag.equals("ln")) {
            values.put(LINE, data);
            database.insert(TABLE_NAME_LASTNAME, null, values);
            Log.i(TAG, "TABLE_NAME_LASTNAME: 数据初始化结束");
        } else if (tag.equals("kw")) {
            values.put(LINE, data);
            database.insert(TABLE_NAME_KEYWORD, null, values);
            Log.i(TAG, "TABLE_NAME_KEYWORD:  数据初始化结束");
        }
    }

    /**
     * add 2017-02-14
     * <p>
     * 从数据库中取历史搜索记录
     */
    public List<String> queryDBHistory(String tag) {
        List<String> list = new ArrayList<String>();
        if (tag.equals("name")) {
            List<String> names = new ArrayList<String>();
            cursor = database.query(true, TABLE_NAME_NAME, new String[]{LINE}, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                names.add(cursor.getString(cursor.getColumnIndex(LINE)));
            }
            list = names;
        } else if (tag.equals("ln")) {
            List<String> ln = new ArrayList<String>();
            cursor = database.query(true, TABLE_NAME_LASTNAME, new String[]{LINE}, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                ln.add(cursor.getString(cursor.getColumnIndex(LINE)));
            }
            list = ln;
        } else if (tag.equals("kw")) {
            List<String> kw = new ArrayList<String>();
            cursor = database.query(true, TABLE_NAME_KEYWORD, new String[]{LINE}, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                kw.add(cursor.getString(cursor.getColumnIndex(LINE)));
            }
            list = kw;
        }
        return list;
    }

    /**
     * 清除历史数据
     * <p>
     * add in 2017-02-15
     */
    public void clearHistoryData(String tag) {
        if (tag.equals("name")) {
            database.delete(TABLE_NAME_NAME, null, null);
        } else if (tag.equals("ln")) {
            database.delete(TABLE_NAME_LASTNAME, null, null);
        } else if (tag.equals("kw")) {
            database.delete(TABLE_NAME_KEYWORD, null, null);
        }
    }

    /**
     * add in 2017-2-11
     * <p>
     * ---- 查询数据 将从网络获取的数据迁移至此 ----
     */
    public List<OfficialStudentInfo> queryStuInfo(String lastName, String name, String classId, String keyWord, int page) {
        Log.e(TAG, "queryStuInfo get data is : lastName: " + lastName + " ,name: " + name + " ,classId: " + classId + " ,keyWord: " + keyWord + " ,page: " + page);
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        items = new String[]{ID, STU_ID, IDCARD_STU, NAME_STU, NATION_STU, SEX_STU, DATE, HEIGHT, WEIGHT, CLASS_ID, ADDRESS, ID_ALL};

        if (page != 0) {
            //查询班级，全年级信息
            if (lastName == null && name == null && classId == null && keyWord == null) {
                //查询“全年纪”学生信息
                Log.e(TAG, "queryStuInfo: 查询“全年纪”学生信息");
                studentInfos = queryStuInfoAll(page);
            } else if (lastName == null && name == null && keyWord == null && !classId.equals("")) {
                //查询“班级”学生信息
                Log.e(TAG, "queryStuInfo: 查询“班级”学生信息,classid is :" + classId + ", page is : " + page);
                studentInfos = queryStuInfoClass(classId, page);
            }
        } else if (classId != null) {
            //根据班级查询具体信息
            if (name != null) {
                //按“姓名”“班级”查询能学生信息
                Log.e(TAG, "queryStuInfo: 按“姓名”“班级”查询能学生信息");
                studentInfos = queryStuInfoByNC(name, classId);
            } else if (lastName != null) {
                //按“姓氏”“班级”查看学生信息
                Log.e(TAG, "queryStuInfo: 按“姓氏”“班级”查看学生信息");
                studentInfos = queryStuInfoByLNCID(lastName, classId);
            } else if (keyWord != null) {
                //按“班级”“关键字”查询学生信息
                Log.e(TAG, "queryStuInfo: 按“班级”“关键字”查询学生信息");
                studentInfos = queryStuInfoByCK(keyWord, classId);
            }
        } else {
            //根据具体信息查询信息
            if (name != null) {
                //按“姓名”查看学生信息
                Log.e(TAG, "queryStuInfo: 按“姓名”查看学生信息");
                studentInfos = queryStuInfoByN(name);
            } else if (keyWord != null) {
                //按“关键字”查看学生信息
                Log.e(TAG, "queryStuInfo: 按“关键字”查看学生信息");
                studentInfos = queryStuInfoByK(keyWord);
            } else if (lastName != null) {
                //按“姓氏”查看学生信息
                Log.e(TAG, "queryStuInfo: 按“姓氏”查看学生信息");
                studentInfos = queryStuInfoByLNS(lastName);
            }
        }

        //关闭----否则报错
        cursor.close();
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoByLNS(String lastName) {
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        cursor = database.query(TABLE_NAME, items, NAME_STU + " like ?", new String[]{lastName + "%"}, null, null, STU_ID);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoByK(String keyWord) {
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        cursor = database.query(TABLE_NAME, items, NAME_STU + " like ?", new String[]{"%" + keyWord + "%"}, null, null, STU_ID);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoByN(String name) {
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        cursor = database.query(TABLE_NAME, items, NAME_STU + " = ?", new String[]{name}, null, null, STU_ID);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoByLNCID(String lastName, String classId) {
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        cursor = database.query(TABLE_NAME, items, NAME_STU + " like ? AND " + CLASS_ID + " = ?", new String[]{lastName + "%", classId}, null, null, STU_ID);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoByCK(String keyWord, String classId) {
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        cursor = database.query(TABLE_NAME, items, NAME_STU + " like ? AND " + CLASS_ID + " = ?", new String[]{"%" + keyWord + "%", classId}, null, null, STU_ID);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoByNC(String name, String classId) {
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        cursor = database.query(TABLE_NAME, items, NAME_STU + " = ? AND " + CLASS_ID + " = ?", new String[]{name, classId}, null, null, STU_ID);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoClass(String classId, int page) {
        Log.e(TAG, "queryStuInfoClass: classid is :" + classId + ", page is : " + page);

        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        int start = (page - 1) * PAGE_SIZE;
        String limit = String.valueOf(start) + "," + PAGE_SIZE;
        cursor = database.query(TABLE_NAME, items, CLASS_ID + " = ?", new String[]{classId}, null, null, STU_ID, limit);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private List<OfficialStudentInfo> queryStuInfoAll(int page) {
        List<OfficialStudentInfo> studentInfos = new ArrayList<OfficialStudentInfo>();
        int start = (page - 1) * PAGE_SIZE;
        String limit = String.valueOf(start) + "," + PAGE_SIZE;
        cursor = database.query(TABLE_NAME, items, null, null, null, null, STU_ID, limit);
        while (cursor.moveToNext()) {
            studentInfos.add(querySingleStuInfo(cursor));
        }
        return studentInfos;
    }

    private OfficialStudentInfo querySingleStuInfo(Cursor cursor) {
        OfficialStudentInfo officialStudentInfo = null;
         officialStudentInfo = new OfficialStudentInfo(
                cursor.getString(cursor.getColumnIndex(STU_ID)),
                cursor.getString(cursor.getColumnIndex(IDCARD_STU)),
                cursor.getString(cursor.getColumnIndex(NAME_STU)),
                cursor.getString(cursor.getColumnIndex(NATION_STU)),
                cursor.getString(cursor.getColumnIndex(SEX_STU)),
                cursor.getString(cursor.getColumnIndex(DATE)),
                cursor.getString(cursor.getColumnIndex(HEIGHT)),
                cursor.getString(cursor.getColumnIndex(WEIGHT)),
                cursor.getString(cursor.getColumnIndex(ADDRESS)),
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getInt(cursor.getColumnIndex(ID_ALL)),
                cursor.getInt(cursor.getColumnIndex(CLASS_ID))
        );
        return officialStudentInfo;
    }


    //清除本地数据库数据
    public int deleteDB() {
        //返回值表示被删除的数据量
        int count = database.delete(TABLE_NAME, null, null);
        Log.e(TAG, "deleteDB count is :" + count);
        return count;
    }

    /**
     * add in 2017-02-12
     * <p>
     * ---- 查询人数 ----
     */
    public int[] getCounts(String classID, String name, String lastName, String keyWord) {
        /** 每一項的查询中都有：全部，男生，女生*/
        int[] counts = new int[3];

        if (classID == null && name == null && lastName == null && keyWord == null) {
            //查询“全年级”人数
            counts = queryAllClass();
        } else if (classID != null && name == null && lastName == null && keyWord == null) {
            //查询“按班级”分类人数
            counts = queryByClass(classID);
        } else if (classID != null && name != null && lastName == null && keyWord == null) {
            //查询“姓名”“班级”人数
            counts = queryByNameClass(name, classID);
        } else if (classID != null && lastName != null && keyWord == null) {
            //查询“姓氏”“班级”人数
            counts = queryByLastNameClass(lastName, classID);
        } else if (classID != null && name == null && lastName == null && !keyWord.equals("")) {
            //查询“关键字”“班级”人数
            counts = queryByKeyWordClass(keyWord, classID);
        } else if (name != null) {
            //查询“姓名”人数
            counts = queryByName(name);
        } else if (lastName != null) {
            //查询“姓氏”人数
            counts = queryByLastName(lastName);
        } else if (!keyWord.equals("")) {
            //查询“关键字”人数
            counts = queryByKeyWord(keyWord);
        }

        return counts;
    }

    private int[] queryByKeyWord(String keyWord) {
        String sqlAll = "select count(*) from table_all where " + NAME_STU + " like '%" + keyWord + "%'";
        String sqlMan = "select count(*) from table_all where " + NAME_STU + " like '%" + keyWord + "%' and sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where " + NAME_STU + " like '%" + keyWord + "%' and sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] queryByLastName(String lastName) {
        String sqlAll = "select count(*) from table_all where " + NAME_STU + " like '" + lastName + "%'";
        String sqlMan = "select count(*) from table_all where " + NAME_STU + " like '" + lastName + "%' and sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where " + NAME_STU + " like '" + lastName + "%' and sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] queryByName(String name) {
        String sqlAll = "select count(*) from table_all where " + NAME_STU + " = '" + name + "'";
        String sqlMan = "select count(*) from table_all where " + NAME_STU + " = '" + name + "' and sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where " + NAME_STU + " = '" + name + "' and sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] queryByKeyWordClass(String keyWord, String classID) {
        String sqlAll = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " like '%" + keyWord + "%'";
        String sqlMan = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " like '%" + keyWord + "%' and sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " like '%" + keyWord + "%' and sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] queryByLastNameClass(String lastName, String classID) {
        String sqlAll = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " like '" + lastName + "%'";
        String sqlMan = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " like '" + lastName + "%' and sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " like '" + lastName + "%' and sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] queryByNameClass(String name, String classID) {
        String sqlAll = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " = '" + name + "'";
        String sqlMan = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " = '" + name + "' and sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where class_id = '" + classID + "' and " + NAME_STU + " = '" + name + "' and sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] queryByClass(String classID) {
        String sqlAll = "select count(*) from table_all where class_id = '" + classID + "'";
        String sqlMan = "select count(*) from table_all where class_id = '" + classID + "' and sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where class_id = '" + classID + "' and sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] queryAllClass() {
        String sqlAll = "select count(*) from table_all";
        String sqlMan = "select count(*) from table_all where sex_stu = '男'";
        String sqlWoman = "select count(*) from table_all where sex_stu = '女'";
        return doQuery(sqlAll, sqlMan, sqlWoman);
    }

    private int[] doQuery(String sqlAll, String sqlMan, String sqlWoman) {
        int[] countInts = new int[3];
        countInts[0] = queryCount(sqlAll);
        countInts[1] = queryCount(sqlMan);
        countInts[2] = queryCount(sqlWoman);
        return countInts;
    }

    private int queryCount(String sql) {
        int countInt = 0;
        cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        countInt = cursor.getInt(0);
        cursor.close();
        Log.e(TAG, "getTotalCount: countInt is " + countInt + ", sql is : " + sql);
        return countInt;
    }
}
