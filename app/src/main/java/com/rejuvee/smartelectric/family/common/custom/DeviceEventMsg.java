package com.rejuvee.smartelectric.family.common.custom;

/**
 * Created by liuchengran on 2018/11/2.
 */

public class DeviceEventMsg extends BaseEventMsg {
    public static final int EVENT_GET_CONCENTRATOR = 0;//获取集中器
    public static final int EVENT_ADD_CONCENTRATOR = 1;//添加集中器
    public static final int EVENT_REMOVE_CONCENTRATOR = 2;//删除集中器
    public static final int EVENT_GET_CIRCUITBREAK = 3;//获取某个集中器下断路器信息
    public static final int EVENT_ADD_CIRCUITBREAK = 4;//添加断路器
    public static final int EVENT_GET_ALL_CIRCUITBREAK = 5;//获取某个用户下所有断路器
    public static final int EVENT_REMOVE_CIRCUITBREAK = 7;//删除集中下某个开关
    public static final int EVENT_SWITCH_REMOTE_CONTROL = 8;//远程控制开关

    public static final int EVENT_REFRESH_CONCENTRATOR = 6;//刷新重新获取集中器
    public static final int EVENT_REFRESH_CIRCUITBREAK_STATE = 9;//刷新开关状态
    public static final int EVENT_REFRESH_CONCENTRATOR_STATE = 10;//刷新集中器状态
    public static final int EVENT_CHECK_ONLINE = 11;//检查集中器是否在线

    public static final int EVENT_GET_BREAK_DATA = 12;//获取某个线路的详细数据

    public static final int EVENT_GET_ALL_BREAK = 13;//获取所有的线路数据
    public static final int EVENT_ADD_UPDATEELET = 14;//添加更新电器

    public static final int EVENT_GET_ALLELES = 15;//获取所有电器
    public static final int EVENT_DELET_ELES = 16;//删除电器

    public static final int EVENT_GET_FAULTALARM = 17; // 获取故障告警
    public static final int EVENT_GET_ELECTRISTATE = 18;   //获取电器状态

    public static final int EVENT_GET_ALLBRAK = 19;   //获取所有电箱和线路
    public static final int EVENT_GET_SWITCH = 20;//获取某集中器的线路

    public DeviceEventMsg(int eventType) {
        this.eventType = eventType;
    }


}
