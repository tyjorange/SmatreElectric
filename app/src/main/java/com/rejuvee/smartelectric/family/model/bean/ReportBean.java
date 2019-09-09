package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ReportBean implements Parcelable {
    public int id;
    public String startDay;
    public String endDay;
    public int timeType;// 1 周报 2 月报

    protected ReportBean(Parcel in) {
        id = in.readInt();
        startDay = in.readString();
        endDay = in.readString();
        timeType = in.readInt();
    }

    public static final Creator<ReportBean> CREATOR = new Creator<ReportBean>() {
        @Override
        public ReportBean createFromParcel(Parcel in) {
            return new ReportBean(in);
        }

        @Override
        public ReportBean[] newArray(int size) {
            return new ReportBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(startDay);
        dest.writeString(endDay);
        dest.writeInt(timeType);
    }
}
