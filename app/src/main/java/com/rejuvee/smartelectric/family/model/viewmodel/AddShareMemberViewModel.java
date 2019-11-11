package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddShareMemberViewModel extends ViewModel {
    private MutableLiveData<String> shareName;

    public MutableLiveData<String> getShareName() {
        if (shareName == null) {
            shareName = new MutableLiveData<>();
        }
        return shareName;
    }

    public void setShareName(String _shareName) {
        if (shareName == null) {
            shareName = new MutableLiveData<>();
        }
        shareName.setValue(_shareName);
    }
}
