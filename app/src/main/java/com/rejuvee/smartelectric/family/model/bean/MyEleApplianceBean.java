package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import io.realm.annotations.Ignore;

/**
 * Created by Administrator on 2018/11/20.
 */

public class MyEleApplianceBean implements Parcelable {

    private String linename;  //线路名称
    private String elename; //电器名称
    private Double gonglv;
    private String id;
    private int switchID;
    @Ignore
    public int showDelIcon = View.GONE;

    public MyEleApplianceBean(String linename, String elename, Double gonglv, String id, int switchID) {
        this.linename = linename;
        this.elename = elename;
        this.gonglv = gonglv;
        this.id = id;
        this.switchID = switchID;
    }

    private MyEleApplianceBean(Parcel in) {
        linename = in.readString();
        elename = in.readString();
        gonglv = in.readDouble();
        id = in.readString();
        switchID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(linename);
        dest.writeString(elename);
        dest.writeDouble(gonglv);
        dest.writeString(id);
        dest.writeInt(switchID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyEleApplianceBean> CREATOR = new Creator<MyEleApplianceBean>() {
        @Override
        public MyEleApplianceBean createFromParcel(Parcel in) {
            return new MyEleApplianceBean(in);
        }

        @Override
        public MyEleApplianceBean[] newArray(int size) {
            return new MyEleApplianceBean[size];
        }
    };

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public String getElename() {
        return elename;
    }

    public void setElename(String elename) {
        this.elename = elename;
    }

    public Double getGonglv() {
        return gonglv;
    }

    public void setGonglv(Double gonglv) {
        this.gonglv = gonglv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSwitchID() {
        return switchID;
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }


}
