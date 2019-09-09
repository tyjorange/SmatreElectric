package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 集中器升级信息
 */
public class CollectorUpgradeInfo implements Parcelable {
    private String time;        // 确认时间
    private String upgradeTime;    // 集中器升级通知时间
    private String noticeTime;    // 微信消息通知时间
    private String downloadTime;    // 集中器下载时间
    private Integer doTime; // 执行时间段
    private Integer ok;   // 确认结果 0=不同意 1=同意

    private CollectorUpgradeInfo(Parcel in) {
        time = in.readString();
        upgradeTime = in.readString();
        noticeTime = in.readString();
        downloadTime = in.readString();
        if (in.readByte() == 0) {
            doTime = null;
        } else {
            doTime = in.readInt();
        }
        if (in.readByte() == 0) {
            ok = null;
        } else {
            ok = in.readInt();
        }
    }

    public static final Creator<CollectorUpgradeInfo> CREATOR = new Creator<CollectorUpgradeInfo>() {
        @Override
        public CollectorUpgradeInfo createFromParcel(Parcel in) {
            return new CollectorUpgradeInfo(in);
        }

        @Override
        public CollectorUpgradeInfo[] newArray(int size) {
            return new CollectorUpgradeInfo[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(String upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public Integer getDoTime() {
        return doTime;
    }

    public void setDoTime(Integer doTime) {
        this.doTime = doTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(upgradeTime);
        dest.writeString(noticeTime);
        dest.writeString(downloadTime);
        if (doTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(doTime);
        }
        if (ok == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ok);
        }
    }

    @Override
    public String toString() {
        return "CollectorUpgradeInfo{" +
                "time='" + time + '\'' +
                ", upgradeTime='" + upgradeTime + '\'' +
                ", noticeTime='" + noticeTime + '\'' +
                ", downloadTime='" + downloadTime + '\'' +
                ", doTime=" + doTime +
                ", ok=" + ok +
                '}';
    }
}
