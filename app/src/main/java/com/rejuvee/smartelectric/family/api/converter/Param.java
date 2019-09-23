package com.rejuvee.smartelectric.family.api.converter;

/**
 * 前端 查询 提交的参数
 * web & app
 */
public class Param {
    public final static String DM_FAMILY = "1";
    public final static String DM_COMPANY = "2";
    public final static String YJ_DM_FAMILY = "3";
    public final static String YJ_DM_COMPANY = "4";
    // 登录
    private String username;
    private String password;
    private String newPwd;
    private String nickName;
    private String headImg;// 用户头像
    private String headImg1;//QQ头像 小图
    private String headImg2;//QQ头像 大图
    private String phone;
    private String oldHeadImg;
    //验证码 || 用户换取access_token的code
    private String code;
    //
    private String collectorID;
    private String collectorCode;
    private String setupCode;
    //
    private String switchID;
    private String switchIDs;
    private String switchCode;
    private String switchs;
    //
    private String name;
    private String iconType;
    private String type;
    //
    private String cmdData;
    private String time;
    //
    private String timeControllerID;
    private String weekday;
    private String state;
    private String upload;
    // 过压阈值索引(0,1,2,3)
    private String index;
    private String value;
    // 阈值id 一条或多条
    private String paramID;
    // 指定格式的json字符串,包含所有场景信息
    private String scenes;
    private String sceneID;
    private String sceneSwitchID;
    // 电箱参数设置项
    private String controllerID;
    private String collectorShareID;
    private String baud;
    private String freq;
    private String ranges;
    private String faultFreq;
    private String HBFreq;
    //
    private String pid;
    // 电费
    private String prices;
    private String signalsTypeID;
    private Double gonglv;
    private Integer version;
    private Integer enable;
    private String electricalEquipmentID;
    private String electricalEquipmentIDs;
    // 微信 QQ 登录
    private String access_token;
    private String openid;
    private String unionid;
    // 分页
    private Integer start;
    private Integer length;

    private String setting;

    //集中器版本
    private Integer verMajor;
    private Integer verMinor;
    private Integer ok;// 确认结果 0=不同意 1=同意
    private Integer fileID;// 升级文件ID

    // 报表ID
    private Integer dateListID;

    public Integer getDateListID() {
        return dateListID;
    }

    public void setDateListID(Integer dateListID) {
        this.dateListID = dateListID;
    }

    public String getSetupCode() {
        return setupCode;
    }

    public void setSetupCode(String setupCode) {
        this.setupCode = setupCode;
    }

    public String getSceneSwitchID() {
        return sceneSwitchID;
    }

    public void setSceneSwitchID(String sceneSwitchID) {
        this.sceneSwitchID = sceneSwitchID;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCollectorShareID() {
        return collectorShareID;
    }

    public void setCollectorShareID(String collectorShareID) {
        this.collectorShareID = collectorShareID;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getElectricalEquipmentIDs() {
        return electricalEquipmentIDs;
    }

    public void setElectricalEquipmentIDs(String electricalEquipmentIDs) {
        this.electricalEquipmentIDs = electricalEquipmentIDs;
    }

    public String getElectricalEquipmentID() {
        return electricalEquipmentID;
    }

    public void setElectricalEquipmentID(String electricalEquipmentID) {
        this.electricalEquipmentID = electricalEquipmentID;
    }

    public Double getGonglv() {
        return gonglv;
    }

    public void setGonglv(Double gonglv) {
        this.gonglv = gonglv;
    }

    public String getSwitchIDs() {
        return switchIDs;
    }

    public void setSwitchIDs(String switchIDs) {
        this.switchIDs = switchIDs;
    }

    public String getSignalsTypeID() {
        return signalsTypeID;
    }

    public void setSignalsTypeID(String signalsTypeID) {
        this.signalsTypeID = signalsTypeID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHeadImg1() {
        return headImg1;
    }

    public void setHeadImg1(String headImg1) {
        this.headImg1 = headImg1;
    }

    public String getHeadImg2() {
        return headImg2;
    }

    public void setHeadImg2(String headImg2) {
        this.headImg2 = headImg2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOldHeadImg() {
        return oldHeadImg;
    }

    public void setOldHeadImg(String oldHeadImg) {
        this.oldHeadImg = oldHeadImg;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBaud() {
        return baud;
    }

    public void setBaud(String baud) {
        this.baud = baud;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public String getHBFreq() {
        return HBFreq;
    }

    public void setHBFreq(String HBFreq) {
        this.HBFreq = HBFreq;
    }

    public String getControllerID() {
        return controllerID;
    }

    public void setControllerID(String controllerID) {
        this.controllerID = controllerID;
    }

    public String getSceneID() {
        return sceneID;
    }

    public void setSceneID(String sceneID) {
        this.sceneID = sceneID;
    }

    public String getSwitchs() {
        return switchs;
    }

    public void setSwitchs(String switchs) {
        this.switchs = switchs;
    }

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
    }

    public String getParamID() {
        return paramID;
    }

    public void setParamID(String paramID) {
        this.paramID = paramID;
    }

    public String getSwitchCode() {
        return switchCode;
    }

    public void setSwitchCode(String switchCode) {
        this.switchCode = switchCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTimeControllerID() {
        return timeControllerID;
    }

    public void setTimeControllerID(String timeControllerID) {
        this.timeControllerID = timeControllerID;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCmdData() {
        return cmdData;
    }

    public void setCmdData(String cmdData) {
        this.cmdData = cmdData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public String getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(String collectorID) {
        this.collectorID = collectorID;
    }

    public String getSwitchID() {
        return switchID;
    }

    public void setSwitchID(String switchID) {
        this.switchID = switchID;
    }

    public String getCollectorCode() {
        return collectorCode;
    }

    public void setCollectorCode(String collectorCode) {
        this.collectorCode = collectorCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public Integer getVerMajor() {
        return verMajor;
    }

    public void setVerMajor(Integer verMajor) {
        this.verMajor = verMajor;
    }

    public Integer getVerMinor() {
        return verMinor;
    }

    public void setVerMinor(Integer verMinor) {
        this.verMinor = verMinor;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getFaultFreq() {
        return faultFreq;
    }

    public void setFaultFreq(String faultFreq) {
        this.faultFreq = faultFreq;
    }
}
