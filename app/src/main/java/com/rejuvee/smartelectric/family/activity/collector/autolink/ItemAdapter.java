package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;

import java.util.List;
import java.util.Locale;

public class ItemAdapter extends BaseAdapter {
    private List<Item> list;
    private Context context;

    ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.list = items;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_auto_link, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvDbm = (TextView) convertView.findViewById(R.id.dbm);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Item item = list.get(position);
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvDbm.setText(String.format(Locale.getDefault(), context.getString(R.string.vs251), item.getDbm()));
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvDbm;
    }
}
