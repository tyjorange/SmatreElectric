package com.rejuvee.smartelectric.family.model.bean;

/**
 * Created by liuchengran on 2018/1/4.
 *
 *  场景下断路器信息
 * "cmdData": 1,
 "id": "1",
 "switchs": {
 "code": "010000000000",
 "collector": {
 "baud": 9600,
 "code": "0000001",
 "collectorID": "1",
 "freq": 0,
 "name": "控制器1",
 "ranges": 0
 },
 "frequency": 0,
 "iconType": 1,
 "name": "客厅",
 "state": 0,
 "switchID": "1"
 }
 *
 * */
public class SceneItemBean {
    private int cmdData;//场景下断路器是开or关
    private String id;//场景下断路器id
    private SwitchInfoBean switchs;

    public int getCmdData() {
        return cmdData;
    }

    public void setCmdData(int cmdData) {
        this.cmdData = cmdData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SwitchInfoBean getSwitchs() {
        return switchs;
    }

    public void setSwitchs(SwitchInfoBean switchs) {
        this.switchs = switchs;
    }
}
