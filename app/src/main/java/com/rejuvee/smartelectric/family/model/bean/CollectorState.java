package com.rejuvee.smartelectric.family.model.bean;

import java.util.List;

import io.realm.RealmModel;

/**
 * 集中器 在线状态
 * 集中器所有线路的 开关状态
 */
public class CollectorState implements RealmModel {
    private Integer collectorState;
    private List<SwitchBean> switchState;

    public Integer getCollectorState() {
        return collectorState;
    }

    public void setCollectorState(Integer collectorState) {
        this.collectorState = collectorState;
    }

    public List<SwitchBean> getSwitchState() {
        return switchState;
    }

    public void setSwitchState(List<SwitchBean> switchState) {
        this.switchState = switchState;
    }
}
