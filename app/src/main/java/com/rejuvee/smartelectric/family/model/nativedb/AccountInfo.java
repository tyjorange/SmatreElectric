package com.rejuvee.smartelectric.family.model.nativedb;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by liuchengran on 2017/12/22.
 * <p>
 * 用户信息
 */

public class AccountInfo extends RealmObject {
    @PrimaryKey
    private String userName;
    private String password;

//    @Ignore
//    private String userkey;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getUserkey() {
//        return userkey;
//    }
//
//    public void setUserkey(String userkey) {
//        this.userkey = userkey;
//    }
}
