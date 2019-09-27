package com.rejuvee.smartelectric.family.common;

/**
 * 什么玩意
 * Created by liuchengran on 2017/8/30.
 */
public class ListSetingItem {
    public static final int ITEM_VIEW_TYPE_NORMAL = 0;
    public static final int ITEM_VIEW_TYPE_SELECT_1 = 1;
    public static final int ITEM_VIEW_TYPE_SELECT_2 = 2;
    public static final int ITEM_VIEW_TYPE_SELECT_3 = 3;
    public static final int ITEM_VIEW_TYPE_SELECT_4 = 4;
    public static final int ITEM_VIEW_TYPE_EDIT_0 = 5;
    public static final int ITEM_VIEW_TYPE_CONNECT = 6;
    public static final int ITEM_VIEW_TYPE_DELETE = 7;
    public static final int ITEM_VIEW_TYPE_SWITCH = 8;
    public static final int ITEM_VIEW_TYPE_CHECK = 9;
    public static final int ITEM_VIEW_TYPE_SWITCH1 = 10;
    public static final int ITEM_VIEW_TYPE_LINEDETAIL1 = 11;
    public static final int ITEM_VIEW_TYPE_LINEDETAIL2 = 12;
    public static final int ITEM_VIEW_TYPE_EMPTY = 13;


    public static final int VIEW_TYPE_COUNT = 14;

    private String content;
    private boolean isSwitch;
    private int viewType;
    private int isEnable;// switch控制权限
    private String id;
    private boolean isConnect = false;
    private boolean isChecked = false;
    private String desc;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }
}
