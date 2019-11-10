package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountBaseViewModel extends ViewModel {
    private MutableLiveData<String> Imei;
    private MutableLiveData<String> Phone;
    private MutableLiveData<String> NewPhone;
    private MutableLiveData<String> Code;
    private MutableLiveData<String> Pwd;
    private MutableLiveData<String> RePwd;
    private MutableLiveData<String> OrgPwd;

    public MutableLiveData<String> getImei() {
        if (Imei == null) {
            Imei = new MutableLiveData<>();
        }
        return Imei;
    }

    public void setImei(String _Imei) {
        if (Imei == null) {
            Imei = new MutableLiveData<>();
        }
        Imei.setValue(_Imei);
    }

    public MutableLiveData<String> getPhone() {
        if (Phone == null) {
            Phone = new MutableLiveData<>();
        }
        return Phone;
    }

    public void setPhone(String _Phone) {
        if (Phone == null) {
            Phone = new MutableLiveData<>();
        }
        Phone.setValue(_Phone);
    }

    public MutableLiveData<String> getNewPhone() {
        if (NewPhone == null) {
            NewPhone = new MutableLiveData<>();
        }
        return NewPhone;
    }

    public void setNewPhone(String _NewPhone) {
        if (NewPhone == null) {
            NewPhone = new MutableLiveData<>();
        }
        NewPhone.setValue(_NewPhone);
    }

    public MutableLiveData<String> getCode() {
        if (Code == null) {
            Code = new MutableLiveData<>();
        }
        return Code;
    }

    public void setCode(String _Code) {
        if (Code == null) {
            Code = new MutableLiveData<>();
        }
        Code.setValue(_Code);
    }

    public MutableLiveData<String> getPwd() {
        if (Pwd == null) {
            Pwd = new MutableLiveData<>();
        }
        return Pwd;
    }

    public void setPwd(String _Pwd) {
        if (Pwd == null) {
            Pwd = new MutableLiveData<>();
        }
        Pwd.setValue(_Pwd);
    }

    public MutableLiveData<String> getRePwd() {
        if (RePwd == null) {
            RePwd = new MutableLiveData<>();
        }
        return RePwd;
    }

    public void setRePwd(String _RePwd) {
        if (RePwd == null) {
            RePwd = new MutableLiveData<>();
        }
        RePwd.setValue(_RePwd);
    }

    public MutableLiveData<String> getOrgPwd() {
        if (OrgPwd == null) {
            OrgPwd = new MutableLiveData<>();
        }
        return OrgPwd;
    }

    public void setOrgPwd(String _OrgPwd) {
        if (OrgPwd == null) {
            OrgPwd = new MutableLiveData<>();
        }
        OrgPwd.setValue(_OrgPwd);
    }
}
