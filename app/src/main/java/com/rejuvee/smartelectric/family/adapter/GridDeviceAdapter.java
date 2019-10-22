package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;

import java.util.List;

/**
 * Created by liuchengran on 2018/1/2.
 */
public class GridDeviceAdapter extends BaseAdapter {
    private List<CollectorBean> mListData;
    private Context mContext;
    private boolean isEditMode = false;
    private MyListener mListener;

    public GridDeviceAdapter(List<CollectorBean> listData, Context context, MyListener listener) {
        mListData = listData;
        mContext = context;
        mListener = listener;
    }

    public void setEditMode(boolean edit) {
        isEditMode = edit;
        notifyDataSetChanged();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    @Override
    public int getCount() {
        return mListData == null ? 1 : mListData.size() + 1;//为了保留添加按钮
//        return mListData == null ? 0 : mListData.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_easy_device, null);
            holder = new ViewHolder();
            holder.imgDevice = (ImageView) convertView.findViewById(R.id.img_device);
            holder.txtDeviceName = (TextView) convertView.findViewById(R.id.txt_device_name);
            holder.imgOnline = (ImageView) convertView.findViewById(R.id.img_online);
            holder.ivDeviceType = (ImageView) convertView.findViewById(R.id.img_device_type);
            holder.ivJinru = (ImageView) convertView.findViewById(R.id.iv_jinru);
            holder.ivShare = (ImageView) convertView.findViewById(R.id.iv_share);
            holder.llRight = (LinearLayout) convertView.findViewById(R.id.ll_right);
            holder.ll_collector_item1 = convertView.findViewById(R.id.ll_collector_item);
            holder.ll_collector_item2 = convertView.findViewById(R.id.ll_collector_item2);
            holder.ivDel = (ImageView) convertView.findViewById(R.id.iv_del);
            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    if (mListener != null) {
                        mListener.onDel(pos);
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
//            ViewGroup.LayoutParams params = convertView.getLayoutParams();
//            params.height = params.width;
//            convertView.requestLayout();
        }
        boolean isShareFromOther = false;
        if (mListData == null || position == mListData.size()) {//添加
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.gravity = Gravity.CENTER;
//            holder.ll_collector_item.setLayoutParams(lp);
//            holder.imgDevice.setImageResource(R.drawable.pluse);
//            holder.txtDeviceName.setText(mContext.getString(R.string.add_device));
//            holder.ivShare.setVisibility(View.GONE);
//            holder.imgOnline.setVisibility(View.GONE);
//            holder.ivDeviceType.setVisibility(View.GONE);
//            holder.ivJinru.setVisibility(View.GONE);
            holder.ll_collector_item1.setVisibility(View.GONE);
            holder.ll_collector_item2.setVisibility(View.VISIBLE);
        } else {
            holder.ll_collector_item1.setVisibility(View.VISIBLE);
            holder.ll_collector_item2.setVisibility(View.GONE);
            CollectorBean collectorBean = mListData.get(position);
            holder.imgDevice.setImageResource(R.drawable.dianxiang_88);
            holder.txtDeviceName.setText(collectorBean.getDeviceName());
            holder.imgOnline.setSelected(collectorBean.getOnline() == 0);
            holder.imgOnline.setVisibility(View.VISIBLE);
            holder.ivDeviceType.setVisibility(View.VISIBLE);
            holder.ivJinru.setVisibility(View.VISIBLE);
            holder.ivShare.setVisibility(View.INVISIBLE);
            if (collectorBean.beShared == 1) {
                holder.ivShare.setVisibility(View.VISIBLE);
                holder.ivShare.setImageResource(R.drawable.gongxiangjinlai);
                isShareFromOther = true;
            } else {
                if (collectorBean.beShared == 0 && collectorBean.share > 0) {
                    holder.ivShare.setVisibility(View.VISIBLE);
                    holder.ivShare.setImageResource(R.drawable.gongxiangchuqu);
                }
            }
//            holder.tvDeviceType.setText(CollectorBean.getDeviceType(collectorBean.ioType));
            holder.ivDeviceType.setBackground(mContext.getResources().getDrawable(CollectorBean.getDeviceTypeImg(collectorBean.ioType)));
//            holder.ivDeviceType.setImageDrawable(mContext.getResources().getDrawable(CollectorBean.getDeviceTypeImg(collectorBean.ioType)));
//            Drawable leftDrawable = mContext.getResources().getDrawable(CollectorBean.getDeviceTypeImg(collectorBean.ioType));
//            leftDrawable.setBounds(0, 0, SizeUtils.dp2px(12), SizeUtils.dp2px(12));
//            holder.ivDeviceType.setCompoundDrawables(leftDrawable, null, null, null);
//            holder.tvDeviceType.setVisibility(collectorBean.ioType == 0 ? View.GONE : View.VISIBLE);
        }
        holder.ivDel.setVisibility(isEditMode && position != mListData.size() && !isShareFromOther ? View.VISIBLE : View.GONE);
        holder.llRight.setVisibility(isEditMode && position != mListData.size() ? View.GONE : View.VISIBLE);
//        if (isShareFromOther) {
//            holder.ivDel.setImageDrawable(mContext.getResources().getDrawable(R.drawable.reduce));
//            holder.ivDel.setVisibility(View.VISIBLE);
//        }
        holder.ivDel.setTag(position);
        return convertView;
    }

    class ViewHolder {
        ImageView imgDevice;
        ImageView imgOnline;
        ImageView ivDel;
        ImageView ivShare;
        TextView txtDeviceName;
        //        TextView tvDeviceType;
        ImageView ivDeviceType;
        ImageView ivJinru;
        LinearLayout llRight;
        LinearLayout ll_collector_item1;
        LinearLayout ll_collector_item2;
    }

    @Deprecated
    public interface MyListener {
        void onDel(int position);
    }
}
