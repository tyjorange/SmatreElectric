package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.os.Parcel;
import android.os.Parcelable;

public class SSIDItem implements Parcelable {
    private String name;
    // 信号强度
    private int dbm;

    SSIDItem(Parcel in) {
        name = in.readString();
        dbm = in.readInt();
    }

    public static final Creator<SSIDItem> CREATOR = new Creator<SSIDItem>() {
        @Override
        public SSIDItem createFromParcel(Parcel in) {
            return new SSIDItem(in);
        }

        @Override
        public SSIDItem[] newArray(int size) {
            return new SSIDItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getDbm() {
        return dbm;
    }

    void setDbm(int dbm) {
        this.dbm = dbm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(dbm);
    }
}
