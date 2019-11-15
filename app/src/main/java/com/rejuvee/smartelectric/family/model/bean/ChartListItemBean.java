package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.ViewModel;

public class ChartListItemBean extends ViewModel implements Parcelable {
    private int id;
    private String topic;
    private String content;
    private String time;
    private String updateTime;

    private ChartListItemBean(Parcel in) {
        id = in.readInt();
        topic = in.readString();
        content = in.readString();
        time = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<ChartListItemBean> CREATOR = new Creator<ChartListItemBean>() {
        @Override
        public ChartListItemBean createFromParcel(Parcel in) {
            return new ChartListItemBean(in);
        }

        @Override
        public ChartListItemBean[] newArray(int size) {
            return new ChartListItemBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(topic);
        dest.writeString(content);
        dest.writeString(time);
        dest.writeString(updateTime);
    }
}