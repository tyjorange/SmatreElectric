package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.SwitchInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ListViewLineSceneAdapter extends BaseAdapter {
    private Context context;
    private List<SwitchInfoBean> result;
    private int count;
    private String sceneid;


    public ListViewLineSceneAdapter(Context context, List<SwitchInfoBean> result, String sceneid) {
        this.context = context;
        this.result = result;
        this.sceneid = sceneid;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_scene_switch, null);
            holder = new Holder();
            holder.img_device = convertView.findViewById(R.id.img_device);
            holder.txt_device = convertView.findViewById(R.id.txt_device);
            holder.txt_room = convertView.findViewById(R.id.txt_room);
            holder.img_kaiguang = convertView.findViewById(R.id.img_kaiguang);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.img_device.setImageResource(NativeLine.LinePictures[result.get(position).getIconType() % NativeLine.LinePictures.length]);
        holder.txt_device.setText(String.format("%s%s", context.getString(R.string.vs5), result.get(position).getCollector().getDeviceName()));
        holder.txt_room.setText(String.format("%s%s", context.getString(R.string.vs4), result.get(position).getName()));
        if (result.get(position).getState() == 1) {
            holder.img_kaiguang.setImageResource(R.drawable.yk_hezha);
        } else
            holder.img_kaiguang.setImageResource(R.drawable.yk_kaizha);

        holder.img_kaiguang.setOnClickListener(v -> {
            int state = result.get(position).getState();
            result.get(position).setState(state == 0 ? 1 : 0);
            ListViewLineSceneAdapter.this.notifyDataSetChanged();
        });
        return convertView;
    }

    private class Holder {
        private ImageView img_device;
        private TextView txt_device;
        private TextView txt_room;
        private ImageView img_kaiguang;
    }
}
