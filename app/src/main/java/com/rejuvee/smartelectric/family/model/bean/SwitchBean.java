package com.rejuvee.smartelectric.family.model.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.gson.annotations.SerializedName;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;

import java.util.List;

import io.realm.annotations.Ignore;

/**
 * Created by liuchengran on 2018/10/31.
 */
public class SwitchBean implements Parcelable {
    @SerializedName("code")
    private String serialNumber;//断路器唯一标识
    public String name;
    private int iconType;//对应本地图片资源 NativeLine
    private int pid;
    private boolean flag; //是否选中
    private int switchID;//断路器ID
    @SerializedName("state")
    private int switchState;//开关状态 0： 关  1： 开

    private int lockState;//断路器锁状态。 0：未锁  1：已锁
    private int icon;//本地图标RES
    @Ignore
    private String deviceName;//所属设备名
    private List<SwitchBean> child;
    public int fault;
    private int faultState;
    private String temperature;//温度
    public int timerCount;

    // switch 控制执行结果
    private String runCode;
    private String runResult;

    // 节能信息
    private String year;
    private String today;
    private String month;
    private String lastMonth;
    private String lastYearMonth;
    private String tbjn;
    private String hbjn;

    // 断路器版本型号
    private int modelMajor;
    private int modelMinor;
    private int verMajor;
    private int verMinor;
    @Ignore
    public int showDelIcon = View.GONE;

    /**
     * 获取开关断开原因
     *
     * @return
     */
    public static String getSwitchFaultState(Context context, int faultCode) {

        if ((faultCode & (0x01)) > 0) {
            // 第0位:过压
            return context.getResources().getStringArray(R.array.switch_fault_code)[0];
        } else if ((faultCode & (0x01 << 1)) > 0) {
            // 第1位:欠压
            return context.getResources().getStringArray(R.array.switch_fault_code)[1];
        } else if ((faultCode & (0x01 << 2)) > 0) {
            // 第2位:过载
            return context.getResources().getStringArray(R.array.switch_fault_code)[2];
        } else if ((faultCode & (0x01 << 3)) > 0) {
            // 第3位:用电量超额
            return context.getResources().getStringArray(R.array.switch_fault_code)[3];
        } else if ((faultCode & (0x01 << 4)) > 0) {
            // 第4位:定时拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[4];
        } else if ((faultCode & (0x01 << 5)) > 0) {
            // 第5位:远程拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[5];
        } else if ((faultCode & (0x01 << 6)) > 0) {
            // 第6位:超规定自动合闸次数
            return context.getResources().getStringArray(R.array.switch_fault_code)[6];
        } else if ((faultCode & (0x01 << 7)) > 0) {
            // 第7位:快速电流拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[7];
        } else if ((faultCode & (0x01 << 8)) > 0) {
            // 第8位:手动合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[8];
        } else if ((faultCode & (0x01 << 9)) > 0) {
            // 第9位:手动拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[9];
        } else if ((faultCode & (0x01 << 10)) > 0) {
            // 第10位:远程合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[10];
        } else if ((faultCode & (0x01 << 11)) > 0) {
            // 第11位:漏电
            return context.getResources().getStringArray(R.array.switch_fault_code)[11];
        } else if ((faultCode & (0x01 << 12)) > 0) {
            // 第12位:过温
            return context.getResources().getStringArray(R.array.switch_fault_code)[12];
        } else if ((faultCode & (0x01 << 13)) > 0) {
            // 第13位:漏电自检拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[13];
        } else if ((faultCode & (0x01 << 14)) > 0) {
            // 第14位:上级保护合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[14];
        } else if ((faultCode & (0x01 << 15)) > 0) {
            // 第15位:上级保护拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[15];
        } else if ((faultCode & (0x01 << 16)) > 0) {
            // 第16位:定时合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[16];
        } else if ((faultCode & (0x01 << 17)) > 0) {
            // 第17位:漏电自检失败
            return context.getResources().getStringArray(R.array.switch_fault_code)[17];
        } else if ((faultCode & (0x01 << 18)) > 0) {
            // 第18位:开关故障
            return context.getResources().getStringArray(R.array.switch_fault_code)[18];
        } else if ((faultCode & (0x01 << 19)) > 0) {
            // 第19位:过压恢复合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[19];
        } else if ((faultCode & (0x01 << 20)) > 0) {
            // 第20位:欠压恢复合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[20];
        } else if ((faultCode & (0x01 << 21)) > 0) {
            // 第21位:上电合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[21];
        } else if ((faultCode & (0x01 << 22)) > 0) {
            // 第22位:上电拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[22];
        } else if ((faultCode & (0x01 << 23)) > 0) {
            // 第23位:电弧拉闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[23];
        } else if ((faultCode & (0x01 << 24)) > 0) {
            // 第24位:漏电自检恢复合闸
            return context.getResources().getStringArray(R.array.switch_fault_code)[24];
        }
        return "unknow";
    }

    /**
     * 告警预警TYPE名
     *
     * @param context
     * @param paramID
     * @return
     */
    public static String getParamName(Context context, int paramID) {
        switch (paramID) {
            case 0x00000001:
                return context.getResources().getStringArray(R.array.switch_param_code)[0];
            case 0x00000002:
                return context.getResources().getStringArray(R.array.switch_param_code)[1];
            case 0x00000003:
                return context.getResources().getStringArray(R.array.switch_param_code)[2];
            case 0x00000004:
                return context.getResources().getStringArray(R.array.switch_param_code)[3];
            case 0x00000005:
                return context.getResources().getStringArray(R.array.switch_param_code)[4];
            case 0x00000006:
                return context.getResources().getStringArray(R.array.switch_param_code)[5];
            case 0x00000007:
                return context.getResources().getStringArray(R.array.switch_param_code)[6];
            case 0x00000008:
                return context.getResources().getStringArray(R.array.switch_param_code)[7];
            case 0x00000009:
                return context.getResources().getStringArray(R.array.switch_param_code)[8];
            case 0x0000000A:
                return context.getResources().getStringArray(R.array.switch_param_code)[9];
            case 0x0000000B:
                return context.getResources().getStringArray(R.array.switch_param_code)[10];
            case 0x0000000C:
                return context.getResources().getStringArray(R.array.switch_param_code)[11];
            case 0x0000000D:
                return context.getResources().getStringArray(R.array.switch_param_code)[12];
            case 0x0000000E:
                return context.getResources().getStringArray(R.array.switch_param_code)[13];
            case 0x0000000F:
                return context.getResources().getStringArray(R.array.switch_param_code)[14];
            case 0x00000010:
                return context.getResources().getStringArray(R.array.switch_param_code)[15];
            case 0x00000011:
                return context.getResources().getStringArray(R.array.switch_param_code)[16];
            case 0x00000012:
                return context.getResources().getStringArray(R.array.switch_param_code)[17];
            case 0x00000013:
                return context.getResources().getStringArray(R.array.switch_param_code)[18];
            case 0x00000014:
                return context.getResources().getStringArray(R.array.switch_param_code)[19];
            case 0x00000015:
                return context.getResources().getStringArray(R.array.switch_param_code)[20];
            case 0x00000016:
                return context.getResources().getStringArray(R.array.switch_param_code)[21];
            case 0x00000017:
                return context.getResources().getStringArray(R.array.switch_param_code)[22];
            case 0x00000018:
                return context.getResources().getStringArray(R.array.switch_param_code)[23];
            case 0x00000019:
                return context.getResources().getStringArray(R.array.switch_param_code)[24];
            case 0x0000001A:
                return context.getResources().getStringArray(R.array.switch_param_code)[25];
            case 0x0000001B:
                return context.getResources().getStringArray(R.array.switch_param_code)[26];
            case 0x0000001C:
                return context.getResources().getStringArray(R.array.switch_param_code)[27];
            case 0x0000001D:
                return context.getResources().getStringArray(R.array.switch_param_code)[28];
            case 0x0000001E:
                return context.getResources().getStringArray(R.array.switch_param_code)[29];
            case 0x0000001F:
                return context.getResources().getStringArray(R.array.switch_param_code)[31];
            case 0x00000020:
                return context.getResources().getStringArray(R.array.switch_param_code)[32];
            case 0x00000021:
                return context.getResources().getStringArray(R.array.switch_param_code)[33];
            case 0x00000022:
                return context.getResources().getStringArray(R.array.switch_param_code)[34];
        }
        return "unkonw";
    }

    protected SwitchBean(Parcel in) {
        serialNumber = in.readString();
        icon = in.readInt();
        iconType = in.readInt();
        pid = in.readInt();
        flag = in.readByte() != 0;
        switchID = in.readInt();
        switchState = in.readInt();
        lockState = in.readInt();
        deviceName = in.readString();
        name = in.readString();
        lastMonth = in.readString();
        today = in.readString();
        month = in.readString();
        lastYearMonth = in.readString();
        year = in.readString();
        tbjn = in.readString();
        hbjn = in.readString();
        modelMajor = in.readInt();
        modelMinor = in.readInt();
        verMajor = in.readInt();
        verMinor = in.readInt();
        child = in.createTypedArrayList(SwitchBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serialNumber);
        dest.writeInt(icon);
        dest.writeInt(iconType);
        dest.writeInt(pid);
        dest.writeByte((byte) (flag ? 1 : 0));
        dest.writeInt(switchID);
        dest.writeInt(switchState);
        dest.writeInt(lockState);
        dest.writeString(deviceName);
        dest.writeString(name);
        dest.writeString(lastMonth);
        dest.writeString(today);
        dest.writeString(month);
        dest.writeString(lastYearMonth);
        dest.writeString(year);
        dest.writeString(tbjn);
        dest.writeString(hbjn);
        dest.writeInt(modelMajor);
        dest.writeInt(modelMinor);
        dest.writeInt(verMajor);
        dest.writeInt(verMinor);
        dest.writeTypedList(child);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SwitchBean> CREATOR = new Creator<SwitchBean>() {
        @Override
        public SwitchBean createFromParcel(Parcel in) {
            return new SwitchBean(in);
        }

        @Override
        public SwitchBean[] newArray(int size) {
            return new SwitchBean[size];
        }
    };

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTbjn() {
        return tbjn;
    }

    public void setTbjn(String tbjn) {
        this.tbjn = tbjn;
    }

    public String getHbjn() {
        return hbjn;
    }

    public void setHbjn(String hbjn) {
        this.hbjn = hbjn;
    }

    public SwitchBean(String name, String lastMonth, String month, String year) {
        this.name = name;
        this.lastMonth = lastMonth;
        this.month = month;
        this.year = year;
    }

    public int getSwitchID() {
        return switchID;
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }

    public SwitchBean() {
        iconType = 3;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public int getIcon() {
        return NativeLine.LinePictures[iconType % NativeLine.LinePictures.length];
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getSwitchState() {
        return switchState;
    }

    public void toggleSwitchState() {
        Log.e("switchState", switchState + "");
        switchState = (switchState + 1) % 2;
        Log.e("switchState", switchState + "");
    }

    public void setSwitchState(int switchState) {
        this.switchState = switchState;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<SwitchBean> getChild() {
        return child;
    }

    public void setChild(List<SwitchBean> child) {
        this.child = child;
    }

    public String getRunCode() {
        return runCode;
    }

    public void setRunCode(String runCode) {
        this.runCode = runCode;
    }

    public String getRunResult() {
        return runResult;
    }

    public void setRunResult(String runResult) {
        this.runResult = runResult;
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getTimerCount() {
        return timerCount;
    }

    public void setTimerCount(int timerCount) {
        this.timerCount = timerCount;
    }

    public int getModelMajor() {
        return modelMajor;
    }

    public void setModelMajor(int modelMajor) {
        this.modelMajor = modelMajor;
    }

    public int getModelMinor() {
        return modelMinor;
    }

    public void setModelMinor(int modelMinor) {
        this.modelMinor = modelMinor;
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

    public String getLastYearMonth() {
        return lastYearMonth;
    }

    public void setLastYearMonth(String lastYearMonth) {
        this.lastYearMonth = lastYearMonth;
    }

    public int getPid() {
        return pid;
    }

    public SwitchBean setPid(int pid) {
        this.pid = pid;
        return this;
    }
}
