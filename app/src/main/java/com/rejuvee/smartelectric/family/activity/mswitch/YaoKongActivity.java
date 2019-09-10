package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;

import java.util.ArrayList;
import java.util.List;

public class YaoKongActivity extends BaseActivity {
    // 集中器 collector
    private CollectorBean mCollectorBean;
    private LoadingDlg waitDialog;
    private List<SwitchBean> mListData = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_yao_kong;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mCollectorBean = getIntent().getParcelableExtra("collectorBean");
        waitDialog = new LoadingDlg(this, -1);
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ListView lvProduct = (ListView) findViewById(R.id.lv_products);
        adapter = new MyAdapter(this, mListData);
        lvProduct.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        getSwitchByCollector();
    }

    /**
     * 获取集中器下的线路(switch)
     */
    private void getSwitchByCollector() {
        waitDialog.show();
        Core.instance(this).getSwitchByCollector(mCollectorBean.getCode(), "hierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                mListData.clear();
                mListData.addAll(data);
                adapter.notifyDataSetChanged();
                // 隐藏空提示界面
//                findViewById(R.id.ll_empty_layout).setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);

//                mHandler.sendEmptyMessageDelayed(MSG_FILLDATA, 10);
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                String messageTip = "";
//                if (errorEvent == 12) {
//                    messageTip = getString(R.string.local_error_message_no_data);
//                } else {
//                    messageTip = getString(R.string.operator_failure);
//                }
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(YaoKongActivity.this, "请先添加线路");
                } else {
                    CustomToast.showCustomErrorToast(YaoKongActivity.this, message);
                }
                // 显示空提示界面
//                findViewById(R.id.ll_empty_layout).setVisibility(View.VISIBLE);
//                linearLayout.setVisibility(View.GONE);

                waitDialog.dismiss();
//                refreshLayout.setRefreshing(false);
            }
        });
        // 初始化线路状态
//        Core.instance(YaoKongActivity.this).getAllSwitchState(mCollectorBean.getCode(), new ActionCallbackListener<CollectorState>() {
//            @Override
//            public void onSuccess(CollectorState data) {
//                switchState = data.getSwitchState();
//            }
//
//            @Override
//            public void onFailure(int errorEvent, String message) {
//                CustomToast.showCustomErrorToast(YaoKongActivity.this, message);
//            }
//        });
    }

    @Override
    protected void dealloc() {

    }

    public class MyAdapter extends BaseAdapter {
        private Context context;
        private List<SwitchBean> list;

        MyAdapter(Context context, List<SwitchBean> list) {
            super();
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public SwitchBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_test0, parent, false);
                holder = new ViewHolder();
                holder.img_line = (ImageView) convertView.findViewById(R.id.img_line);
                holder.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
                holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
                holder.iv_time_clock = (ImageView) convertView.findViewById(R.id.iv_time_clock);
                holder.img_right = (ImageView) convertView.findViewById(R.id.img_right);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SwitchBean switchBean = getItem(position);
            holder.img_line.setImageResource(switchBean.getIcon());
            holder.txt_content.setText(switchBean.getName());
            holder.tv_state.setVisibility(switchBean.getSwitchState() == 0 ? View.VISIBLE : View.INVISIBLE);// 更新状态文字
            holder.tv_state.setText(SwitchBean.getSwitchFaultState(convertView.getContext(), switchBean.fault));// 更新状态文字
            holder.tv_code.setText(switchBean.getSerialNumber());
            holder.iv_time_clock.setVisibility(switchBean.timerCount > 0 ? View.VISIBLE : View.INVISIBLE);

            if (switchBean.getChild() != null) {
                holder.img_right.setVisibility(View.VISIBLE);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, YaoKongDetailActivity.class);
//                        ArrayList<SwitchBean> arrayList = new ArrayList<>(switchBean.getChild());
//                        HashMap<SwitchBean, List<SwitchBean>> datas = new HashMap<SwitchBean, List<SwitchBean>>();
//                        datas.put(switchBean, switchBean.getChild());

                        intent.putExtra("collectorBean", mCollectorBean);
                        intent.putExtra("SwitchBean", switchBean);
//                        intent.putExtra("datas", datas);
                        startActivity(intent);
                    }
                });
            }
            return convertView;
        }

        private class ViewHolder {
            private ImageView img_line;
            private TextView txt_content;
            private TextView tv_state;
            private TextView tv_code;
            private ImageView iv_time_clock;
            private ImageView img_right;
        }
    }
}
