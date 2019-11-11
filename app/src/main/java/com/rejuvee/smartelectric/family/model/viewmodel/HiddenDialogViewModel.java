package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HiddenDialogViewModel extends ViewModel {
    private MutableLiveData<String> ip;

    public MutableLiveData<String> getIp() {
        if (ip == null) {
            ip = new MutableLiveData<>();
        }
        return ip;
    }

    public void setIp(String _ip) {
        if (ip == null) {
            ip = new MutableLiveData<>();
        }
        ip.setValue(_ip);
    }
}
