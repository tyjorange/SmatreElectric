package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/11/20.
 */

/*{
        "code": 1,
        "message": "成功",
        "datasize": 1,
        "data": [{
        "HBFreq": 45,
        "HBTime": "2018-11-20 09:48:48",
        "baud": 2400,
        "code": "201811061111",
        "collectorID": "C7CD37CA1C03412A98151DBB2F5939FE",
        "freq": 10,
        "name": "201811061111",
        "ranges": 10,
        "setupCode": "a4yemDU2zBJJIS_LOsa",
        "srvIP": "192.168.1.82",
        "srvPort": 55555,
        "switchs": [{
        "addTime": "2018-11-16 11:39:12",
        "code": "201804180015",
        "frequency": 0,
        "iconType": 1,
        "isLocked": 0,
        "lockedPwd": "",
        "lockedUser": null,
        "name": "客厅",
        "sequence": 0,
        "state": 0,
        "switchID": "6C7F78FA76524217A63F8EEE1C311DDC",
        "ees": [{
        "gonglv": 13,
        "id": "024bbdb021074faebb0aa0ec8b484e65",
        "name": "3"
        }, {
        "gonglv": 10,
        "id": "309c662f3fa4439d8877700edc97c5e3",
        "name": "\"sety\""
        }, {
        "gonglv": 1,
        "id": "d9f4e1620ae84f599ec41702ec904411",
        "name": "请输入电器名称"
        }]
        }]
        }]
        }*/
@Deprecated
public class MyEleBean {

    public String code;
    public ArrayList<allSwitchs> switchs;

    public MyEleBean(String code, ArrayList<allSwitchs> switchs) {
        this.code = code;
        this.switchs = switchs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<allSwitchs> getSwitchs() {
        return switchs;
    }

    public void setSwitchs(ArrayList<allSwitchs> switchs) {
        this.switchs = switchs;
    }

    public static class allSwitchs implements Parcelable{

        @SerializedName("name")
        public String linename; //线路名称
        public String switchID;
        public ArrayList<allEle> ees;

        public allSwitchs(String linename, String switchID, ArrayList<allEle> ees) {
            this.linename = linename;
            this.switchID = switchID;
            this.ees = ees;
        }

        public String getLinename() {
            return linename;
        }

        public void setLinename(String linename) {
            this.linename = linename;
        }

        public String getSwitchID() {
            return switchID;
        }

        public void setSwitchID(String switchID) {
            this.switchID = switchID;
        }

        public ArrayList<allEle> getEes() {
            return ees;
        }

        public void setEes(ArrayList<allEle> ees) {
            this.ees = ees;
        }

        protected allSwitchs(Parcel in) {
            linename = in.readString();
            switchID = in.readString();
            ees = in.createTypedArrayList(allEle.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(linename);
            dest.writeString(switchID);
            dest.writeTypedList(ees);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<allSwitchs> CREATOR = new Creator<allSwitchs>() {
            @Override
            public allSwitchs createFromParcel(Parcel in) {
                return new allSwitchs(in);
            }

            @Override
            public allSwitchs[] newArray(int size) {
                return new allSwitchs[size];
            }
        };
    }

    public static class allEle implements Parcelable {

        public String name;
        public Double gonglv;
        public String id;

        protected allEle(Parcel in) {
            name = in.readString();
            id = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(id);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<allEle> CREATOR = new Creator<allEle>() {
            @Override
            public allEle createFromParcel(Parcel in) {
                return new allEle(in);
            }

            @Override
            public allEle[] newArray(int size) {
                return new allEle[size];
            }
        };
    }
}