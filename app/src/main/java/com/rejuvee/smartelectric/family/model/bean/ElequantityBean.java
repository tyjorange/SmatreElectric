package com.rejuvee.smartelectric.family.model.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/11/14.
 */
/*
/*{
	"code": 1,
	"message": "成功",
	"datasize": 2,
	"data": [{
		"collector": {
			"HBFreq": 45,
			"HBTime": "2018-11-29 16:03:10",
			"baud": 2400,
			"code": "201811061111",
			"collectorID": "C7CD37CA1C03412A98151DBB2F5939FE",
			"faultState": 2,
			"faultTime": "2018-11-29 13:02:34",
			"freq": 15,
			"ip": "2018-11-29T16:02:22.831",
			"name": "索罗内网测试",
			"ranges": 15,
			"setupCode": "a4yemDU2zBJJIS_LOsa",
			"srvIP": "192.168.1.82",
			"srvPort": 55555
		},
		"switchs": [{
			"addTime": "2018-11-22 11:33:25",
			"code": "201809130001",
			"fault": 0,
			"faultState": 2,
			"faultTime": "2018-11-29 10:21:51",
			"frequency": 0,
			"iconType": 8,
			"isLocked": 0,
			"lockedPwd": "",
			"lockedUser": null,
			"name": "空调",
			"sequence": 6,
			"state": 1,
			"switchID": "089D875AF6EF47B78A9FA65534702E52",
			"today": 0,
			"month": 0,
			"lastMonth": 0,
			"year": 0,
			"tbjn": 0,
			"hbjn": 0
		}, {
			"addTime": "2018-11-20 15:44:56",
			"code": "201805040045",
			"fault": 256,
			"faultState": 2,
			"faultTime": "2018-11-29 11:18:27",
			"frequency": 0,
			"iconType": 0,
			"isLocked": 0,
			"lockedPwd": "",
			"lockedUser": null,
			"name": "测试1",
			"sequence": 1,
			"state": 1,
			"switchID": "1",
			"today": 0,
			"month": 0,
			"lastMonth": 0,
			"year": 0,
			"tbjn": 0,
			"hbjn": 0
		}, {*/
public class ElequantityBean {

    public CollectorBean collector;
    public ArrayList<SwitchBean> switchs;

    public ElequantityBean(CollectorBean collector, ArrayList<SwitchBean> switchs) {
        this.collector = collector;
        this.switchs = switchs;
    }

    public CollectorBean getCollector() {
        return collector;
    }

    public void setCollector(CollectorBean collector) {
        this.collector = collector;
    }

    public ArrayList<SwitchBean> getSwitchs() {
        return switchs;
    }

    public void setSwitchs(ArrayList<SwitchBean> switchs) {
        this.switchs = switchs;
    }
}
