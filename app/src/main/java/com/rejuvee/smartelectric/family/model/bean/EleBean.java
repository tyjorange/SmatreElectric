package com.rejuvee.smartelectric.family.model.bean;

/**
 * {
 * "code":1,
 * "message":"成功",
 * "datasize":2,
 * "data":[
 * {
 * "gonglv":50,
 * "id":1,
 * "name":"Asdf34",
 * "switchID":1001,
 * "switchName":"照明"
 * },
 * {
 * "gonglv":44,
 * "id":3,
 * "name":"asdfsadf",
 * "switchID":1001,
 * "switchName":"照明"
 * }
 * ]
 * }
 */
@Deprecated
public class EleBean {
    double gonglv;
    int id;
    String name;
    int switchID;
    String switchName;

    public double getGonglv() {
        return gonglv;
    }

    public void setGonglv(double gonglv) {
        this.gonglv = gonglv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSwitchID() {
        return switchID;
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }
}
