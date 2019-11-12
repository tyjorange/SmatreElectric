package com.rejuvee.smartelectric.family.activity.collector;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;

import androidx.databinding.DataBindingUtil;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.databinding.ActiveUpgradeDialogBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorUpgradeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 透明DialogActivity
 * <p>
 * 集中器版本升级确认
 */
public class UpgradeDialogActivity extends BaseActivity {
    private String TAG = "UpgradeDialogActivity";
    private final List<Item> items = new ArrayList<>();
    private CollectorBean collectorBean;
    private List<CheckBox> mListDataCheckButton = new ArrayList<>();

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.active_upgrade_dialog;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private CollectorUpgradeInfo collectorUpgradeInfo;

    @Override
    protected void initView() {
        ActiveUpgradeDialogBinding mBinding = DataBindingUtil.setContentView(this, R.layout.active_upgrade_dialog);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
        //        Resources resources = this.getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = dm.heightPixels;
//        p.width = dm.widthPixels; // 宽度设置为屏幕的0.8*/
//        dialogWindow.setAttributes(p);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        collectorBean = getIntent().getParcelableExtra("collectorBean");
        collectorUpgradeInfo = getIntent().getParcelableExtra("collectorUpgradeInfo");

        items.add(new Item("00:00-04:00", "1"));
        items.add(new Item("04:00-08:00", "2"));
        items.add(new Item("08:00-12:00", "3"));
        items.add(new Item("12:00-16:00", "4"));
        items.add(new Item("16:00-20:00", "5"));
        items.add(new Item("20:00-24:00", "6"));
//        cb1.setOnClickListener(this);
//        cb2.setOnClickListener(this);
//        cb3.setOnClickListener(this);
//        cb4.setOnClickListener(this);
//        cb5.setOnClickListener(this);
//        cb6.setOnClickListener(this);
        mListDataCheckButton.add(mBinding.cbOp1);
        mListDataCheckButton.add(mBinding.cbOp2);
        mListDataCheckButton.add(mBinding.cbOp3);
        mListDataCheckButton.add(mBinding.cbOp4);
        mListDataCheckButton.add(mBinding.cbOp5);
        mListDataCheckButton.add(mBinding.cbOp6);
//        int width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32 + 28)) / mListDataCheckButton.size();
        for (int i = 0; i < mListDataCheckButton.size(); i++) {
            CheckBox checkBox = mListDataCheckButton.get(i);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) checkBox.getLayoutParams();
//            params.width = width;
//            params.height = width;
            checkBox.setText(items.get(i).key);
        }

//        Button btnSave = mBinding.btnOk;
//        Button btnCancel = mBinding.btnIgnore;
//        TextView tvTime = mBinding.tvTime;
//        TextView tvTip = mBinding.tvTip;

//        tvWenHao.setOnClickListener(v -> new DialogTipWithoutOkCancel(UpgradeDialogActivity.this).setTitle(getString(R.string.vs22)).setContent(getString(R.string.upgrade_tip)).show());
//        iv_version_wenhao.setOnClickListener(v -> new DialogTipWithoutOkCancel(UpgradeDialogActivity.this).setTitle(getString(R.string.vs257)).setContent(collectorBean.getText()).show());
//        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        spinner.setAdapter(adapter);//事件段选择
        if (collectorUpgradeInfo != null) {// 以前确认过
            mBinding.tvTime.setText(String.format(Locale.getDefault(), "%s%d.%d %s%d.%d",
                    getString(R.string.vs20), collectorBean.getVerMajor(), collectorBean.getVerMinor(),
                    getString(R.string.vs21), collectorBean.getVerMajorNew(), collectorBean.getVerMinorNew()));
            mBinding.tvTip.setText(String.format("%s%s ", collectorUpgradeInfo.getTime(), collectorUpgradeInfo.getOk() == 1 ? getString(R.string.vs23) : getString(R.string.vs24)));
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
            mBinding.btnIgnore.setVisibility(View.INVISIBLE);
            mBinding.btnOk.setText(getString(R.string.vs27));
//            btnSave.setOnClickListener(v -> {
//                setResult(RESULT_CANCELED);
//                finish();
//            });
        } else {// 让用户选择：同意升级/忽略此版本
            mBinding.tvTime.setText(String.format(Locale.getDefault(), "%s%d.%d%s%d.%d",
                    getString(R.string.vs20), collectorBean.getVerMajor(), collectorBean.getVerMinor(),
                    getString(R.string.vs21), collectorBean.getVerMajorNew(), collectorBean.getVerMinorNew()));
            mBinding.tvTip.setText(getString(R.string.vs25));
            mBinding.btnIgnore.setVisibility(View.VISIBLE);
            mBinding.btnOk.setText(getString(R.string.vs26));
//            btnSave.setOnClickListener(v -> {
//                for (int i = 0; i < mListDataCheckButton.size(); i++) {
//                    if (mListDataCheckButton.get(i).isChecked()) {
//                        setCollectorUpgrade(1, items.get(i).getValue());
//                        return;
//                    }
//                }
//                CustomToast.showCustomErrorToast(UpgradeDialogActivity.this, getString(R.string.vs25));
//            });
//            btnCancel.setOnClickListener(v -> {
//                    setCollectorUpgrade(0, "0");
//                finish();
//            });
        }
    }

    public class Presenter {
        public void onCancel(View v) {
            finish();
        }

        public void onOk(View v) {
            if (collectorUpgradeInfo != null) {// 以前确认过
                setResult(RESULT_CANCELED);
                finish();
            } else {// 让用户选择：同意升级/忽略此版本
                for (int i = 0; i < mListDataCheckButton.size(); i++) {
                    if (mListDataCheckButton.get(i).isChecked()) {
                        setCollectorUpgrade(1, items.get(i).getValue());
                        return;
                    }
                }
                CustomToast.showCustomErrorToast(UpgradeDialogActivity.this, getString(R.string.vs25));
            }
        }

        public void onClickCheckBox(View v) {
            for (CheckBox c : mListDataCheckButton) {
                c.setChecked(false);
            }
            ((CheckBox) v).setChecked(true);
        }

        public void onWenhao(View v) {
            new DialogTipWithoutOkCancel(UpgradeDialogActivity.this).setTitle(getString(R.string.vs22)).setContent(getString(R.string.upgrade_tip)).show();
        }

        public void onVersionWenhao(View v) {
            new DialogTipWithoutOkCancel(UpgradeDialogActivity.this).setTitle(getString(R.string.vs257)).setContent(collectorBean.getText()).show();
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
                        CustomToast.showCustomToast(UpgradeDialogActivity.this, ok == 1 ? getString(R.string.vs23) : getString(R.string.vs24));
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

//    private Item getSelect(String val) {
//        for (Item i : items) {
//            if (i.getValue().equals(val))
//                return i;
//        }
//        return null;
//    }

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
