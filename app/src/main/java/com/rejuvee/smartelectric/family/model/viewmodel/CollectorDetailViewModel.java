package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollectorDetailViewModel extends ViewModel {
    private MutableLiveData<String> sharedName;
    private MutableLiveData<String> collectorBeanCode;

    public MutableLiveData<String> getSharedName() {
        if (sharedName == null) {
            sharedName = new MutableLiveData<>();
        }
        return sharedName;
    }

    public void setSharedName(String _sharedName) {
        if (sharedName == null) {
            sharedName = new MutableLiveData<>();
        }
        sharedName.setValue(_sharedName);
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
