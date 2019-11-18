package com.rejuvee.smartelectric.family.fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.common.custom.MyTextWatcher;
import com.rejuvee.smartelectric.family.databinding.FragmentSettingDl2Binding;
import com.rejuvee.smartelectric.family.model.bean.PP;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 电量类设置
 */
public class SettingDL2Fragment extends BaseFragment {
    private EditText dl_shangxian;
    private EditText dl_xiaxian;
//    private boolean isShowing = false;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_setting_dl2;
//    }

    @Override
    protected View initView() {
        FragmentSettingDl2Binding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_setting_dl2, null, false);
        // 电量上限
        dl_shangxian = mBinding.etDl1;//v.findViewById(R.id.et_dl1);
        MyTextWatcher myTextWatcher1 = new MyTextWatcher(dl_shangxian, "000000.00");
        dl_shangxian.addTextChangedListener(myTextWatcher1);
        dl_shangxian.setOnFocusChangeListener(myTextWatcher1);
        dl_shangxian.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        // 电量下限
        dl_xiaxian = mBinding.etDl2;//v.findViewById(R.id.et_dl2);
        MyTextWatcher myTextWatcher2 = new MyTextWatcher(dl_xiaxian, "000000.00");
        dl_xiaxian.addTextChangedListener(myTextWatcher2);
        dl_xiaxian.setOnFocusChangeListener(myTextWatcher2);
        dl_xiaxian.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        return mBinding.getRoot();
    }

    public List<String> getParamID() {
        List<String> list = new ArrayList<>();
//        return "00000018," + // 电量下限
//                "00000019,"; // 电量上限
        list.add("00000018");
        list.add("00000019");
        return list;
    }

    public void setXX(float paramValue) {
        if (dl_xiaxian != null)
            dl_xiaxian.setText(new DecimalFormat("000000.00").format(paramValue));
    }

    public void setSX(float paramValue) {
        if (dl_shangxian != null)
            dl_shangxian.setText(new DecimalFormat("000000.00").format(paramValue));
    }

    /**
     * @return
     */
    public List<PP> getValList() {
        List<PP> list = new ArrayList<>();
//        String res = "";
        String dlsx = dl_shangxian.getEditableText().toString();
        String dlxx = dl_xiaxian.getEditableText().toString();
        if (dlsx.isEmpty() || dlxx.isEmpty()) {
            CustomToast.showCustomErrorToast(getContext(), getString(R.string.vs151));
            return list;
        }
        if (Double.valueOf(dlsx) <= Double.valueOf(dlxx)) {
            CustomToast.showCustomErrorToast(getContext(), getString(R.string.vs189));
            return list;
        }
//        res += "00000018:" + dlxx + // 电量下限
        list.add(new PP("00000018", dlxx + ""));
//                ",00000019:" + dlsx; // 电量上限
        list.add(new PP("00000019", dlsx + ""));
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onDL2Show();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser && isShowing) {
//            Log.e("VpAdapter", "setUserVisibleHint: " + position);
//            listener.onDL2Show();
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    private OnShowingListener listener;

    public SettingDL2Fragment setOnShowingListener(OnShowingListener onShowingListener) {
        listener = onShowingListener;
        return this;
    }

    public interface OnShowingListener {
        void onDL2Show();
    }
}
