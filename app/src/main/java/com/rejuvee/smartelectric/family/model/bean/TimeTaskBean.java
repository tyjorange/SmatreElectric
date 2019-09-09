package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.Ignore;

/**
 * Created by liuchengran on 2017/12/21.
 * <p>
 * "code": "001",
 * "frequency": 0,
 * "iconType": 1,
 * "name": "客厅",
 * "switchID": "1",
 * "timeController": [
 * {
 * "timeControllerID": "0AD1CEC3B0634B70876E8FD03721796E",
 * "runTime": "06:30:00",
 * "weekday": 1,
 * "state": 1,
 * "cmdData": 1
 * },
 * {
 * "timeControllerID": "1",
 * "runTime": "07:50:00",
 * "weekday": 2,
 * "state": 1,
 * "cmdData": 1
 * }
 * ]
 */

public class TimeTaskBean {
    @SerializedName("name")
    public String lineName;
    public int iconType;
    public int taskNum;
    @SerializedName("switchID")
    public String lineId;
    @SerializedName("timeController")
    public TimeTask listTask;
    public boolean isSelected;
    public String addTime;//任务添加时间
    public CollectorBean collector;

    public static class TimeTask implements Parcelable {
        @SerializedName("runTime")
        public String time;//重复时间
        @SerializedName("cmdData")
        public int switchState; // 操作
        @SerializedName("weekday")
        public int repeatState;//重复周期
        @SerializedName("state")
        public int enable;//使能
        @SerializedName("timeControllerID")
        public String taskId;

        public long createTime;//创建时间
        public String lineName;//线路名称
        public String lineId;//线路id
        public int repeatMode;//任务类型

        public String collectName;//集中器名称
        public int onLine;
        public int viewType; // 0 :集中器  1：定时器

        public int upload;//驻留集中器

        @Ignore
        public int showDelIcon = View.GONE;

        public TimeTask() {

        }

        protected TimeTask(Parcel in) {
            time = in.readString();
            switchState = in.readInt();
            repeatState = in.readInt();
            enable = in.readInt();
            taskId = in.readString();
            createTime = in.readLong();
            lineName = in.readString();
            lineId = in.readString();
            repeatMode = in.readInt();
            collectName = in.readString();
            onLine = in.readInt();
            viewType = in.readInt();
            upload = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(time);
            dest.writeInt(switchState);
            dest.writeInt(repeatState);
            dest.writeInt(enable);
            dest.writeString(taskId);
            dest.writeLong(createTime);
            dest.writeString(lineName);
            dest.writeString(lineId);
            dest.writeInt(repeatMode);
            dest.writeString(collectName);
            dest.writeInt(onLine);
            dest.writeInt(viewType);
            dest.writeInt(upload);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TimeTask> CREATOR = new Creator<TimeTask>() {
            @Override
            public TimeTask createFromParcel(Parcel in) {
                return new TimeTask(in);
            }

            @Override
            public TimeTask[] newArray(int size) {
                return new TimeTask[size];
            }
        };
    }

}
