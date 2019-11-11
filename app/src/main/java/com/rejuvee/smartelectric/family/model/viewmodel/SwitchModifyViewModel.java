package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SwitchModifyViewModel extends ViewModel {
    private MutableLiveData<String> txtLineName;
    private MutableLiveData<String> editLineName;


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

    public MutableLiveData<String> getEditLineName() {
        if (editLineName == null) {
            editLineName = new MutableLiveData<>();
        }
        return editLineName;
    }

    public void setEditLineName(String _editLineName) {
        if (editLineName == null) {
            editLineName = new MutableLiveData<>();
        }
        editLineName.setValue(_editLineName);
    }
}
