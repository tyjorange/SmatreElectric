package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.util.List;

public class ReportWarnAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReportDetailBean.Warn> mListData;

    public ReportWarnAdapter(Context context, List<ReportDetailBean.Warn> listData) {
        mContext = context;
        mListData = listData;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public ReportDetailBean.Warn getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.item_report_warn, null);
            holder = new ViewHolder();
            holder.tv_line_name = (TextView) convertView.findViewById(R.id.tv_line_name);
            holder.tv_warn_type = (TextView) convertView.findViewById(R.id.tv_warn_type);
            holder.tv_warn_count = (TextView) convertView.findViewById(R.id.tv_warn_count);
            holder.tv_line_id = (TextView) convertView.findViewById(R.id.tv_line_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_line_name.setText("线路:" + getItem(position).getSwitchName());
        holder.tv_warn_type.setText(SwitchBean.getParamName(mContext, getItem(position).getErrType()));
        holder.tv_warn_count.setText(getItem(position).getCount() + "次");
        holder.tv_line_id.setText("ID:" + getItem(position).getSwitchID());
        return convertView;
    }

    class ViewHolder {
        TextView tv_line_name;
        TextView tv_warn_type;
        TextView tv_warn_count;
        TextView tv_line_id;
    }
}
