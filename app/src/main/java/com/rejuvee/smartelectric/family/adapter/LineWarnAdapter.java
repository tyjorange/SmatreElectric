package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.WarnBean;

import java.util.List;

/**
 * Created by liuchengran on 2018/12/4.
 */
public class LineWarnAdapter extends BaseAdapter {
    private Context mContext;
    private List<WarnBean> mListData;

    public LineWarnAdapter(Context context, List<WarnBean> recordBeanList) {
        mContext = context;
        mListData = recordBeanList;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.item_line_warn, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tvCode = (TextView) convertView.findViewById(R.id.tv_code);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WarnBean warnBean = mListData.get(position);
        holder.tvTitle.setText("线路:" + warnBean.getName());
        holder.tvCode.setText("ID:" + (warnBean.getCode()));
        holder.tvType.setText(SwitchBean.getParamName(mContext, warnBean.getParamID()));
        holder.tvState.setText("值:" + warnBean.getWarningValue() + "/阀值:" + warnBean.getParamValue());
        holder.tvDate.setText(warnBean.getTime());
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvCode;
        TextView tvType;
        TextView tvState;
        TextView tvDate;
    }
}
