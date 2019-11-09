package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollectorAttrViewModel extends ViewModel {
    private MutableLiveData<String> devicename;
    private MutableLiveData<String> Baud;
    private MutableLiveData<String> Freq;
    private MutableLiveData<String> Ranges;
    private MutableLiveData<String> guzhangma;
    private MutableLiveData<String> heartrate;
    private MutableLiveData<String> collectorBeanCode;

    public MutableLiveData<String> getDevicename() {
        if (devicename == null) {
            devicename = new MutableLiveData<>();
        }
        return devicename;
    }

    public void setDevicename(String _devicename) {
        if (devicename == null) {
            devicename = new MutableLiveData<>();
        }
        devicename.setValue(_devicename);
    }

    public MutableLiveData<String> getBaud() {
        if (Baud == null) {
            Baud = new MutableLiveData<>();
        }
        return Baud;
    }

    public void setBaud(String _Baud) {
        if (Baud == null) {
            Baud = new MutableLiveData<>();
        }
        Baud.setValue(_Baud);
    }

    public MutableLiveData<String> getFreq() {
        if (Freq == null) {
            Freq = new MutableLiveData<>();
        }
        return Freq;
    }

    public void setFreq(String _Freq) {
        if (Freq == null) {
            Freq = new MutableLiveData<>();
        }
        Freq.setValue(_Freq);
    }

    public MutableLiveData<String> getRanges() {
        if (Ranges == null) {
            Ranges = new MutableLiveData<>();
        }
        return Ranges;
    }

    public void setRanges(String _Ranges) {
        if (Ranges == null) {
            Ranges = new MutableLiveData<>();
        }
        Ranges.setValue(_Ranges);
    }

    public MutableLiveData<String> getGuzhangma() {
        if (guzhangma == null) {
            guzhangma = new MutableLiveData<>();
        }
        return guzhangma;
    }

    public void setGuzhangma(String _guzhangma) {
        if (guzhangma == null) {
            guzhangma = new MutableLiveData<>();
        }
        guzhangma.setValue(_guzhangma);
    }

    public MutableLiveData<String> getHeartrate() {
        if (heartrate == null) {
            heartrate = new MutableLiveData<>();
        }
        return heartrate;
    }

    public void setHeartrate(String _heartrate) {
        if (heartrate == null) {
            heartrate = new MutableLiveData<>();
        }
        heartrate.setValue(_heartrate);
    }

    public MutableLiveData<String> getCollectorBeanCode() {
        if (collectorBeanCode == null) {
            collectorBeanCode = new MutableLiveData<>();
        }
        return collectorBeanCode;
    }

    public void setCollectorBeanCode(String _collectorBeanCode) {
        if (collectorBeanCode == null) {
            collectorBeanCode = new MutableLiveData<>();
        }
        collectorBeanCode.setValue(_collectorBeanCode);
    }
}
