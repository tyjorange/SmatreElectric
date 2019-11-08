package com.rejuvee.smartelectric.family.activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LunchViewModel extends ViewModel {
    private MutableLiveData<String> liveData;

    public MutableLiveData<String> getName() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        return liveData;
    }

    public void setName(String name) {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        liveData.setValue(name);
    }
}
