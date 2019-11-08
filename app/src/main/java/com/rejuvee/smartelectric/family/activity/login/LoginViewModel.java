package com.rejuvee.smartelectric.family.activity.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rejuvee.smartelectric.family.common.utils.AccountHelper;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<AccountInfo> cacheAccount;
    private MutableLiveData<AccountHelper> accountHelper;

    public MutableLiveData<AccountInfo> getCacheAccount() {
        if (cacheAccount == null) {
            cacheAccount = new MutableLiveData<>();
        }
        return cacheAccount;
    }

    public AccountInfo setCacheAccount(AccountInfo account) {
        if (cacheAccount == null) {
            cacheAccount = new MutableLiveData<>();
        }
        cacheAccount.setValue(account);
        return cacheAccount.getValue();
    }

    public MutableLiveData<AccountHelper> getAccountHelper() {
        if (accountHelper == null) {
            accountHelper = new MutableLiveData<>();
        }
        return accountHelper;
    }

    public AccountHelper setAccountHelper(AccountHelper ah) {
        if (accountHelper == null) {
            accountHelper = new MutableLiveData<>();
        }
        accountHelper.setValue(ah);
        return accountHelper.getValue();
    }
}
