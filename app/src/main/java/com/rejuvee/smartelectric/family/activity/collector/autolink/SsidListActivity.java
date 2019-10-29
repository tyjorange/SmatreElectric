package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 模块扫描到的SSID列表
 */
public class SsidListActivity extends Activity {
    private ArrayList<Item> ssids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ssid_list);
        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        ssids = getIntent().getParcelableArrayListExtra("ssids");
        ListView lv = findViewById(R.id.lv_ssid);
        if (ssids == null) {
            ssids = new ArrayList<>();
        }
        ItemAdapter adapter = new ItemAdapter(this, ssids);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Item ssid = ssids.get(position);
            System.out.println("onClick-------------->ssid:" + ssid.getName() + " dbm:" + ssid.getDbm());
            Intent data = new Intent();
            data.putExtra("ssid", ssid.getName());
            setResult(RESULT_OK, data);
            finish();
        });
    }

    private class ItemAdapter extends BaseAdapter {
        private List<Item> list;
        private Context context;

        ItemAdapter(Context context, List<Item> items) {
            this.context = context;
            this.list = items;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_auto_link, null);
                viewHolder.tvName = convertView.findViewById(R.id.name);
                viewHolder.tvDbm = convertView.findViewById(R.id.dbm);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Item item = list.get(position);
            viewHolder.tvName.setText(item.getName());
            viewHolder.tvDbm.setText(String.format(Locale.getDefault(), context.getString(R.string.vs251), item.getDbm()));
            return convertView;
        }

        class ViewHolder {
            TextView tvName;
            TextView tvDbm;
        }
    }
}
