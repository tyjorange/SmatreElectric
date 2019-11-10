package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EnergySavingInformationViewModel extends ViewModel {
    private MutableLiveData<String> collectorName;
    private MutableLiveData<String> collectorCode;
    private MutableLiveData<String> lineName;
    private MutableLiveData<String> lastMonth;
    private MutableLiveData<String> lastYearMonth;
    private MutableLiveData<String> hbjsVal;
    private MutableLiveData<String> tbjsVal;
    private MutableLiveData<String> ydlVal;

    public MutableLiveData<String> getCollectorName() {
        if (collectorName == null) {
            collectorName = new MutableLiveData<>();
        }
        return collectorName;
    }

    public void setCollectorName(String _collectorName) {
        if (collectorName == null) {
            collectorName = new MutableLiveData<>();
        }
        collectorName.setValue(_collectorName);
    }

    public MutableLiveData<String> getCollectorCode() {
        if (collectorCode == null) {
            collectorCode = new MutableLiveData<>();
        }
        return collectorCode;
    }

    public void setCollectorCode(String _collectorCode) {
        if (collectorCode == null) {
            collectorCode = new MutableLiveData<>();
        }
        collectorCode.setValue(_collectorCode);
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

    public MutableLiveData<String> getLastMonth() {
        if (lastMonth == null) {
            lastMonth = new MutableLiveData<>();
        }
        return lastMonth;
    }

    public void setLastMonth(String _lastMonth) {
        if (lastMonth == null) {
            lastMonth = new MutableLiveData<>();
        }
        lastMonth.setValue(_lastMonth);
    }

    public MutableLiveData<String> getLastYearMonth() {
        if (lastYearMonth == null) {
            lastYearMonth = new MutableLiveData<>();
        }
        return lastYearMonth;
    }

    public void setLastYearMonth(String _lastYearMonth) {
        if (lastYearMonth == null) {
            lastYearMonth = new MutableLiveData<>();
        }
        lastYearMonth.setValue(_lastYearMonth);
    }

    public MutableLiveData<String> getHbjsVal() {
        if (hbjsVal == null) {
            hbjsVal = new MutableLiveData<>();
        }
        return hbjsVal;
    }

    public void setHbjsVal(String _hbjsVal) {
        if (hbjsVal == null) {
            hbjsVal = new MutableLiveData<>();
        }
        hbjsVal.setValue(_hbjsVal);
    }

    public MutableLiveData<String> getTbjsVal() {
        if (tbjsVal == null) {
            tbjsVal = new MutableLiveData<>();
        }
        return tbjsVal;
    }

    public void setTbjsVal(String _tbjsVal) {
        if (tbjsVal == null) {
            tbjsVal = new MutableLiveData<>();
        }
        tbjsVal.setValue(_tbjsVal);
    }

    public MutableLiveData<String> getYdlVal() {
        if (ydlVal == null) {
            ydlVal = new MutableLiveData<>();
        }
        return ydlVal;
    }

    public void setYdlVal(String _ydlVal) {
        if (ydlVal == null) {
            ydlVal = new MutableLiveData<>();
        }
        ydlVal.setValue(_ydlVal);
    }
}
