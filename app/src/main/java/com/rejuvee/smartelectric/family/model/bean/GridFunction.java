package com.rejuvee.smartelectric.family.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/15.
 */

public class GridFunction  implements Serializable {

    private  int imgid;
    private  String text_device;
    private  String text_room;
    private  boolean flag ;

    public GridFunction(){

    }

    public GridFunction(int imgid, String text_device, String text_room,boolean flag) {
        this.imgid = imgid;
        this.text_device = text_device;
        this.text_room = text_room;
        this.flag = flag ;
    }

    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getText_device() {
        return text_device;
    }

    public void setText_device(String text_device) {
        this.text_device = text_device;
    }

    public String getText_room() {
        return text_room;
    }

    public void setText_room(String text_room) {
        this.text_room = text_room;
    }
}