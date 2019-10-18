package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;

import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private List<Item> list;
    private Context context;

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.list = items;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_auto_link, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvDbm = (TextView) convertView.findViewById(R.id.dbm);
        Item item = list.get(position);
        tvName.setText(item.getName());
        tvDbm.setText(String.valueOf(item.getDbm()));
        return convertView;
    }

}
