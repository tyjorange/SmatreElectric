package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.ExpandLayout;

import java.util.List;

public class YaoKongDetailActivity extends BaseActivity {
    private SwitchBean switchBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_yao_kong_detail;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        switchBean = getIntent().getParcelableExtra("SwitchBean");
//        HashMap<SwitchBean, List<SwitchBean>> datas = getIntent().getParcelableExtra("datas");
//        System.out.println(datas);
//        ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
//        for (int i = 1; i <= 10; i++) {
//            HashMap<String, String> item = new HashMap<String, String>();
//            item.put("phoneType", "HTC-M" + i + "");
//            item.put("discount", "9");
//            item.put("price", (2000 + i) + "");
//            item.put("time", "2016020" + i);
//            item.put("num", (300 - i) + "");
//            datas.add(item);
//        }

        ListView lvProduct = (ListView) findViewById(R.id.lv_products);
        OneExpandAdapter adapter = new OneExpandAdapter(this, switchBean.getChild());
        lvProduct.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    private void doS() {
//        for (SwitchBean c : mListData) {
//
//        }
    }

    @Override
    protected void dealloc() {

    }

    /**
     * 点击item展开隐藏部分,再次点击收起
     * 只可展开一条记录
     *
     * @author WangJ
     * @date 2016.01.31
     */
    public class OneExpandAdapter extends BaseAdapter {
        private Context context;
        private List<SwitchBean> list;
        private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心

        OneExpandAdapter(Context context, List<SwitchBean> list) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_test1, parent, false);
                holder = new ViewHolder();
                holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
                holder.img_line = (ImageView) convertView.findViewById(R.id.img_line);
                holder.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
                holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
                holder.iv_time_clock = (ImageView) convertView.findViewById(R.id.iv_time_clock);
                holder.toggle_icon = (ImageView) convertView.findViewById(R.id.toggle_icon);
                holder.hideArea = (ExpandLayout) convertView.findViewById(R.id.layout_hideArea);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
            holder.showArea.setTag(position);

            SwitchBean currentSwitchBean = getItem(position);
            holder.img_line.setImageResource(currentSwitchBean.getIcon());
            holder.txt_content.setText(currentSwitchBean.getName());
            holder.tv_state.setVisibility(currentSwitchBean.getSwitchState() == 0 ? View.VISIBLE : View.GONE);// 更新状态文字
            holder.tv_state.setText(SwitchBean.getSwitchFaultState(convertView.getContext(), currentSwitchBean.fault));// 更新状态文字
            holder.tv_code.setText(currentSwitchBean.getSerialNumber());
            holder.iv_time_clock.setVisibility(currentSwitchBean.timerCount > 0 ? View.VISIBLE : View.GONE);

            List<SwitchBean> child = currentSwitchBean.getChild();
            if (child != null) {
                holder.hideArea.removeAllViews();//清除重绘
                for (SwitchBean s : child) {
                    View inflate1 = LayoutInflater.from(context).inflate(R.layout.item_test2, parent, false);
                    ViewHolder holder1 = new ViewHolder();
                    holder1.img_line = (ImageView) inflate1.findViewById(R.id.img_line);
                    holder1.txt_content = (TextView) inflate1.findViewById(R.id.txt_content);
                    holder1.tv_state = (TextView) inflate1.findViewById(R.id.tv_state);
                    holder1.tv_code = (TextView) inflate1.findViewById(R.id.tv_code);
                    holder1.iv_time_clock = (ImageView) inflate1.findViewById(R.id.iv_time_clock);

                    holder1.img_line.setImageResource(s.getIcon());
                    holder1.txt_content.setText(s.getName());
                    holder1.tv_state.setVisibility(s.getSwitchState() == 0 ? View.VISIBLE : View.GONE);// 更新状态文字
                    holder1.tv_state.setText(SwitchBean.getSwitchFaultState(convertView.getContext(), s.fault));// 更新状态文字
                    holder1.tv_code.setText(s.getSerialNumber());
                    holder1.iv_time_clock.setVisibility(s.timerCount > 0 ? View.VISIBLE : View.GONE);

                    holder.hideArea.addView(inflate1);
                }
            }
            if (child == null) {
                holder.toggle_icon.setVisibility(View.INVISIBLE);
            }
            //切换收缩按钮
            if (currentItem != position) {
                // 其他node 如果展开就收起
                if (holder.hideArea.isExpand()) {
                    holder.hideArea.collapse();
                    holder.toggle_icon.setImageResource(R.drawable.toggle_down);
                }
            } else {
                // 当前点击的node 根据情况变化
                if (holder.hideArea.isExpand()) {
                    holder.toggle_icon.setImageResource(R.drawable.toggle_up);
                } else {
                    holder.toggle_icon.setImageResource(R.drawable.toggle_down);
                }
            }
            ViewHolder finalHolder = holder;
            // 点击事件
            holder.showArea.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //用 currentItem 记录点击位置
                    currentItem = (int) (Integer) view.getTag();
                    finalHolder.hideArea.toggleExpand();
                    //通知adapter数据改变需要重新加载 必须有的一步
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private LinearLayout showArea;

            private ImageView img_line;
            private TextView txt_content;
            private TextView tv_state;
            private TextView tv_code;
            private ImageView iv_time_clock;
            private ImageView toggle_icon;

            private ExpandLayout hideArea;
        }
    }
}
