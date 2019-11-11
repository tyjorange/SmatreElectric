package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SwitchSettingViewModel extends ViewModel {
    private MutableLiveData<String> txtLineName;

    public MutableLiveData<String> getTxtLineName() {
        if (txtLineName == null) {
            txtLineName = new MutableLiveData<>();
        }
        return txtLineName;
    }

    public void setTxtLineName(String _txtLineName) {
        if (txtLineName == null) {
            txtLineName = new MutableLiveData<>();
        }
        txtLineName.setValue(_txtLineName);
    }
}
