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
import com.yannischeng.model.Talking;

import java.io.IOException;
import java.util.List;

/**
 * 发表言论适配器
 *
 * Created by 程文佳 on 2016/11/25.
 */

public class ListViewAdapterTalk extends BaseAdapter {

    private List<Talking> list = null;
    private Context context = null;

    public ListViewAdapterTalk(List<Talking> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("--ListViewAdapterTalk--" + list.get(position));
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_talking_item_layout, null);
            holder.ueadHead = (ImageView) convertView.findViewById(R.id.show_student_head);
            holder.name = (TextView) convertView.findViewById(R.id.show_student_name);
            holder.stuClass = (TextView) convertView.findViewById(R.id.show_student_class);
            holder.date = (TextView) convertView.findViewById(R.id.show_student_date);
            holder.talking = (TextView) convertView.findViewById(R.id.show_student_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getvName());
        holder.talking.setText(list.get(position).getContent());
        holder.stuClass.setText("行政 " + list.get(position).getvClassID() + " 班");
        holder.date.setText(list.get(position).getDate().substring(0, 16));

        try {
            holder.ueadHead.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(list.get(position).getvID() + ".jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ueadHead;
        TextView name, stuClass, date, talking;
    }
}
