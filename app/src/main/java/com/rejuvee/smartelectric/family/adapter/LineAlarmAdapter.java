package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.RecordBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.util.List;

/**
 * Created by liuchengran on 2018/12/4.
 */
@Deprecated
public class LineAlarmAdapter extends BaseAdapter {
    private Context mContext;
    private List<RecordBean> mListData;

    public LineAlarmAdapter(Context context, List<RecordBean> recordBeanList) {
        mContext = context;
        mListData = recordBeanList;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public RecordBean getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.item_line_alarm, null);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.tvState = convertView.findViewById(R.id.tv_state);
            holder.tvCode = convertView.findViewById(R.id.tv_code);
            holder.tvDate = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecordBean recordBean = mListData.get(position);
        holder.tvTitle.setText(String.format("%s%s", mContext.getString(R.string.vs4), recordBean.name));
        holder.tvCode.setText(String.format("%s%s", mContext.getString(R.string.vs6), recordBean.code));
//        holder.tvState.setText(SwitchBean.getSwitchFaultState(mContext, recordBean.state));
        holder.tvState.setText(SwitchBean.getSwitchFaultState(mContext, getItem(position).state));
        holder.tvDate.setText(recordBean.time);
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvCode;
        TextView tvState;
        TextView tvDate;
    }
}
