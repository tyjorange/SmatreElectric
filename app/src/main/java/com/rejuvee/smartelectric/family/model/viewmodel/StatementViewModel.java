package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatementViewModel extends ViewModel {
    private MutableLiveData<String> currentDate;
    private MutableLiveData<String> dateStart;
    private MutableLiveData<String> resStart;
    private MutableLiveData<String> dateEnd;
    private MutableLiveData<String> resEnd;
    private MutableLiveData<String> quantity;
    private MutableLiveData<String> charge;

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

    public MutableLiveData<String> getDateStart() {
        if (dateStart == null) {
            dateStart = new MutableLiveData<>();
        }
        return dateStart;
    }

    public void setDateStart(String _dateStart) {
        if (dateStart == null) {
            dateStart = new MutableLiveData<>();
        }
        dateStart.setValue(_dateStart);
    }

    public MutableLiveData<String> getResStart() {
        if (resStart == null) {
            resStart = new MutableLiveData<>();
        }
        return resStart;
    }

    public void setResStart(String _resStart) {
        if (resStart == null) {
            resStart = new MutableLiveData<>();
        }
        resStart.setValue(_resStart);
    }

    public MutableLiveData<String> getDateEnd() {
        if (dateEnd == null) {
            dateEnd = new MutableLiveData<>();
        }
        return dateEnd;
    }

    public void setDateEnd(String _dateEnd) {
        if (dateEnd == null) {
            dateEnd = new MutableLiveData<>();
        }
        dateEnd.setValue(_dateEnd);
    }

    public MutableLiveData<String> getResEnd() {
        if (resEnd == null) {
            resEnd = new MutableLiveData<>();
        }
        return resEnd;
    }

    public void setResEnd(String _resEnd) {
        if (resEnd == null) {
            resEnd = new MutableLiveData<>();
        }
        resEnd.setValue(_resEnd);
    }

    public MutableLiveData<String> getQuantity() {
        if (quantity == null) {
            quantity = new MutableLiveData<>();
        }
        return quantity;
    }

    public void setQuantity(String _quantity) {
        if (quantity == null) {
            quantity = new MutableLiveData<>();
        }
        quantity.setValue(_quantity);
    }
    public MutableLiveData<String> getCharge() {
        if (charge == null) {
            charge = new MutableLiveData<>();
        }
        return charge;
    }

    public void setCharge(String _charge) {
        if (charge == null) {
            charge = new MutableLiveData<>();
        }
        charge.setValue(_charge);
    }
}
