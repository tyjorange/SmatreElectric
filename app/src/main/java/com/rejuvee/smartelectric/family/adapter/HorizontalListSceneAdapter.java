package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;

import java.util.List;

/**
 * main page 场景Adapter
 * Created by liuchengran on 2018/1/2.
 */
public class HorizontalListSceneAdapter extends BaseAdapter {
    private List<SceneBean> mListData;
    private Context mContext;

    public HorizontalListSceneAdapter(Context context, List<SceneBean> listData) {
        mContext = context;
        mListData = listData;
    }

    @Override
    public int getCount() {
//        return mListData == null ? 1 : mListData.size() + 1;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_easy_scene, null);
            holder = new ViewHolder();
            holder.imgSceneIcon = convertView.findViewById(R.id.img_scene);
            holder.txtSceneName = convertView.findViewById(R.id.txt_scene_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (mListData == null || position == mListData.size()) {//最后一个
//            holder.imgSceneIcon.setImageResource(R.drawable.gengduo_cj);
//            holder.txtSceneName.setText(mContext.getString(R.string.sceneBean));
//        } else {
        SceneBean sceneBean = mListData.get(position);
        holder.imgSceneIcon.setImageResource(NativeLine.imageId[sceneBean.getSceneIconRes()]);
        holder.txtSceneName.setText(spSceneName(sceneBean.getSceneName()));
//        }
        return convertView;
    }

    private String spSceneName(String name) {
        if (name.length() > 5) {
            name = name.substring(0, 5) + "...";
        }
        return name;
    }

    class ViewHolder {
        ImageView imgSceneIcon;
        TextView txtSceneName;
    }
}
