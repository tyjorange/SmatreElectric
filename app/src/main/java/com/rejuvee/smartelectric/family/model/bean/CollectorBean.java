package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.ObservableBoolean;

import com.google.gson.annotations.SerializedName;
import com.rejuvee.smartelectric.family.R;

import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * Created by liuchengran on 2018/10/31.
 */

/*"collector": {
        "HBFreq": 45,
        "HBTime": "2018-11-29 16:03:10",
        "baud": 2400,
        "code": "201811061111",
        "collectorID": "C7CD37CA1C03412A98151DBB2F5939FE",
        "faultState": 2,
        "faultTime": "2018-11-29 13:02:34",
        "freq": 15,
        "ip": "2018-11-29T16:02:22.831",
        "name": "索罗内网测试",
        "ranges": 15,
        "setupCode": "a4yemDU2zBJJIS_LOsa",
        "srvIP": "192.168.1.82",
        "srvPort": 55555
        },*/

/*"collector": {
        "HBFreq": 90,
        "active": 1,
        "activeTime": {
        "date": 27,
        "day": 3,
        "hours": 11,
        "minutes": 48,
        "month": 1,
        "nanos": 0,
        "seconds": 13,
        "time": 1551239293000,
        "timezoneOffset": -480,
        "year": 119
        },
        "baud": 2400,
        "code": "201811062222",
        "collectorID": "D0B487F43EFB4C3B9B988D2246B57A95",
        "faultState": 2,
        "faultTime": "2019-02-26 10:39:48",
        "freq": 10,
        "ioType": 2,
        "ip": "175.8.95.109",
        "name": "内网测试22",
        "ranges": 10,
        "setupCode": "Gtm7ebIeyC4",
        "srvIP": "116.62.38.203",
        "srvPort": 55555,
        "verMajor": 0,
        "verMinor": 0,
        "online": 1
        },*/



public class CollectorBean implements Parcelable {

    @PrimaryKey
    private String deviceMac;
    @SerializedName("name")
    private String deviceName;
    private String collectorID;
    private String code;
    private int baud;
    private int freq;//采集频率 分钟
    private int ranges;//变化幅度范围 百分比值
    @SerializedName("HBFreq")
    private int heartrate;
    private int online;

    private int fault;
    private int faultState;
    private int faultFreq;// 故障码频率
    public int beShared;// 1:别人分享给我的  0： 自己绑定的
    public int share;//beShare为0时，表示分享给多少个人
    public int enable;//集中器操作权限 0 没有 1 有
    //集中器版本信息
    private int verMajor;
    private int verMinor;
    private int fileID;
    private int verMajorNew;
    private int verMinorNew;
    private String text;

    private List<UserMsg> shareUsers;
    public UserMsg ownerUser;

    public int ioType;
    private boolean flag; //是否选中
//    public ObservableBoolean isAddBtn = new ObservableBoolean(false);

//    public CollectorBean setIsAddBtn(boolean fired) {
//        isAddBtn.set(fired);
//        return this;
//    }

    public CollectorBean(Parcel in) {
        deviceMac = in.readString();
        deviceName = in.readString();
        collectorID = in.readString();
        code = in.readString();
        baud = in.readInt();
        freq = in.readInt();
        ranges = in.readInt();
        heartrate = in.readInt();
        online = in.readInt();
        fault = in.readInt();
        faultState = in.readInt();
        beShared = in.readInt();
        share = in.readInt();
        enable = in.readInt();
        shareUsers = in.createTypedArrayList(UserMsg.CREATOR);
        ownerUser = in.readParcelable(UserMsg.class.getClassLoader());
        ioType = in.readInt();
        flag = in.readByte() != 0;
        verMajor = in.readInt();
        verMinor = in.readInt();
        fileID = in.readInt();
        verMajorNew = in.readInt();
        verMinorNew = in.readInt();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceMac);
        dest.writeString(deviceName);
        dest.writeString(collectorID);
        dest.writeString(code);
        dest.writeInt(baud);
        dest.writeInt(freq);
        dest.writeInt(ranges);
        dest.writeInt(heartrate);
        dest.writeInt(online);
        dest.writeInt(fault);
        dest.writeInt(faultState);
        dest.writeInt(beShared);
        dest.writeInt(share);
        dest.writeInt(enable);
        dest.writeTypedList(shareUsers);
        dest.writeParcelable(ownerUser, flags);
        dest.writeInt(ioType);
        dest.writeByte((byte) (flag ? 1 : 0));
        dest.writeInt(verMajor);
        dest.writeInt(verMinor);
        dest.writeInt(fileID);
        dest.writeInt(verMajorNew);
        dest.writeInt(verMinorNew);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectorBean> CREATOR = new Creator<CollectorBean>() {
        @Override
        public CollectorBean createFromParcel(Parcel in) {
            return new CollectorBean(in);
        }

        @Override
        public CollectorBean[] newArray(int size) {
            return new CollectorBean[size];
        }
    };

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(String collectorID) {
        this.collectorID = collectorID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getBaud() {
        return baud;
    }

    public void setBaud(int baud) {
        this.baud = baud;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getRanges() {
        return ranges;
    }

    public void setRanges(int ranges) {
        this.ranges = ranges;
    }

    public int getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(int heartrate) {
        this.heartrate = heartrate;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getFault() {
        return fault;
    }

    public void setFault(int fault) {
        this.fault = fault;
    }

    public int getFaultState() {
        return faultState;
    }

    public void setFaultState(int faultState) {
        this.faultState = faultState;
    }

    public int getBeShared() {
        if (beShared == 1) {
            return (R.drawable.gongxiangjinlai);
        } else {
            if (beShared == 0 && share > 0) {
                return (R.drawable.gongxiangchuqu);
            }
        }
        return R.drawable.empty;
    }

    public void setBeShared(int beShared) {
        this.beShared = beShared;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public List<UserMsg> getShareUsers() {
        return shareUsers;
    }

    public void setShareUsers(List<UserMsg> shareUsers) {
        this.shareUsers = shareUsers;
    }

    public UserMsg getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(UserMsg ownerUser) {
        this.ownerUser = ownerUser;
    }

    public int getIoType() {
        return getDeviceTypeImg(ioType);
    }

    public void setIoType(int ioType) {
        this.ioType = ioType;
    }

    public int getVerMajor() {
        return verMajor;
    }

    public void setVerMajor(int verMajor) {
        this.verMajor = verMajor;
    }

    public int getVerMinor() {
        return verMinor;
    }

    public void setVerMinor(int verMinor) {
        this.verMinor = verMinor;
    }

    public int getVerMajorNew() {
        return verMajorNew;
    }

    public void setVerMajorNew(int verMajorNew) {
        this.verMajorNew = verMajorNew;
    }

    public int getVerMinorNew() {
        return verMinorNew;
    }

    public void setVerMinorNew(int verMinorNew) {
        this.verMinorNew = verMinorNew;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public int getFaultFreq() {
        return faultFreq;
    }

    public void setFaultFreq(int faultFreq) {
        this.faultFreq = faultFreq;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static String getDeviceType(int ioType) {
        if (ioType == 0) {
            return "UNKONW";
        } else if (ioType == 1) {
            return "GPRS";
        } else if (ioType == 2) {
            return "WIFI";
        } else if (ioType == 3) {
            return "ETH";
        } else if (ioType == 4) {
            return "NB";
        } else if (ioType == 5) {
            return "LoRa";
        } else if (ioType == 6) {
            return "4G";
        } else if (ioType == 7) {
            return "Mesh";
        } else if (ioType == 8) {
            return "4G.A";
        } else {
            return "UNKONW";
        }
    }

    //        0 -- 未知
    //        1 -- GPRS
    //        2 -- WIFI
    //        3 -- ETH
    //        4 -- NB-iot
    //        5 -- LoRa
    //        6 -- mesh
    //        7 -- 4g
    private int getDeviceTypeImg(int ioType) {
        if (ioType == 0) {
            return R.drawable.io_weizhi;
        } else if (ioType == 1) {
            return R.drawable.io_gprs;
        } else if (ioType == 2) {
            return R.drawable.io_wifi;
        } else if (ioType == 3) {
            return R.drawable.io_eth;
        } else if (ioType == 4) {
            return R.drawable.io_nb_iot;
        } else if (ioType == 5) {
            return R.drawable.io_lora;
        } else if (ioType == 6) {
            return R.drawable.io_4g;
        } else if (ioType == 7) {
            return R.drawable.io_mesh;
        } else if (ioType == 8) {
            return R.drawable.io_4g_a;
        } else {
            return R.drawable.io_weizhi;
        }
    }
}
