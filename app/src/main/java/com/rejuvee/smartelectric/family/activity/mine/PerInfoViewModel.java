package com.rejuvee.smartelectric.family.activity.mine;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerInfoViewModel extends ViewModel {
    private MutableLiveData<String> headImgurl;
    private MutableLiveData<String> phone;
    private MutableLiveData<String> nickname;
    private MutableLiveData<String> username;

    public MutableLiveData<String> getHeadImgUrl() {
        if (headImgurl == null) {
            headImgurl = new MutableLiveData<>();
        }
        return headImgurl;
    }

    public void setHeadImgUrl(String _headImgurl) {
        if (headImgurl == null) {
            headImgurl = new MutableLiveData<>();
        }
        headImgurl.setValue(_headImgurl);
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

    public MutableLiveData<String> getNickname() {
        if (nickname == null) {
            nickname = new MutableLiveData<>();
        }
        return nickname;
    }

    public void setNickname(String _nickname) {
        if (nickname == null) {
            nickname = new MutableLiveData<>();
        }
        nickname.setValue(_nickname);
    }
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
}
