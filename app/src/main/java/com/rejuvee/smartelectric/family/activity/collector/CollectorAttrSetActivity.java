package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;


/**
 * 集中器参数设置
 * Created by Administrator on 2018/1/2.
 */
public class CollectorAttrSetActivity extends BaseActivity implements View.OnClickListener {
    private String title;
    private EditText edit_devicename;
    private TextView tv_attr_title;
    private int Acposition;
    private String settext;
    private Countsadapter countsadapter;
    private ListView list_count;
    private int curposition = -1;
    private String collectorID;
    private String partext;
    private Intent intent;

    private static final String[] setBtlv = {
            "2400", "4800", "9600",
    };
    private static final String[] setAcquifrequency = {
            "10", "15", "20",
    };
    private static final String[] setAlarmthreshold = {
            "10", "15", "20",
    };
    private static final String[] setGuzhangma = {
            "10", "15", "20", "25", "30",
    };
    private static final String[] setheartrate = {
            "90", "120", "150",
    };

    @Override
    protected int getLayoutResId() {
        Acposition = getIntent().getIntExtra("position", 1);
        if (Acposition == 0) {
            return R.layout.activity_setupname;
        } else {
            return R.layout.activity_setupextra;
        }
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        intent = getIntent();
        collectorID = intent.getStringExtra("CollectorID");
        partext = intent.getStringExtra("partext");
        title = intent.getStringExtra("title");

        edit_devicename = findViewById(R.id.edit_devicename);
        tv_attr_title = findViewById(R.id.tv_attr_title);
        if (tv_attr_title != null) {
            tv_attr_title.setText(title);
        }
        if (edit_devicename != null)
            edit_devicename.setText(partext);
        if (Acposition != 0) {
            list_count = findViewById(R.id.list_count);
            switch (Acposition) {
                case 1:
                    isslecet(setBtlv);
                    countsadapter = new Countsadapter(setBtlv);
                    list_count.setAdapter(countsadapter);
                    break;
                case 2:
                    isslecet(setAcquifrequency);
                    countsadapter = new Countsadapter(setAcquifrequency);
                    list_count.setAdapter(countsadapter);
                    break;
                case 3:
                    isslecet(setAlarmthreshold);
                    countsadapter = new Countsadapter(setAlarmthreshold);
                    list_count.setAdapter(countsadapter);
                    break;
                case 4:
                    isslecet(setGuzhangma);
                    countsadapter = new Countsadapter(setGuzhangma);
                    list_count.setAdapter(countsadapter);
                    break;
                case 5:
                    isslecet(setheartrate);
                    countsadapter = new Countsadapter(setheartrate);
                    list_count.setAdapter(countsadapter);
                    break;
            }

            list_count.setOnItemClickListener((parent, view, position, id) -> {
                switch (Acposition) {
                    case 1:
                        settext = setBtlv[position];
                        break;
                    case 2:
                        settext = setAcquifrequency[position];
                        break;
                    case 3:
                        settext = setAlarmthreshold[position];
                        break;
                    case 4:
                        settext = setGuzhangma[position];
                        break;
                    case 5:
                        settext = setheartrate[position];
                        break;
                }
                curposition = position;
                countsadapter.notifyDataSetChanged();
            });
        }
        findViewById(R.id.img_cancel).setOnClickListener(this);
        findViewById(R.id.bu_wancheng).setOnClickListener(this);
    }

    public void isslecet(String[] setBtlv) {
        for (int i = 0; i < setBtlv.length; i++) {
            if (setBtlv[i].equals(partext)) {
                curposition = i;
            }
        }
    }

    @Override
    protected void initData() {

    }

//    @Override
//    protected String getToolbarTitle() {
//
//        return null;
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {
      /*
       ;*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                finish();
                break;
            case R.id.bu_wancheng:
                Log.i("collectorID", collectorID + "");
                switch (Acposition) {
                    case 0:
                        settext = edit_devicename.getText().toString();
                        if (settext.isEmpty() | settext.length() > 12) {
                            CustomToast.showCustomErrorToast(this, getString(R.string.vs179));
                            break;
                        }
                        changeCollector(collectorID, settext, null, null, null, null, null);
                        break;
                    case 1:
                        changeCollector(collectorID, null, settext, null, null, null, null);
                        break;
                    case 2:
                        changeCollector(collectorID, null, null, settext, null, null, null);
                        break;
                    case 3:
                        changeCollector(collectorID, null, null, null, settext, null, null);
                        break;
                    case 4:
                        changeCollector(collectorID, null, null, null, null, settext, null);
                        break;
                    case 5:
                        changeCollector(collectorID, null, null, null, null, null, settext);
                        break;
                }
                break;
        }
    }

    public void changeCollector(String collectorID, String name, String baud, String freq, String ranges, String faultFreq, String HBFreq) {
        Core.instance(this).updateCollectorParam(collectorID, name, baud, freq, ranges, faultFreq, HBFreq, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(CollectorAttrSetActivity.this, R.string.modify_succe, Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
             /*   intent.putExtra("settext", settext);
                intent.putExtra("Acposition", Acposition);*/

                setResult(RESULT_OK, intent);
                CollectorAttrSetActivity.this.finish();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Toast.makeText(CollectorAttrSetActivity.this, R.string.modify_fail + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    class Countsadapter extends BaseAdapter {
        private String[] counts;

        public Countsadapter(String[] counts) {
            this.counts = counts;
        }

        @Override
        public int getCount() {
            return counts.length;
        }

        @Override
        public Object getItem(int position) {
            return counts[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void notifyDataSetChanged(int position) {
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(CollectorAttrSetActivity.this, R.layout.setcounts_items, null);
                holder = new Holder();
                holder.text_counts = convertView.findViewById(R.id.text_counts);
                holder.img_slecte = convertView.findViewById(R.id.img_slecte);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            switch (Acposition) {
                case 1:
                    holder.text_counts.setText(String.format("%sbps", counts[position]));
                    break;
                case 2:
                    holder.text_counts.setText(String.format("%smin", counts[position]));
                    break;
                case 3:
                    holder.text_counts.setText(String.format("%s%%", counts[position]));
                    break;
                case 4:
                    holder.text_counts.setText(String.format("%ssec", counts[position]));
                    break;
                case 5:
                    holder.text_counts.setText(String.format("%ss", counts[position]));
                    break;
            }
//            holder.img_slecte.setVisibility(curposition != position ? View.INVISIBLE : View.VISIBLE);
            holder.img_slecte.setImageDrawable(curposition != position ? getDrawable(R.drawable.dx_unchose_slices) : getDrawable(R.drawable.dx_chose_slices));
            return convertView;
        }

        class Holder {
            TextView text_counts;
            ImageView img_slecte;
        }
    }
}
