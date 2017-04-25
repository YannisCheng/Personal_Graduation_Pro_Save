package com.yannischeng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yannischeng.R;

import java.util.List;

/**
 * 搜索历史适配器
 *
 * Created by 程文佳 on 17-2-14.
 */

public class SearchGridAdapter extends BaseAdapter {

    private static final String TAG = "SearchGridAdapter";
    private Context context;
    private List<String> strings;

    public SearchGridAdapter(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
        for (String item : strings) {
            Log.e(TAG, "SearchGridAdapter item is: " + item.toString());
        }
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int i) {
        return strings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_grid_layout, null);
            viewHolder.name = (TextView) view.findViewById(R.id.set_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(strings.get(i));
        return view;
    }

    class ViewHolder {
        TextView name;
    }
}
