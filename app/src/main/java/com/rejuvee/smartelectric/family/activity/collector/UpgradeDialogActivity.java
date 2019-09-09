package com.rejuvee.smartelectric.family.activity.collector;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.ScreenUtils;
import com.base.library.utils.SizeUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorUpgradeInfo;
import com.rejuvee.smartelectric.family.widget.DialogTipWithoutOkCancel;

import java.util.ArrayList;
import java.util.List;

/**
 * 透明DialogActivity
 * <p>
 * 集中器版本升级确认
 */
public class UpgradeDialogActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "UpgradeDialogActivity";
    private final List<Item> items = new ArrayList<>();
    private CollectorBean collectorBean;
    private CollectorUpgradeInfo collectorUpgradeInfo;
    //    private Spinner spinner;
    private Button btnSave;
    private Button btnCancel;
    private TextView tvTime;
    private TextView tvTip;
    private ImageView tvWenHao;
    private List<CheckBox> mListDataCheckButton = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.active_upgrade_dialog;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        //        Resources resources = this.getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = dm.heightPixels;
//        p.width = dm.widthPixels; // 宽度设置为屏幕的0.8*/
//        dialogWindow.setAttributes(p);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        items.add(new Item("00:00-04:00", "1"));
        items.add(new Item("04:00-08:00", "2"));
        items.add(new Item("08:00-12:00", "3"));
        items.add(new Item("12:00-16:00", "4"));
        items.add(new Item("16:00-20:00", "5"));
        items.add(new Item("20:00-24:00", "6"));
        CheckBox cb1 = findViewById(R.id.cb_op1);
        CheckBox cb2 = findViewById(R.id.cb_op2);
        CheckBox cb3 = findViewById(R.id.cb_op3);
        CheckBox cb4 = findViewById(R.id.cb_op4);
        CheckBox cb5 = findViewById(R.id.cb_op5);
        CheckBox cb6 = findViewById(R.id.cb_op6);
        cb1.setOnClickListener(this);
        cb2.setOnClickListener(this);
        cb3.setOnClickListener(this);
        cb4.setOnClickListener(this);
        cb5.setOnClickListener(this);
        cb6.setOnClickListener(this);
        mListDataCheckButton.add(cb1);
        mListDataCheckButton.add(cb2);
        mListDataCheckButton.add(cb3);
        mListDataCheckButton.add(cb4);
        mListDataCheckButton.add(cb5);
        mListDataCheckButton.add(cb6);
//        int width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32 + 28)) / mListDataCheckButton.size();
        for (int i = 0; i < mListDataCheckButton.size(); i++) {
            CheckBox checkBox = mListDataCheckButton.get(i);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) checkBox.getLayoutParams();
//            params.width = width;
//            params.height = width;
            checkBox.setText(items.get(i).key);
        }
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        collectorUpgradeInfo = getIntent().getParcelableExtra("collectorUpgradeInfo");
//        spinner = findViewById(R.id.spinner_time);
        btnSave = findViewById(R.id.btn_ok);
        btnCancel = findViewById(R.id.btn_ignore);
        tvTime = findViewById(R.id.tv_time);
        tvTip = findViewById(R.id.tv_tip);
        tvWenHao = findViewById(R.id.iv_wenhao);
    }

    @Override
    protected void initData() {
        tvWenHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogTipWithoutOkCancel(UpgradeDialogActivity.this).setTitle("电箱升级介绍").setContent(getString(R.string.upgrade_tip)).show();
            }
        });
//        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        spinner.setAdapter(adapter);//事件段选择
        if (collectorUpgradeInfo != null) {// 以前确认过
            tvTime.setText("当前版本:" + collectorBean.getVerMajor() + "." + collectorBean.getVerMinor() + " 新版本:" + collectorBean.getVerMajorNew() + "." + collectorBean.getVerMinorNew());
            tvTip.setText(collectorUpgradeInfo.getTime() + (collectorUpgradeInfo.getOk() == 1 ? " 已同意升级" : " 已忽略此版本"));
//            spinner.setVisibility(collectorUpgradeInfo.getOk() == 1 ? View.VISIBLE : View.INVISIBLE);
//            spinner.setSelection(collectorUpgradeInfo.getDoTime() - 1);// array [index-1]
//            spinner.setEnabled(collectorUpgradeInfo.getOk() != 1);
            // 设置选中时间
            if (collectorUpgradeInfo.getOk() == 1) {
                mListDataCheckButton.get(collectorUpgradeInfo.getDoTime() - 1).setChecked(true);
            }
            for (CheckBox c : mListDataCheckButton) {
                c.setEnabled(false);
            }
            btnCancel.setVisibility(View.INVISIBLE);
            btnSave.setText("确认");
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        } else {// 让用户选择：同意升级/忽略此版本
            tvTime.setText("当前版本:" + collectorBean.getVerMajor() + "." + collectorBean.getVerMinor() + " 新版本:" + collectorBean.getVerMajorNew() + "." + collectorBean.getVerMinorNew());
            tvTip.setText("请选择升级时间段");
            btnCancel.setVisibility(View.VISIBLE);
            btnSave.setText("升级");
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mListDataCheckButton.size(); i++) {
                        if (mListDataCheckButton.get(i).isChecked()) {
                            setCollectorUpgrade(1, items.get(i).getValue());
                            break;
                        }
                    }

                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCollectorUpgrade(0, "0");
                }
            });
        }
    }

    @Override
    protected void dealloc() {
    }

    /**
     * 设置集中器版本升级情况
     *
     * @param ok 确认结果 0=不同意 1=同意
     */
    private void setCollectorUpgrade(Integer ok, String execTime) {
        Core.instance(UpgradeDialogActivity.this).setCollectorUpgrade(
                collectorBean.getCollectorID(),
                collectorBean.getVerMajorNew(),
                collectorBean.getVerMinorNew(),
                collectorBean.getFileID(),
                ok,
                execTime,
                new ActionCallbackListener<Void>() {

                    @Override
                    public void onSuccess(Void data) {
                        CustomToast.showCustomToast(UpgradeDialogActivity.this, ok == 1 ? "已同意升级" : "已忽略此版本");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        Log.e(TAG, message);
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
    }

    private Item getSelect(String val) {
        for (Item i : items) {
            if (i.getValue().equals(val))
                return i;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        for (CheckBox c : mListDataCheckButton) {
            c.setChecked(false);
        }
        ((CheckBox) v).setChecked(true);
    }

    class Item {
        private String key;
        private String value;

        Item(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        //to display object as a string in spinnerGL
        @Override
        public String toString() {
            return key;
        }

    }

}
