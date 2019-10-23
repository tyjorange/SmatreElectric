package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Item implements Serializable, Parcelable {
    private String name;
    // 信号强度
    private int dbm;

    protected Item(Parcel in) {
        name = in.readString();
        dbm = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDbm() {
        return dbm;
    }

    public void setDbm(int dbm) {
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
