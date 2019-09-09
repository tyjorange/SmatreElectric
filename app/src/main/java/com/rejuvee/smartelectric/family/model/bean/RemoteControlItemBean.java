package com.rejuvee.smartelectric.family.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchengran on 2018/11/8.
 */

public class RemoteControlItemBean {

    public int viewType;
    public SwitchBean breakerInfoBean;//断路器
    public CollectorBean collectorBean;//集中器

    public static final int VIEW_TYPE_COLLETDEVICE = 0;//集中器
    public static final int VIEW_TYPE_BREAKINFO = 1;//断路器

    public static List<RemoteControlItemBean> convertToRemoteControlItem(List<ElequantityBean> listBreaks) {
        List<RemoteControlItemBean> listData = new ArrayList<>();
        for (ElequantityBean elequantityBean : listBreaks) {
            RemoteControlItemBean remoteControlItemBean = new RemoteControlItemBean();
            remoteControlItemBean.viewType = RemoteControlItemBean.VIEW_TYPE_COLLETDEVICE;
            remoteControlItemBean.collectorBean = elequantityBean.collector;
            listData.add(remoteControlItemBean);
            if (elequantityBean.switchs != null) {
                for (SwitchBean switchBean : elequantityBean.switchs) {
                    remoteControlItemBean = new RemoteControlItemBean();
                    remoteControlItemBean.viewType = RemoteControlItemBean.VIEW_TYPE_BREAKINFO;
                    remoteControlItemBean.breakerInfoBean = switchBean;
                    remoteControlItemBean.collectorBean = elequantityBean.collector;
                    listData.add(remoteControlItemBean);
                }
            }
        }
        return listData;
    }
}