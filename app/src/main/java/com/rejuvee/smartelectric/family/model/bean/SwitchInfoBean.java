package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.rejuvee.smartelectric.family.common.NativeLine;

/**
 *
 * 断路器信息
 * Created by Administrator on 2017/12/26.
 *   "code": "010000000000",
 "collector": {
 "baud": 9600,
 "code": "0000001",
 "collectorID": "1",
 "freq": 0,
 "name": "控制器1",
 "range": 0
 },
 "frequency": 0,
 "iconType": 1,
 "name": "客厅",
 "state": 0,
 "switchID": "1"
 */

public class SwitchInfoBean implements Parcelable {
    private String code;
    private int iconType;
    private String name;
    private int state;
    private String switchID;

    private CollectorBean collector;
    private boolean flag; //是否选中,用于UI选中标记

    public SwitchInfoBean(int cmdData, String switchID) {
        state = cmdData;
        this.switchID = switchID;
    }

    public SwitchInfoBean() {

    }

    protected SwitchInfoBean(Parcel in) {
        code = in.readString();
        iconType = in.readInt();
        name = in.readString();
        state = in.readInt();
        switchID = in.readString();
        collector = in.readParcelable(CollectorBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeInt(iconType);
        dest.writeString(name);
        dest.writeInt(state);
        dest.writeString(switchID);
        dest.writeParcelable(collector, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SwitchInfoBean> CREATOR = new Creator<SwitchInfoBean>() {
        @Override
        public SwitchInfoBean createFromParcel(Parcel in) {
            return new SwitchInfoBean(in);
        }

        @Override
        public SwitchInfoBean[] newArray(int size) {
            return new SwitchInfoBean[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    public int getIcon() {
        return NativeLine.LinePictures[iconType % NativeLine.LinePictures.length];
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSwitchID() {
        return switchID;
    }

    public void setSwitchID(String switchID) {
        this.switchID = switchID;
    }

    public CollectorBean getCollector() {
        return collector;
    }

    public void setCollector(CollectorBean collector) {
        this.collector = collector;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
