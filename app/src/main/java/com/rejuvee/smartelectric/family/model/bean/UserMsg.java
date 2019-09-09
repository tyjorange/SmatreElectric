package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import io.realm.annotations.Ignore;

/**
 * Created by Administrator on 2018/6/5.
 */
public class UserMsg implements Parcelable {

    private String headImg;
    private String id;
    private String nickName;
    private String password;
    private String phone;
    private String timeMills;
    private String username;
    private String collectorShareID;
    private String wechatUnionID;
    private String qqUnionID;
    private int enable;

    private UserMsg(Parcel in) {
        headImg = in.readString();
        id = in.readString();
        nickName = in.readString();
        password = in.readString();
        phone = in.readString();
        timeMills = in.readString();
        username = in.readString();
//        collectorShareID = in.readString();
//        wechatUnionID = in.readString();
//        qqOpenID = in.readString();
//        enable = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headImg);
        dest.writeString(id);
        dest.writeString(nickName);
        dest.writeString(password);
        dest.writeString(phone);
        dest.writeString(timeMills);
        dest.writeString(username);
//        dest.writeString(collectorShareID);
//        dest.writeString(wechatUnionID);
//        dest.writeString(qqOpenID);
//        dest.writeInt(enable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserMsg> CREATOR = new Creator<UserMsg>() {
        @Override
        public UserMsg createFromParcel(Parcel in) {
            return new UserMsg(in);
        }

        @Override
        public UserMsg[] newArray(int size) {
            return new UserMsg[size];
        }
    };

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTimeMills() {
        return timeMills;
    }

    public void setTimeMills(String timeMills) {
        this.timeMills = timeMills;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCollectorShareID() {
        return collectorShareID;
    }

    public void setCollectorShareID(String collectorShareID) {
        this.collectorShareID = collectorShareID;
    }

    public String getWechatUnionID() {
        return wechatUnionID;
    }

    public void setWechatUnionID(String wechatUnionID) {
        this.wechatUnionID = wechatUnionID;
    }

    public String getQqUnionID() {
        return qqUnionID;
    }

    public void setQqUnionID(String qqUnionID) {
        this.qqUnionID = qqUnionID;
    }
}
