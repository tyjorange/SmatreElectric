package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.rejuvee.smartelectric.family.R;

import java.util.ArrayList;

/**
 * 模块扫描到的SSID列表
 */
public class SsidListActivity extends Activity {
    private ArrayList<Item> ssids;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ssid_list);
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ssids = (ArrayList<Item>) getIntent().getSerializableExtra("ssids");
        lv = (ListView) findViewById(R.id.lv_ssid);
        ItemAdapter adapter = new ItemAdapter(this, ssids);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Item ssid = ssids.get(position);
                System.out.println("onClick-------------->ssid:" + ssid.getName() + " dbm:" + ssid.getDbm());
                Intent data = new Intent();
                data.putExtra("ssid", ssid.getName());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}
