package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchengran on 2017/12/19.
 */
public class CustomLineAdapter extends BaseAdapter {
    //    public static class Line {
//        public String name;
//        public int picRes;
//    }
    private int currentSelected = -1;
    private List<Item> mListData = new ArrayList<>();
    private Context mContext;
//    private boolean isShowName = true;

    public CustomLineAdapter(Context context) {
        mContext = context;

        for (int i = 0; i < NativeLine.LinePictures.length; i++) {
            Item line = new Item();
            line.chose = 0;
            line.img = NativeLine.LinePictures[i];
            line.name = NativeLine.LineNames[i];
            mListData.add(line);
        }
    }

    /**
     * 取消现有选择
     */
    public void reset() {
        for (Item i : mListData) {
            i.chose = 0;
        }
//        notifyDataSetChanged();
    }

    public Item setCurrentSelected(int position) {
        currentSelected = position;
        Item item = mListData.get(currentSelected);
        item.chose = 1;
        notifyDataSetChanged();
        return item;
    }

//    public int getCurrentSelected() {
//        return currentSelected;
//    }

//    public Item getSelected() {
//        if (currentSelected == -1) {
//            return null;
//        }
//        return mListData.get(currentSelected);
//    }

    public int getSelectedPosition() {
        return currentSelected;
    }

//    public void setShowName(boolean show) {
//        isShowName = show;
//        notifyDataSetChanged();
//    }

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
        Item line = mListData.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_gv_breaker, null);
            viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
            viewHolder.imgChose = convertView.findViewById(R.id.img_chose);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
//            viewHolder.llBreaker = (LinearLayout) convertView.findViewById(R.id.ll_breaker);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivPicture.setImageResource(line.img);
        viewHolder.imgChose.setImageResource(mListData.get(position).chose == 1 ? R.drawable.img_chose : R.drawable.img_unchose);
        viewHolder.tvName.setText(line.name);
//        if (currentSelected == position) {
//            viewHolder.llBreaker.setBackgroundResource(R.drawable.fuxuankuang);
//        } else {
//            viewHolder.llBreaker.setBackgroundResource(R.drawable.weixuanzhong);
//        }
//        viewHolder.tvName.setVisibility(isShowName ? View.VISIBLE : View.GONE);

        return convertView;
    }

    class ViewHolder {
        ImageView ivPicture;
        ImageView imgChose;
        TextView tvName;
//        LinearLayout llBreaker;
    }

    public class Item {
        int img;
        int chose;// 1 选中 0 未选中
        public String name;

//        Item(int img, int chose) {
//            this.img = img;
//            this.chose = chose;
//        }
    }
}
