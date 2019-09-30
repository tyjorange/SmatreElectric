package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.MyEleApplianceBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/11/5.
 */
public class EleapplianceAdapter extends BaseAdapter implements View.OnClickListener {

    private static final String TAG = "EleapplianceAdapter";
    private Context context;
    private ArrayList<MyEleApplianceBean> arreleappliance;
    //    private DialogTip dialogTip;
    private OnDeletEleListener mDeletEleListener;
    //    private int seletpositon;

    public EleapplianceAdapter(Context context, ArrayList<MyEleApplianceBean> arreleappliance, OnDeletEleListener mDeletEleListener) {
        this.context = context;
        this.arreleappliance = arreleappliance;
        this.mDeletEleListener = mDeletEleListener;
    }

    @Override
    public void onClick(View view) {
        MyEleApplianceBean eleApp = (MyEleApplianceBean) view.getTag();
        if (mDeletEleListener != null) {
            mDeletEleListener.onDelete(eleApp);
        }
    }

    @Override
    public int getCount() {
        return arreleappliance == null ? 0 : arreleappliance.size();
    }

    @Override
    public Object getItem(int position) {
        return arreleappliance.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MyHolder myHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_ele, null);
            myHolder = new MyHolder();
            myHolder.txt_linename = (TextView) convertView.findViewById(R.id.txt_linename);
            myHolder.txt_elename = (TextView) convertView.findViewById(R.id.txt_elename);
            myHolder.txt_gonglv = (TextView) convertView.findViewById(R.id.txt_gonglv);
            myHolder.img_shanchu = (ImageView) convertView.findViewById(R.id.img_shanchu);
            myHolder.img_line = (ImageView) convertView.findViewById(R.id.img_line);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        MyEleApplianceBean myEleApplianceBean = arreleappliance.get(position);
//        seletpositon = position;
        myHolder.txt_linename.setText(myEleApplianceBean.getLinename());
        myHolder.txt_elename.setText(myEleApplianceBean.getElename());
        myHolder.txt_gonglv.setText(String.format("%s", myEleApplianceBean.getGonglv()));
        myHolder.img_shanchu.setVisibility(myEleApplianceBean.showDelIcon);
        myHolder.img_shanchu.setOnClickListener(this);
        myHolder.img_shanchu.setTag(myEleApplianceBean);
        return convertView;
    }

    private class MyHolder {
        private TextView txt_linename;
        private TextView txt_elename;
        private TextView txt_gonglv;
        private ImageView img_shanchu;
        private ImageView img_line;
    }

    public interface OnDeletEleListener {
        void onDelete(MyEleApplianceBean MyEleApplianceBean);
    }
}


