package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.ReportBean;

import java.util.List;

public class ReportAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReportBean> mListData;

    public ReportAdapter(Context context, List<ReportBean> recordBeanList) {
        mContext = context;
        mListData = recordBeanList;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public ReportBean getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_line_report, null);
            holder = new ViewHolder();
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(getItem(position).timeType == 1 ? "周报" : "月报");
        holder.tv_time.setText(String.format("%s - %s", getItem(position).startDay, getItem(position).endDay));
        return convertView;
    }

    class ViewHolder {
        TextView tv_type;
        TextView tv_time;
    }
}
