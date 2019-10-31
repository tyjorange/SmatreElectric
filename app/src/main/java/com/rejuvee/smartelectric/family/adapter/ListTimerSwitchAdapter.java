package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.util.List;

@Deprecated
public class ListTimerSwitchAdapter extends BaseAdapter {
    private String TAG = "ListTimerSwitchAdapter";
    private List<SwitchBean> mListData;
    private Context mContext;
    private MyListener mListener;

    public ListTimerSwitchAdapter(Context context, List<SwitchBean> listData, MyListener listener) {
        mContext = context;
        mListData = listData;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_timer_switch, null);
            holder = new ViewHolder();
            holder.img_line = convertView.findViewById(R.id.img_line);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_code = convertView.findViewById(R.id.tv_code);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SwitchBean switchBean = mListData.get(position);
        holder.img_line.setImageResource(switchBean.getIcon());
        holder.tv_name.setText(switchBean.getName());
        holder.tv_code.setText(switchBean.getSerialNumber());
        convertView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClickItem(switchBean.getSwitchID());
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_line;
        TextView tv_name;
        TextView tv_code;
    }

    public interface MyListener {
        void onClickItem(int switchId);
    }
}
