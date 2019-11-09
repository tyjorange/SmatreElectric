package com.rejuvee.smartelectric.family.activity.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rejuvee.smartelectric.family.common.utils.AccountHelper;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<AccountInfo> cacheAccountInfo;
    private MutableLiveData<AccountHelper> accountHelper;

    MutableLiveData<AccountInfo> getCacheAccountInfo() {
        if (cacheAccountInfo == null) {
            cacheAccountInfo = new MutableLiveData<>();
        }
        return cacheAccountInfo;
    }

    void setCacheAccountInfo(AccountInfo account) {
        if (cacheAccountInfo == null) {
            cacheAccountInfo = new MutableLiveData<>();
        }
        cacheAccountInfo.setValue(account);
    }

    MutableLiveData<AccountHelper> getAccountHelper() {
        if (accountHelper == null) {
            accountHelper = new MutableLiveData<>();
        }
        return accountHelper;
    }

    void setAccountHelper(AccountHelper ah) {
        if (accountHelper == null) {
            accountHelper = new MutableLiveData<>();
        }
        accountHelper.setValue(ah);
    }
}
