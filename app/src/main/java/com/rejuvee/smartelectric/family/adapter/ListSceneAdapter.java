package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */
public class ListSceneAdapter extends BaseAdapter {
    private Context context;
    private List<SceneBean> result;
    private MyListener mListener;
    //    private SceneRealm sceneRealm;
//    private LoadingDlg loadingDlg;
//    private List<SwitchBean> javaListBreak = new ArrayList<>();

    public ListSceneAdapter(Context context, List<SceneBean> result, MyListener mListener) {
        this.context = context;
        this.result = result;
        this.mListener = mListener;
//        loadingDlg = new LoadingDlg(context, -1);
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
       /* sceneRealm = new SceneRealm();
        sceneRealm.open();*/
        MyHolder myHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_scene, null);
            myHolder = new MyHolder();
            myHolder.img_item_remove = convertView.findViewById(R.id.img_item_remove);
            myHolder.img_scen = convertView.findViewById(R.id.img_scen);
            myHolder.text_scenname = convertView.findViewById(R.id.text_scenname);
            myHolder.text_scenline = convertView.findViewById(R.id.text_scenline);
            myHolder.img_zhixing = convertView.findViewById(R.id.img_zhixing);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        final SceneBean sceneBean = result.get(position);
        myHolder.img_item_remove.setVisibility(sceneBean.showDelIcon);
        myHolder.img_item_remove.setOnClickListener(v -> mListener.onDelItem(sceneBean.getSceneId()));
        myHolder.img_scen.setImageResource(NativeLine.imageId[sceneBean.getSceneIconRes()]);
        myHolder.text_scenname.setText(sceneBean.getSceneName());
        myHolder.text_scenline.setText(String.format(context.getString(R.string.associated_lines), sceneBean.getCount()));
        myHolder.img_zhixing.setOnClickListener(v -> mListener.onExcuteItem(sceneBean.getSceneId(), position));
        return convertView;
    }

    private class MyHolder {
        private ImageView img_item_remove;
        private ImageView img_scen;
        private TextView text_scenname;
        private TextView text_scenline;
        private TextView img_zhixing;
    }

    public interface MyListener {
        void onDelItem(String sceneId);

        void onExcuteItem(String sceneId, int position);
    }
}
