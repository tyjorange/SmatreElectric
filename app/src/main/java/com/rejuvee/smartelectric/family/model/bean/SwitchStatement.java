package com.rejuvee.smartelectric.family.model.bean;

import java.math.BigDecimal;

/**
 * Created by SH on 2017/12/19.
 * 断路器统计数据
 */

public class SwitchStatement {
    private String value;//电量
    private String code;//
    private String collector;//
    private String frequency;//
    private int iconType;//
    private String name;//
    private String time;//
    private String price;//电费

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
