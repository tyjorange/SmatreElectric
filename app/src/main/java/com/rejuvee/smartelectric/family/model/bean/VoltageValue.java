package com.rejuvee.smartelectric.family.model.bean;

/**
 * switch安全设置的 参数
 * <p>
 * 过流：0x00000011   过压：0x00000005  欠压：0x0000000D
 * 电量下限 0x00000018 电量上限 0x00000019
 */

public class VoltageValue {

    private String id;
    private String paramID;
    private String paramValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParamID() {
        return paramID;
    }

    public void setParamID(String paramID) {
        this.paramID = paramID;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
