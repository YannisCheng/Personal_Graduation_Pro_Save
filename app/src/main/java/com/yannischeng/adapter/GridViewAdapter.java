package com.yannischeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.yannischeng.R;


/**
 * GridView 设配器
 * Created by yannischeng on 2016/12/3.
 */

public class GridViewAdapter extends BaseAdapter {
    private String[] strs;
    private Context context;

    public GridViewAdapter(String[] strs, Context context) {
        this.strs = strs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return strs.length;
    }

    @Override
    public Object getItem(int position) {
        return strs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.tool_item_grid_view_layout, null);
            holder.textView = (Button) convertView.findViewById(R.id.item_grid_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(strs[position]);

        return convertView;
    }

    class ViewHolder {
        Button textView;
    }
}
