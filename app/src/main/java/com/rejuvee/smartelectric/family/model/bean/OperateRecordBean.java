package com.rejuvee.smartelectric.family.model.bean;

/**
 * Created by Administrator on 2018/12/3.
 */

public class OperateRecordBean  {

    public String name ;
    public String time;
    public String state;

    public OperateRecordBean(String name, String time, String state) {
        this.name = name;
        this.time = time;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
