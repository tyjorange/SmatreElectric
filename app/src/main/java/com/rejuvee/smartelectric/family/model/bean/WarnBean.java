package com.rejuvee.smartelectric.family.model.bean;

/**
 * {
 * "code":1,
 * "message":"成功",
 * "datasize":1,
 * "data":[
 * {
 * "id":1,
 * "switchID":1001,
 * "name":"照明",
 * "paramID":11,
 * "paramValue":23,
 * "time":"2019-09-04 09:53:02",
 * "warningValue":20
 * }
 * ]
 * }
 */
public class WarnBean {
    private int id;
    private int switchID;
    private String name;
    private String code;
    private int paramID;
    private double paramValue;
    private String time;
    private double warningValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSwitchID() {
        return switchID;
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParamID() {
        return paramID;
    }

    public void setParamID(int paramID) {
        this.paramID = paramID;
    }

    public double getParamValue() {
        return paramValue;
    }

    public void setParamValue(double paramValue) {
        this.paramValue = paramValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(double warningValue) {
        this.warningValue = warningValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
