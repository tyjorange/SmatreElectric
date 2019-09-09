package com.rejuvee.smartelectric.family.model.bean;

/**
 * Created by Administrator on 2019/2/27.
 */

public class LineParameter  {

    private String name ;
    private int ischoice; //0 选中   1 未选中

    public LineParameter(String name, int ischoice) {
        this.name = name;
        this.ischoice = ischoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIschoice() {
        return ischoice;
    }

    public void setIschoice(int ischoice) {
        this.ischoice = ischoice;
    }
}
