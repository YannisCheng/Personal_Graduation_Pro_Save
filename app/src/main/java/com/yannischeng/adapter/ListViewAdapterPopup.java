package com.yannischeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yannischeng.R;


/**
 * listView popup
 *
 * Created by 程文佳 on 2016/11/8.
 */

public class ListViewAdapterPopup extends BaseAdapter {

    private Context context;
    private String[] strs = null;

    public ListViewAdapterPopup(Context contexts, String[] strings) {
        strs = strings;
        context = contexts;
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
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tool_item_list_view_popup_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.text_popup);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(strs[position]);
        return convertView;
    }

    class ViewHolder {
        TextView text;
    }
}
