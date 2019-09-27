package com.rejuvee.smartelectric.family.model.bean;

/**
 * switch安全设置的 参数
 * <p>
 */
public class VoltageValue {

    //    private String id;
    private int paramID;
    private float paramValue;

    public int getParamID() {
        return paramID;
    }

    public void setParamID(int paramID) {
        this.paramID = paramID;
    }

    public float getParamValue() {
        return paramValue;
    }

    public void setParamValue(float paramValue) {
        this.paramValue = paramValue;
    }
}
