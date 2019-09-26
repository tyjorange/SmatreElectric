package com.rejuvee.smartelectric.family.model.bean;

import java.math.BigDecimal;

/**
 * 断路器统计数据
 * 我的电表
 * 电量+电费
 */
public class SwitchStatementBean {
    private String value;//电量
    private String price;//电费
    private int switchID;
    private int pid;
    private String code;//线路编码
    private String name;//线路名称
    private int iconType;//线路图标

    //    private String collector;//
    //    private String frequency;//
//    private String time;//

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharge() {
        return value;
    }


    public String getValue() {
        return value;
    }

    public double getShowValue() {
        return new BigDecimal(Double.parseDouble(value)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getShowPrice() {
        return new BigDecimal(Double.parseDouble(price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSwitchID() {
        return switchID;
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
