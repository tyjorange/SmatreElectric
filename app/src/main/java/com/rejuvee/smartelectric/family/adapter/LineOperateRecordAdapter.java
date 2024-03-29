package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.RecordBean;

import java.util.List;
import java.util.Locale;

/**
 * Created by liuchengran on 2018/12/4.
 */
public class LineOperateRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<RecordBean> mListData;

    public LineOperateRecordAdapter(Context context, List<RecordBean> recordBeanList) {
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
            convertView = View.inflate(mContext, R.layout.item_line_record, null);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.tvState = convertView.findViewById(R.id.tv_state);
            holder.tvCode = convertView.findViewById(R.id.tv_code);
            holder.tvDate = convertView.findViewById(R.id.tv_time);
            holder.ivRight = convertView.findViewById(R.id.iv_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RecordBean recordBean = mListData.get(position);
        holder.tvTitle.setText(String.format("%s%s", mContext.getString(R.string.vs4), recordBean.name));
        holder.tvCode.setText(recordBean.type == 1 ? mContext.getString(R.string.fun_changjing) : mContext.getString(R.string.vs6) + recordBean.code);

        if (recordBean.type == -1) {//场景里面的开关
            String cmdState = (recordBean.cmdData == 1 ? mContext.getString(R.string.switch_on) : mContext.getString(R.string.switch_off));
            holder.tvState.setText(cmdState);
        } else {
            String source = "";//来源
            if (recordBean.source == 0) {
                source = mContext.getString(R.string.control);
            } else if (recordBean.source == 1) {
                source = mContext.getString(R.string.fun_dingshi);
            } else if (recordBean.source == 2) {
                source = mContext.getString(R.string.vs31);
            }
            String cmdState = String.format("%s ", recordBean.cmdData == 1 ? mContext.getString(R.string.switch_on) : mContext.getString(R.string.switch_off));

            String cmdResult = "";
            int resultColor = 0;
            if (recordBean.runCode == 0) {
                cmdResult = "";
                resultColor = mContext.getResources().getColor(R.color.text_base_color_333);
            } else if (recordBean.runCode == 1) {
                cmdResult = String.format("%s(%s)", mContext.getString(R.string.failure), mContext.getString(R.string.off_line));
                resultColor = mContext.getResources().getColor(R.color.red_light);
            } else if (recordBean.runCode == 2) {
                resultColor = mContext.getResources().getColor(R.color.green_light);
                if (recordBean.runResult == 0) {
                    cmdResult = mContext.getString(R.string.sucess);
                } else if (recordBean.runResult == 34) {
                    resultColor = mContext.getResources().getColor(R.color.blue);
                    cmdResult = mContext.getString(R.string.vs32);
                } else {
                    resultColor = mContext.getResources().getColor(R.color.red_light);
                    cmdResult = (String.format(Locale.getDefault(), "%s(%d)", mContext.getString(R.string.failure), recordBean.runResult));
                }
            } else if (recordBean.runCode == 3) {
                resultColor = mContext.getResources().getColor(R.color.red_light);
                cmdResult = String.format("%s(%s)", mContext.getString(R.string.failure), mContext.getString(R.string.vs33));
            } else {
                resultColor = mContext.getResources().getColor(R.color.red_light);
            }

            SpannableString spannableString = new SpannableString(source + cmdState + cmdResult);
            spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_base_color_333)),
                    0, source.length() + cmdState.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

            spannableString.setSpan(new ForegroundColorSpan(resultColor),
                    source.length() + cmdState.length(), source.length() + cmdState.length() + cmdResult.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

            holder.tvState.setText(spannableString);
        }

        holder.tvState.setVisibility(recordBean.type == 1 ? View.GONE : View.VISIBLE);
//        String s = recordBean.username + recordBean.nickName;
        String sName = recordBean.username;
        if (sName != null) {
            holder.tvDate.setText(String.format("%s%s | %s", mContext.getString(R.string.vs7), sName, recordBean.time));
        } else {
            holder.tvDate.setText(recordBean.time);
        }
        holder.ivRight.setVisibility(recordBean.type == 1 ? View.VISIBLE : View.GONE);
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvCode;
        TextView tvState;
        TextView tvDate;

        ImageView ivRight;
    }
}
