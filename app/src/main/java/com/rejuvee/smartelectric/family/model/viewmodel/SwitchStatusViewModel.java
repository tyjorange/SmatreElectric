package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SwitchStatusViewModel extends ViewModel {
    private MutableLiveData<String> onlineText;//电流
    private MutableLiveData<String> lineName;//电流
    private MutableLiveData<String> dlVal;//电流
    private MutableLiveData<String> dyVal;//电压
    private MutableLiveData<String> ldlVal;//漏电流
    private MutableLiveData<String> wdVal;//温度
    private MutableLiveData<String> ygdyVal;//有功电量 当月
    private MutableLiveData<String> switchVer;//线路硬件版本

    public MutableLiveData<String> getOnlineText() {
        if (onlineText == null) {
            onlineText = new MutableLiveData<>();
        }
        return onlineText;
    }

    public void setOnlineText(String _onlineText) {
        if (onlineText == null) {
            onlineText = new MutableLiveData<>();
        }
        onlineText.setValue(_onlineText);
    }

    public MutableLiveData<String> getLineName() {
        if (lineName == null) {
            lineName = new MutableLiveData<>();
        }
        return lineName;
    }

    public void setLineName(String _lineName) {
        if (lineName == null) {
            lineName = new MutableLiveData<>();
        }
        lineName.setValue(_lineName);
    }

    public MutableLiveData<String> getDlVal() {
        if (dlVal == null) {
            dlVal = new MutableLiveData<>();
        }
        return dlVal;
    }

    public void setDlVal(String _dlVal) {
        if (dlVal == null) {
            dlVal = new MutableLiveData<>();
        }
        dlVal.setValue(_dlVal);
    }

    public MutableLiveData<String> getDyVal() {
        if (dyVal == null) {
            dyVal = new MutableLiveData<>();
        }
        return dyVal;
    }

    public void setDyVal(String _dyVal) {
        if (dyVal == null) {
            dyVal = new MutableLiveData<>();
        }
        dyVal.setValue(_dyVal);
    }

    public MutableLiveData<String> getLdlVal() {
        if (ldlVal == null) {
            ldlVal = new MutableLiveData<>();
        }
        return ldlVal;
    }

    public void setLdlVal(String _ldlVal) {
        if (ldlVal == null) {
            ldlVal = new MutableLiveData<>();
        }
        ldlVal.setValue(_ldlVal);
    }

    public MutableLiveData<String> getWdVal() {
        if (wdVal == null) {
            wdVal = new MutableLiveData<>();
        }
        return wdVal;
    }

    public void setWdVal(String _wdVal) {
        if (wdVal == null) {
            wdVal = new MutableLiveData<>();
        }
        wdVal.setValue(_wdVal);
    }

    public MutableLiveData<String> getYgdyVal() {
        if (ygdyVal == null) {
            ygdyVal = new MutableLiveData<>();
        }
        return ygdyVal;
    }

    public void setYgdyVal(String _ygdyVal) {
        if (ygdyVal == null) {
            ygdyVal = new MutableLiveData<>();
        }
        ygdyVal.setValue(_ygdyVal);
    }

    public MutableLiveData<String> getSwitchVer() {
        if (switchVer == null) {
            switchVer = new MutableLiveData<>();
        }
        return switchVer;
    }

    public void setSwitchVer(String _switchVer) {
        if (switchVer == null) {
            switchVer = new MutableLiveData<>();
        }
        switchVer.setValue(_switchVer);
    }
}
