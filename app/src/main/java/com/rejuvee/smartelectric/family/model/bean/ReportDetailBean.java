package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

public class ReportDetailBean implements Parcelable {
    private List<Warn> warn;
    private List<Alarm> alarm;
    private List<Power> power;

    protected ReportDetailBean(Parcel in) {
    }

    public static final Creator<ReportDetailBean> CREATOR = new Creator<ReportDetailBean>() {
        @Override
        public ReportDetailBean createFromParcel(Parcel in) {
            return new ReportDetailBean(in);
        }

        @Override
        public ReportDetailBean[] newArray(int size) {
            return new ReportDetailBean[size];
        }
    };

    public List<Warn> getWarn() {
        return warn;
    }

    public void setWarn(List<Warn> warn) {
        this.warn = warn;
    }

    public List<Alarm> getAlarm() {
        return alarm;
    }

    public void setAlarm(List<Alarm> alarm) {
        this.alarm = alarm;
    }

    public List<Power> getPower() {
        return power;
    }

    public void setPower(List<Power> power) {
        this.power = power;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    /**
     * 预警
     */
    public class Warn implements Parcelable {
        int count;
        int errType;
        int id;
        String switchCode;
        int switchID;
        String switchName;

        Warn(Parcel in) {
            count = in.readInt();
            errType = in.readInt();
            id = in.readInt();
            switchCode = in.readString();
            switchID = in.readInt();
            switchName = in.readString();
        }

        public final Creator<Warn> CREATOR = new Creator<Warn>() {
            @Override
            public Warn createFromParcel(Parcel in) {
                return new Warn(in);
            }

            @Override
            public Warn[] newArray(int size) {
                return new Warn[size];
            }
        };

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getErrType() {
            return errType;
        }

        public void setErrType(int errType) {
            this.errType = errType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSwitchCode() {
            return switchCode;
        }

        public void setSwitchCode(String switchCode) {
            this.switchCode = switchCode;
        }

        public int getSwitchID() {
            return switchID;
        }

        public void setSwitchID(int switchID) {
            this.switchID = switchID;
        }

        public String getSwitchName() {
            return switchName;
        }

        public void setSwitchName(String switchName) {
            this.switchName = switchName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(count);
            dest.writeInt(errType);
            dest.writeInt(id);
            dest.writeString(switchCode);
            dest.writeInt(switchID);
            dest.writeString(switchName);
        }
    }

    /**
     * 告警
     */
    public class Alarm implements Parcelable {
        int count;
        int errType;
        int id;
        String switchCode;
        int switchID;
        String switchName;

        Alarm(Parcel in) {
            count = in.readInt();
            errType = in.readInt();
            id = in.readInt();
            switchCode = in.readString();
            switchID = in.readInt();
            switchName = in.readString();
        }

        public final Creator<Alarm> CREATOR = new Creator<Alarm>() {
            @Override
            public Alarm createFromParcel(Parcel in) {
                return new Alarm(in);
            }

            @Override
            public Alarm[] newArray(int size) {
                return new Alarm[size];
            }
        };

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getErrType() {
            return errType;
        }

        public void setErrType(int errType) {
            this.errType = errType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSwitchCode() {
            return switchCode;
        }

        public void setSwitchCode(String switchCode) {
            this.switchCode = switchCode;
        }

        public int getSwitchID() {
            return switchID;
        }

        public void setSwitchID(int switchID) {
            this.switchID = switchID;
        }

        public String getSwitchName() {
            return switchName;
        }

        public void setSwitchName(String switchName) {
            this.switchName = switchName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(count);
            dest.writeInt(errType);
            dest.writeInt(id);
            dest.writeString(switchCode);
            dest.writeInt(switchID);
            dest.writeString(switchName);
        }
    }

    /**
     * 电量
     */
    public class Power implements Parcelable {
        float count;
        int id;
        String switchCode;
        int switchID;
        String switchName;

        Power(Parcel in) {
            count = in.readFloat();
            id = in.readInt();
            switchCode = in.readString();
            switchID = in.readInt();
            switchName = in.readString();
        }

        public final Creator<Power> CREATOR = new Creator<Power>() {
            @Override
            public Power createFromParcel(Parcel in) {
                return new Power(in);
            }

            @Override
            public Power[] newArray(int size) {
                return new Power[size];
            }
        };

        public float getCount() {
            return count;
        }

        public void setCount(float count) {
            this.count = count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSwitchCode() {
            return switchCode;
        }

        public void setSwitchCode(String switchCode) {
            this.switchCode = switchCode;
        }

        public int getSwitchID() {
            return switchID;
        }

        public void setSwitchID(int switchID) {
            this.switchID = switchID;
        }

        public String getSwitchName() {
            return switchName;
        }

        public void setSwitchName(String switchName) {
            this.switchName = switchName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(count);
            dest.writeInt(id);
            dest.writeString(switchCode);
            dest.writeInt(switchID);
            dest.writeString(switchName);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "ReportDetailBean{" +
                "warn=" + warn +
                ", alarm=" + alarm +
                ", power=" + power +
                '}';
    }
}
