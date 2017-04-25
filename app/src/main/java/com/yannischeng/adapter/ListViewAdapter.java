package com.yannischeng.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yannischeng.R;
import com.yannischeng.model.OfficialStudentInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by 程文佳 on 2016/11/23.
 */

public class ListViewAdapter extends BaseAdapter {

    private List<OfficialStudentInfo> lists = null;
    private Context context;

    public ListViewAdapter(List<OfficialStudentInfo> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_single_info, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.list_view_item_user_name);
            holder.category = (TextView) convertView.findViewById(R.id.list_view_item_user_category);
            holder.classID = (TextView) convertView.findViewById(R.id.list_view_item_user_teach);
            holder.adminID = (TextView) convertView.findViewById(R.id.list_view_item_user_class_admin);
            holder.sex = (TextView) convertView.findViewById(R.id.list_view_item_user_sex);
            holder.birth = (TextView) convertView.findViewById(R.id.list_view_item_user_date);
            holder.stuID = (TextView) convertView.findViewById(R.id.list_view_item_user_stu_id);
            holder.headView = (ImageView) convertView.findViewById(R.id.item_user_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.headView.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(lists.get(position).getIdOfficialStu() + ".jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.name.setText(lists.get(position).getNameStu());
        holder.adminID.setText("行政 " + String.valueOf(lists.get(position).getClassId()) + "班");
        holder.birth.setText(lists.get(position).getDateStu());
        holder.category.setText("");
        holder.classID.setText("");
        holder.sex.setText(lists.get(position).getSexStu());
        holder.stuID.setText(lists.get(position).getIdOfficialStu());

        return convertView;
    }

    class ViewHolder {
        ImageView headView;
        TextView name, category, classID, adminID, sex, birth, stuID;
    }
}
