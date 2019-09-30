package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.TimeTaskBean;

import java.util.List;

public class ListTimerAdapter extends BaseAdapter {
    private String TAG = "ListTimerSwitchAdapter";
    private List<TimeTaskBean.TimeTask> mListData;
    private Context mContext;
    private MyListener mListener;

    public ListTimerAdapter(Context context, List<TimeTaskBean.TimeTask> listData, MyListener listener) {
//        super(context);
        mContext = context;
        mListData = listData;
        mListener = listener;
//        setOnItemClickListener(new SwipeLayout.onItemClickListener() {
//            @Override
//            public void onItemClick(int position, SwipeLayout layout, boolean longPress) {
//                if (mListener != null) {
//                    mListener.onClick(position);
//                }
//            }
//        });
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_timer, null);
            holder = new ViewHolder();
            holder.img_item_remove = (ImageView) convertView.findViewById(R.id.img_item_remove);
            holder.img_switch = (ImageView) convertView.findViewById(R.id.img_switch);
            holder.img_zhuliu = (ImageView) convertView.findViewById(R.id.img_zhuliu);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_repet = (TextView) convertView.findViewById(R.id.tv_repet);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TimeTaskBean.TimeTask timeTask = mListData.get(position);
        holder.img_item_remove.setVisibility(timeTask.showDelIcon);
        holder.img_item_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDelItem(timeTask.taskId);
            }
        });
        holder.img_zhuliu.setVisibility(timeTask.upload == 1 ? View.VISIBLE : View.INVISIBLE);
        holder.img_switch.setImageResource(timeTask.enable == 1 ? R.drawable.shineng : R.drawable.jinzhi);
        holder.img_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEnable(position, timeTask.enable == 0);
            }
        });
        holder.tv_state.setText(timeTask.switchState == 1 ? mContext.getString(R.string.vs74) : mContext.getString(R.string.vs75));
        holder.tv_time.setText(timeTask.time);
        holder.tv_repet.setText(toRepeat(timeTask.repeatState));
//        holder.img_line.setImageResource(switchBean.getIcon());
//        holder.tv_name.setText(switchBean.getName());
//        holder.tv_code.setText(switchBean.getSerialNumber());
//        holder.cbEnable.setChecked(task.getEnable() == 1);
//        if (task.getRepeatState() != null) {
//            holder.txtRepeat.setText(toRepeat(task.getRepeatState()));
//        }
        return convertView;
    }

    private String toRepeat(int repeat) {
        if (repeat == 0 | repeat == 128) {
            return mContext.getString(R.string.timetask_once);
        }
        String[] weekDay = mContext.getResources().getStringArray(R.array.short_week_day);
        String week = mContext.getString(R.string.week);
        StringBuilder repeatToShow = new StringBuilder(week);
        int mask = 0x0000001;
        for (int i = 0; i < 7; i++) {
            if ((repeat & mask << i) != 0) {
                repeatToShow.append(weekDay[i]).append(",");
            }
        }
        repeatToShow = new StringBuilder(repeatToShow.substring(0, repeatToShow.lastIndexOf(",") == -1 ? 1 : repeatToShow.lastIndexOf(",")));
        return repeatToShow.toString();
    }

//    @Override
//    public int getContentResLayoutId() {
//        return R.layout.item_timed_task;
//    }

//    @Override
//    public void onDeleteItem(int position) {
//        if (mListener != null) {
//            mListener.onDelete(position);
//        }
//    }

//    @Override
//    protected void bindView(View convertView, final int position) {
//        TimeTaskBean.TimeTask task = mListData.get(position);
//
//        ViewHolder holder = (ViewHolder) convertView.getTag();
//        if (holder == null) {
//            holder = new ViewHolder();
//            holder.cbEnable = (CheckBox) convertView.findViewById(R.id.cb_swicth);
//            holder.txtRepeat = (TextView) convertView.findViewById(R.id.txt_repeat);
//            holder.txtSwitchState = (TextView) convertView.findViewById(R.id.txt_switch_state);
//            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
//            holder.ivUpload = (ImageView) convertView.findViewById(R.id.iv_upload);
//            convertView.setTag(holder);
//        }
//
//        String time = task.getTime();
//        /*if (time != null) {
//            int index = time.lastIndexOf(":00");
//            if (index != -1 && index < time.length()) {
//                time = time.substring(0, index);
//            }
//
//        }*/
//        holder.txtTime.setText(time);
//
//        holder.txtSwitchState.setText(task.getSwitchState() == 1 ?
//                mContext.getString(R.string.switch_on) : mContext.getString(R.string.switch_off));
//        holder.cbEnable.setChecked(task.getEnable() == 1);
//
//        if (task.getRepeatState() == 0) {
//            holder.txtRepeat.setText(mContext.getString(R.string.repeat_not));
//        } else {
//            holder.txtRepeat.setText(toRepeat(task.getRepeatState()));
//        }
//
//        holder.cbEnable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "CheckBox onClick");
//                if (mListener != null) {
//                    mListener.onEnable(position, ((CheckBox) v).isChecked());
//                }
//            }
//        });
//        holder.ivUpload.setVisibility(task.upload == 1 ? View.VISIBLE : View.GONE);
//    }

    class ViewHolder {
        ImageView img_item_remove;
        ImageView img_switch;
        ImageView img_zhuliu;
        TextView tv_state;
        TextView tv_time;
        TextView tv_repet;
    }

    public interface MyListener {
        void onDelItem(String TimerId);

        void onEnable(int position, final boolean enable);
    }
}
