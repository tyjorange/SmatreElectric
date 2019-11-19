package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.AttrSetBeanAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivitySetupextraBinding;
import com.rejuvee.smartelectric.family.databinding.ActivitySetupnameBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * 集中器参数设置
 * Created by Administrator on 2018/1/2.
 */
public class CollectorAttrSetActivity extends BaseActivity {
    //    private EditText edit_devicename;
//    private TextView tv_attr_title;
//    private ListView list_count;
//    private CountsAdapter countsadapter;
    private AttrSetBeanAdapter countsadapter;
    private int Acposition;
    private int currentPosition = -1;
    private String collectorID;
    private String setText;
    private String partext;
//    private Intent intent;

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
    private static final List<AttrItem> listBtlv = new ArrayList<>();
    private static final List<AttrItem> listAcquifrequency = new ArrayList<>();
    private static final List<AttrItem> listAlarmthreshold = new ArrayList<>();
    private static final List<AttrItem> listGuzhangma = new ArrayList<>();
    private static final List<AttrItem> listheartrate = new ArrayList<>();

    //    @Override
//    protected int getLayoutResId() {
//        Acposition = getIntent().getIntExtra("position", 1);
//        if (Acposition == 0) {
//            return R.layout.activity_setupname;
//        } else {
//            return R.layout.activity_setupextra;
//        }
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivitySetupnameBinding nameBinding;

    @Override
    protected void initView() {
        Acposition = getIntent().getIntExtra("position", 1);
        listBtlv.clear();
        for (String s : setBtlv) {
            listBtlv.add(new AttrItem(format(s)));
        }
        listAcquifrequency.clear();
        for (String s : setAcquifrequency) {
            listAcquifrequency.add(new AttrItem(format(s)));
        }
        listAlarmthreshold.clear();
        for (String s : setAlarmthreshold) {
            listAlarmthreshold.add(new AttrItem(format(s)));
        }
        listGuzhangma.clear();
        for (String s : setGuzhangma) {
            listGuzhangma.add(new AttrItem(format(s)));
        }
        listheartrate.clear();
        for (String s : setheartrate) {
            listheartrate.add(new AttrItem(format(s)));
        }
        collectorID = getIntent().getStringExtra("CollectorID");
        partext = getIntent().getStringExtra("partext");
        if (Acposition == 0) {
            nameBinding = DataBindingUtil.setContentView(this, R.layout.activity_setupname);
            nameBinding.setPresenter(new Presenter());
            nameBinding.setLifecycleOwner(this);
            nameBinding.editDevicename.setText(partext);
//            edit_devicename = findViewById(R.id.edit_devicename);
        } else {
            ActivitySetupextraBinding extraBinding = DataBindingUtil.setContentView(this, R.layout.activity_setupextra);
            extraBinding.setPresenter(new Presenter());
            extraBinding.setLifecycleOwner(this);
//            tv_attr_title = findViewById(R.id.tv_attr_title);
            String title = getIntent().getStringExtra("title");
            extraBinding.tvAttrTitle.setText(title);
//            list_count = findViewById(R.id.list_count);
            extraBinding.listCount.setLayoutManager(new LinearLayoutManager(this));
            switch (Acposition) {
                case 1:
                    isSeleceted(listBtlv);
//                    countsadapter = new CountsAdapter(setBtlv);
                    countsadapter = new AttrSetBeanAdapter(this, currentPosition);
                    extraBinding.listCount.setAdapter(countsadapter);
                    countsadapter.addAll(listBtlv);
                    break;
                case 2:
                    isSeleceted(listAcquifrequency);
//                    countsadapter = new CountsAdapter(setAcquifrequency);
                    countsadapter = new AttrSetBeanAdapter(this, currentPosition);
                    extraBinding.listCount.setAdapter(countsadapter);
                    countsadapter.addAll(listAcquifrequency);
                    break;
                case 3:
                    isSeleceted(listAlarmthreshold);
//                    countsadapter = new CountsAdapter(setAlarmthreshold);
                    countsadapter = new AttrSetBeanAdapter(this, currentPosition);
                    extraBinding.listCount.setAdapter(countsadapter);
                    countsadapter.addAll(listAlarmthreshold);
                    break;
                case 4:
                    isSeleceted(listGuzhangma);
//                    countsadapter = new CountsAdapter(setGuzhangma);
                    countsadapter = new AttrSetBeanAdapter(this, currentPosition);
                    extraBinding.listCount.setAdapter(countsadapter);
                    countsadapter.addAll(listGuzhangma);
                    break;
                case 5:
                    isSeleceted(listheartrate);
//                    countsadapter = new CountsAdapter(setheartrate);
                    countsadapter = new AttrSetBeanAdapter(this, currentPosition);
                    extraBinding.listCount.setAdapter(countsadapter);
                    countsadapter.addAll(listheartrate);
                    break;
            }
            countsadapter.setCallback(position -> {
                switch (Acposition) {
                    case 1:
                        setText = setBtlv[position];
                        break;
                    case 2:
                        setText = setAcquifrequency[position];
                        break;
                    case 3:
                        setText = setAlarmthreshold[position];
                        break;
                    case 4:
                        setText = setGuzhangma[position];
                        break;
                    case 5:
                        setText = setheartrate[position];
                        break;
                }
                countsadapter.setCurrentPosition(position);
                countsadapter.notifyDataSetChanged();
            });
//            extraBinding.listCount.setOnItemClickListener((parent, view, position, id) -> {
//                switch (Acposition) {
//                    case 1:
//                        setText = setBtlv[position];
//                        break;
//                    case 2:
//                        setText = setAcquifrequency[position];
//                        break;
//                    case 3:
//                        setText = setAlarmthreshold[position];
//                        break;
//                    case 4:
//                        setText = setGuzhangma[position];
//                        break;
//                    case 5:
//                        setText = setheartrate[position];
//                        break;
//                }
//                curposition = position;
//                countsadapter.notifyDataSetChanged();
//            });
        }
//        findViewById(R.id.img_cancel).setOnClickListener(this);
//        findViewById(R.id.bu_wancheng).setOnClickListener(this);
    }

    public void isSeleceted(List<AttrItem> items) {
        for (int i = 0; i < items.size(); i++) {
            if (partext.contains(items.get(i).getAttr())) {
                currentPosition = i;
            }
        }
//        for (int i = 0; i < setBtlv.length; i++) {
//            if (setBtlv[i].equals(partext)) {
//                currentPosition = i;
//            }
//        }
    }

    private String format(String s) {
        switch (Acposition) {
            case 1:
                return (String.format("%sbps", s));
            case 2:
                return (String.format("%smin", s));
            case 3:
                return (String.format("%s%%", s));
            case 4:
                return (String.format("%ssec", s));
            case 5:
                return (String.format("%ss", s));
        }
        return s;
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
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onComplete(View view) {
            Log.i("collectorID", collectorID + "");
            switch (Acposition) {
                case 0:
                    setText = nameBinding.editDevicename.getText().toString();
                    if (setText.isEmpty() | setText.length() > 12) {
                        CustomToast.showCustomErrorToast(view.getContext(), getString(R.string.vs179));
                        break;
                    }
                    changeCollector(collectorID, setText, null, null, null, null, null);
                    break;
                case 1:
                    changeCollector(collectorID, null, setText, null, null, null, null);
                    break;
                case 2:
                    changeCollector(collectorID, null, null, setText, null, null, null);
                    break;
                case 3:
                    changeCollector(collectorID, null, null, null, setText, null, null);
                    break;
                case 4:
                    changeCollector(collectorID, null, null, null, null, setText, null);
                    break;
                case 5:
                    changeCollector(collectorID, null, null, null, null, null, setText);
                    break;
            }
        }
    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_cancel:
//                finish();
//                break;
//            case R.id.bu_wancheng:
//                Log.i("collectorID", collectorID + "");
//                switch (Acposition) {
//                    case 0:
//                        setText = nameBinding.editDevicename.getText().toString();
//                        if (setText.isEmpty() | setText.length() > 12) {
//                            CustomToast.showCustomErrorToast(this, getString(R.string.vs179));
//                            break;
//                        }
//                        changeCollector(collectorID, setText, null, null, null, null, null);
//                        break;
//                    case 1:
//                        changeCollector(collectorID, null, setText, null, null, null, null);
//                        break;
//                    case 2:
//                        changeCollector(collectorID, null, null, setText, null, null, null);
//                        break;
//                    case 3:
//                        changeCollector(collectorID, null, null, null, setText, null, null);
//                        break;
//                    case 4:
//                        changeCollector(collectorID, null, null, null, null, setText, null);
//                        break;
//                    case 5:
//                        changeCollector(collectorID, null, null, null, null, null, setText);
//                        break;
//                }
//                break;
//        }
//    }

    public void changeCollector(String collectorID, String name, String baud, String freq, String ranges, String faultFreq, String HBFreq) {
        currentCall = Core.instance(this).updateCollectorParam(collectorID, name, baud, freq, ranges, faultFreq, HBFreq, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(CollectorAttrSetActivity.this, R.string.modify_succe, Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
             /*   intent.putExtra("setText", setText);
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

    public static class AttrItem {
        private String attr;
        private boolean selected;

        AttrItem(String attr) {
            this.attr = attr;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    @Deprecated
    class CountsAdapter extends BaseAdapter {
        private String[] counts;

        CountsAdapter(String[] counts) {
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
            holder.img_slecte.setImageDrawable(currentPosition != position ? getDrawable(R.drawable.dx_unchose_slices) : getDrawable(R.drawable.dx_chose_slices));
            return convertView;
        }

        class Holder {
            TextView text_counts;
            ImageView img_slecte;
        }
    }
}
