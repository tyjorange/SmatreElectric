package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimePriceSetViewModel extends ViewModel {
    private MutableLiveData<String> startTime;
    private MutableLiveData<String> endTime;
    private MutableLiveData<String> price;

    public MutableLiveData<String> getStartTime() {
        if (startTime == null) {
            startTime = new MutableLiveData<>();
        }
        return startTime;
    }

    public void setStartTime(String _startTime) {
        if (startTime == null) {
            startTime = new MutableLiveData<>();
        }
        startTime.setValue(_startTime);
    }

    public MutableLiveData<String> getEndTime() {
        if (endTime == null) {
            endTime = new MutableLiveData<>();
        }
        return endTime;
    }

    public void setEndTime(String _endTime) {
        if (endTime == null) {
            endTime = new MutableLiveData<>();
        }
        endTime.setValue(_endTime);
    }

    public MutableLiveData<String> getPrice() {
        if (price == null) {
            price = new MutableLiveData<>();
        }
        return price;
    }

    public void setPrice(String _price) {
        if (price == null) {
            price = new MutableLiveData<>();
        }
        price.setValue(_price);
    }
}
