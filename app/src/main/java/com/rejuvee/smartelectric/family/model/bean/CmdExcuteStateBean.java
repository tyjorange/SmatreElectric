package com.rejuvee.smartelectric.family.model.bean;

/**
 * Created by liuchengran on 2018/3/8.
 *
 * 断路器开关命令执行状态
 */

/**
 * code":1,"genTime":"2018-03-08 10:30:36","id":"1","returnTime":null,"state":0,"switchID":"1"
 */
public class CmdExcuteStateBean {
    private int code;
    private String genTime;
    private String returnTime;
    private int state;
    private String switchID;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getGenTime() {
        return genTime;
    }

    public void setGenTime(String genTime) {
        this.genTime = genTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSwitchID() {
        return switchID;
    }

    public void setSwitchID(String switchID) {
        this.switchID = switchID;
    }
}
