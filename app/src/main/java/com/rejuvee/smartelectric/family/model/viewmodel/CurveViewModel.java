package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurveViewModel extends ViewModel {
    private MutableLiveData<String> currentSwitchBeanName;
    private MutableLiveData<String> currentDate;

    public MutableLiveData<String> getCurrentSwitchBeanName() {
        if (currentSwitchBeanName == null) {
            currentSwitchBeanName = new MutableLiveData<>();
        }
        return currentSwitchBeanName;
    }

    public void setCurrentSwitchBeanName(String _currentSwitchBeanName) {
        if (currentSwitchBeanName == null) {
            currentSwitchBeanName = new MutableLiveData<>();
        }
        currentSwitchBeanName.setValue(_currentSwitchBeanName);
    }

    public MutableLiveData<String> getCurrentDate() {
        if (currentDate == null) {
            currentDate = new MutableLiveData<>();
        }
        return currentDate;
    }

    public void setCurrentDate(String _currentDate) {
        if (currentDate == null) {
            currentDate = new MutableLiveData<>();
        }
        currentDate.setValue(_currentDate);
    }
}
