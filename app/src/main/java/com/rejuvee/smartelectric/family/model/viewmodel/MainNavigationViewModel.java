package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainNavigationViewModel extends ViewModel {
    private MutableLiveData<String> nickname = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<String> telephone = new MutableLiveData<>();
    private MutableLiveData<String> headImgUrl = new MutableLiveData<>();
    private MutableLiveData<String> wechatUnionID = new MutableLiveData<>();
    private MutableLiveData<String> qqUnionID = new MutableLiveData<>();

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

    public MutableLiveData<String> getTelephone() {
        if (telephone == null) {
            telephone = new MutableLiveData<>();
        }
        return telephone;
    }

    public void setTelephone(String _telephone) {
        if (telephone == null) {
            telephone = new MutableLiveData<>();
        }
        telephone.setValue(_telephone);
    }

    public MutableLiveData<String> getHeadImgUrl() {
        if (headImgUrl == null) {
            headImgUrl = new MutableLiveData<>();
        }
        return headImgUrl;
    }

    public void setHeadImgUrl(String _headImgUrl) {
        if (headImgUrl == null) {
            headImgUrl = new MutableLiveData<>();
        }
        headImgUrl.setValue(_headImgUrl);
    }

    public MutableLiveData<String> getWechatUnionID() {
        if (wechatUnionID == null) {
            wechatUnionID = new MutableLiveData<>();
        }
        return wechatUnionID;
    }

    public void setWechatUnionID(String _wechatUnionID) {
        if (wechatUnionID == null) {
            wechatUnionID = new MutableLiveData<>();
        }
        wechatUnionID.setValue(_wechatUnionID);
    }

    public MutableLiveData<String> getQQUnionID() {
        if (qqUnionID == null) {
            qqUnionID = new MutableLiveData<>();
        }
        return qqUnionID;
    }

    public void setQQUnionID(String _qqUnionID) {
        if (qqUnionID == null) {
            qqUnionID = new MutableLiveData<>();
        }
        qqUnionID.setValue(_qqUnionID);
    }
}
