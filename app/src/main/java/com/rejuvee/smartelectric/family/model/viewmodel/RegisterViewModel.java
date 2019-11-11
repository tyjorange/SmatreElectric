package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> username;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> code;
    private MutableLiveData<String> pwd;
    private MutableLiveData<String> repwd;

    public MutableLiveData<String> getUsername() {
        if (username == null) {
            username = new MutableLiveData<>();
        }
        return username;
    }

    public void setUsername(String _username) {
        if (username == null) {
            username = new MutableLiveData<>();
        }
        username.setValue(_username);
    }

    public MutableLiveData<String> getPhone() {
        if (phone == null) {
            phone = new MutableLiveData<>();
        }
        return phone;
    }

    public void setPhone(String _phone) {
        if (phone == null) {
            phone = new MutableLiveData<>();
        }
        phone.setValue(_phone);
    }

    public MutableLiveData<String> getCode() {
        if (code == null) {
            code = new MutableLiveData<>();
        }
        return code;
    }

    public void setCode(String _code) {
        if (code == null) {
            code = new MutableLiveData<>();
        }
        code.setValue(_code);
    }

    public MutableLiveData<String> getPwd() {
        if (pwd == null) {
            pwd = new MutableLiveData<>();
        }
        return pwd;
    }

    public void setPwd(String _pwd) {
        if (pwd == null) {
            pwd = new MutableLiveData<>();
        }
        pwd.setValue(_pwd);
    }

    public MutableLiveData<String> getRePwd() {
        if (repwd == null) {
            repwd = new MutableLiveData<>();
        }
        return repwd;
    }

    public void setRePwd(String _repwd) {
        if (repwd == null) {
            repwd = new MutableLiveData<>();
        }
        repwd.setValue(_repwd);
    }
}
