package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SwitchInfoViewModel extends ViewModel {
    private MutableLiveData<Integer> imgRes;
    private MutableLiveData<String> collectorName;// 电箱名
    private MutableLiveData<String> switchName;// 电箱名
    private MutableLiveData<Integer> cmdState;// 开合命令

    public MutableLiveData<Integer> getImgRes() {
        if (imgRes == null) {
            imgRes = new MutableLiveData<>();
        }
        return imgRes;
    }

    public void setImgRes(Integer val) {
        if (imgRes == null) {
            imgRes = new MutableLiveData<>();
        }
        imgRes.setValue(val);
    }

    public MutableLiveData<String> getCollectorName() {
        if (collectorName == null) {
            collectorName = new MutableLiveData<>();
        }
        return collectorName;
    }

    public void setCollectorName(String val) {
        if (collectorName == null) {
            collectorName = new MutableLiveData<>();
        }
        collectorName.setValue(val);
    }

    public MutableLiveData<String> getSwitchName() {
        if (switchName == null) {
            switchName = new MutableLiveData<>();
        }
        return switchName;
    }

    public void setSwitchName(String val) {
        if (switchName == null) {
            switchName = new MutableLiveData<>();
        }
        switchName.setValue(val);
    }

    public MutableLiveData<Integer> getCmdState() {
        if (cmdState == null) {
            cmdState = new MutableLiveData<>();
        }
        return cmdState;
    }

    public void setCmdState(Integer val) {
        if (cmdState == null) {
            cmdState = new MutableLiveData<>();
        }
        cmdState.setValue(val);
    }
}
