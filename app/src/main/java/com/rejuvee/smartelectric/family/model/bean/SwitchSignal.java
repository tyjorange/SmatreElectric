package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuchengran on 2017/12/18.
 */

public class SwitchSignal implements Parcelable{
    private float voltage;//电压
    private float electric;//电流
    private float activePower;//有功功率
    private float reactivePower;//无功功率
    private float qElectric;//电量
    private float powerFactor;//功率因素
    private float activePowerCurMonth;//当月有功功率
    private float reactivePowerCurMonth;//当月无功功率

    public SwitchSignal() {

    }
    protected SwitchSignal(Parcel in) {
        voltage = in.readFloat();
        electric = in.readFloat();
        activePower = in.readFloat();
        reactivePower = in.readFloat();
        qElectric = in.readFloat();
        powerFactor = in.readFloat();
        activePowerCurMonth = in.readFloat();
        reactivePowerCurMonth = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(voltage);
        dest.writeFloat(electric);
        dest.writeFloat(activePower);
        dest.writeFloat(reactivePower);
        dest.writeFloat(qElectric);
        dest.writeFloat(powerFactor);
        dest.writeFloat(activePowerCurMonth);
        dest.writeFloat(reactivePowerCurMonth);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SwitchSignal> CREATOR = new Creator<SwitchSignal>() {
        @Override
        public SwitchSignal createFromParcel(Parcel in) {
            return new SwitchSignal(in);
        }

        @Override
        public SwitchSignal[] newArray(int size) {
            return new SwitchSignal[size];
        }
    };

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public float getElectric() {
        return electric;
    }

    public void setElectric(float electric) {
        this.electric = electric;
    }

    public float getActivePower() {
        return activePower;
    }

    public void setActivePower(float activePower) {
        this.activePower = activePower;
    }

    public float getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(float reactivePower) {
        this.reactivePower = reactivePower;
    }

    public float getqElectric() {
        return qElectric;
    }

    public void setqElectric(float qElectric) {
        this.qElectric = qElectric;
    }

    public float getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(float powerFactor) {
        this.powerFactor = powerFactor;
    }

    public float getActivePowerCurMonth() {
        return activePowerCurMonth;
    }

    public void setActivePowerCurMonth(float activePowerCurMonth) {
        this.activePowerCurMonth = activePowerCurMonth;
    }

    public float getReactivePowerCurMonth() {
        return reactivePowerCurMonth;
    }

    public void setReactivePowerCurMonth(float reactivePowerCurMonth) {
        this.reactivePowerCurMonth = reactivePowerCurMonth;
    }
}
