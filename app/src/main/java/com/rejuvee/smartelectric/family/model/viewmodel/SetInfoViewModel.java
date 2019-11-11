package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SetInfoViewModel extends ViewModel {
    private MutableLiveData<String> txtTitle;
    private MutableLiveData<String> txtString;

    public MutableLiveData<String> getTxtTitle() {
        if (txtTitle == null) {
            txtTitle = new MutableLiveData<>();
        }
        return txtTitle;
    }

    public void setTxtTitle(String _txtTitle) {
        if (txtTitle == null) {
            txtTitle = new MutableLiveData<>();
        }
        txtTitle.setValue(_txtTitle);
    }

    public MutableLiveData<String> getTxtString() {
        if (txtString == null) {
            txtString = new MutableLiveData<>();
        }
        return txtString;
    }

    public void setTxtString(String _txtString) {
        if (txtString == null) {
            txtString = new MutableLiveData<>();
        }
        txtString.setValue(_txtString);
    }
}
