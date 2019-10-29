package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.SwitchInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<SwitchInfoBean> arrgrids;

    public GridViewAdapter(Context context, List<SwitchInfoBean> arrgrids) {
        this.context = context;
        this.arrgrids = arrgrids;
    }

    @Override
    public int getCount() {
        return arrgrids == null ? 0 : arrgrids.size();
    }

    @Override
    public Object getItem(int position) {
        return arrgrids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.device_items, null);
            holder = new Holder();
            holder.img_device = convertView.findViewById(R.id.img_device);
            holder.text_device = convertView.findViewById(R.id.text_device);
            holder.text_room = convertView.findViewById(R.id.text_room);
            holder.img_xuanzhe = convertView.findViewById(R.id.img_xuanzhe);
            holder.ln_chose = convertView.findViewById(R.id.ln_chose);
           /*ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(lp);*/
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.img_device.setImageResource(arrgrids.get(position).getIcon());
        holder.text_device.setText(arrgrids.get(position).getCollector().getDeviceName());
        holder.text_room.setText(arrgrids.get(position).getName());
        if (arrgrids.get(position).isFlag()) {
            holder.img_xuanzhe.setImageDrawable(context.getDrawable(R.drawable.img_chose));
//            holder.img_xuanzhe.setVisibility(View.VISIBLE);
//            holder.ln_chose.setBackgroundResource(R.drawable.fuxuankuang);
        } else {
            holder.img_xuanzhe.setImageDrawable(context.getDrawable(R.drawable.img_unchose));
//            holder.img_xuanzhe.setVisibility(View.INVISIBLE);
//            holder.ln_chose.setBackgroundResource(R.drawable.weixuanzhong);
        }
        return convertView;
    }

    private class Holder {
        private ImageView img_device;
        private TextView text_device;
        private TextView text_room;
        private ImageView img_xuanzhe;
        private RelativeLayout ln_chose;
    }
}