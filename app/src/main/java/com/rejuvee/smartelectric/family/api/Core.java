package com.rejuvee.smartelectric.family.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.base.frame.net.ActionCallbackListener;
import com.base.frame.net.ApiResponse;
import com.base.frame.net.retrofit.converter.gson.GsonConverterFactory;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.converter.ConverterFactory;
import com.rejuvee.smartelectric.family.api.converter.Param;
import com.rejuvee.smartelectric.family.common.AppGlobalConfig;
import com.rejuvee.smartelectric.family.model.bean.ChartItemBean;
import com.rejuvee.smartelectric.family.model.bean.ChartListItemBean;
import com.rejuvee.smartelectric.family.model.bean.CmdExcuteStateBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorState;
import com.rejuvee.smartelectric.family.model.bean.CollectorUpgradeInfo;
import com.rejuvee.smartelectric.family.model.bean.ControllerId;
import com.rejuvee.smartelectric.family.model.bean.EleBean;
import com.rejuvee.smartelectric.family.model.bean.ElequantityBean;
import com.rejuvee.smartelectric.family.model.bean.Headimg;
import com.rejuvee.smartelectric.family.model.bean.MyEleBean;
import com.rejuvee.smartelectric.family.model.bean.RecordBean;
import com.rejuvee.smartelectric.family.model.bean.ReportBean;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;
import com.rejuvee.smartelectric.family.model.bean.SceneItemBean;
import com.rejuvee.smartelectric.family.model.bean.SignalPeakValleyValue;
import com.rejuvee.smartelectric.family.model.bean.SignalType;
import com.rejuvee.smartelectric.family.model.bean.SignalValue;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchInfoBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchSignalItem;
import com.rejuvee.smartelectric.family.model.bean.SwitchStatementBean;
import com.rejuvee.smartelectric.family.model.bean.ThirdPartyInfo;
import com.rejuvee.smartelectric.family.model.bean.TimePrice;
import com.rejuvee.smartelectric.family.model.bean.TimeTaskBean;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.model.bean.UserPushSetting;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;
import com.rejuvee.smartelectric.family.model.bean.WXAccessTokenRet;
import com.rejuvee.smartelectric.family.model.bean.WarnBean;
import com.rejuvee.smartelectric.family.model.bean.WxSubscribed;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by SH on 2017/12/19.
 * /usr/apache-tomcat-8.5.11/base_sample/webapps/ROOT/apk
 */

public class Core {
    @SuppressLint("StaticFieldLeak")
    private static Core self;
    private static Api api;
    private static final String TAG = "Core";
    public static final String SALT = "";// MD5加密盐
    //    public static final String IP = "192.168.2.120"; //116.62.30.12   "192.168.1.178"    38.203
//    public static final String PORT = "8090";  //8090  80
    private int defaultTimeOut = 30;
    //    private Context mLastContext;
    private static Retrofit retrofit;
    static String mJSessionId = "";
    private static Context context;

    private Core() {
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder().connectTimeout(defaultTimeOut, TimeUnit.SECONDS)
                        .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)
                        .readTimeout(defaultTimeOut, TimeUnit.SECONDS)
                        .build())
                .baseUrl(AppGlobalConfig.HTTP_URL)
                .addConverterFactory(ConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public static Core instance(Context context) {
        Core.context = context;
        if (self == null) {
            self = new Core();
        }
        if (!retrofit.baseUrl().toString().equals(AppGlobalConfig.HTTP_URL)) {
            retrofit = retrofit.newBuilder().baseUrl(AppGlobalConfig.HTTP_URL).build();
            api = retrofit.create(Api.class);
            Log.d(TAG, "地址改变，重新retrofit");
        }
        //loadingDlg = new LoadingDlg(context, -1);
        return self;
    }

    public Api getApi() {
        return api;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Call<?> Login(String userName, String phone, String version, String password, ActionCallbackListener<AccountInfo> listener) {
        Param param = new Param();
        param.setUsername(userName);
        param.setPhone(phone);
        param.setPassword(password);
        param.setVersion(Integer.valueOf(version));
        Call<ApiResponse<AccountInfo>> call = api.login(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> ThirdPartLogin(ThirdPartyInfo thirdPartyInfo, ActionCallbackListener<AccountInfo> listener) {
        Call<ApiResponse<AccountInfo>> call = null;
        Param param = new Param();
        if (thirdPartyInfo.loginType == ThirdPartyInfo.LOGIN_WEIXIN) {
//            param.setAccess_token(thirdPartyInfo.accessToken);
//            param.setOpenid(thirdPartyInfo.openId);
            param.setCode(thirdPartyInfo.code);
            param.setType(thirdPartyInfo.type);
            call = api.loginWX(mJSessionId, param);
        } else if (thirdPartyInfo.loginType == ThirdPartyInfo.LOGIN_QQ) {
            param.setNickName(thirdPartyInfo.nickName);
            param.setOpenid(thirdPartyInfo.openId);
            param.setHeadImg1(thirdPartyInfo.headImgUrl1);
            param.setHeadImg2(thirdPartyInfo.headImgUrl2);
            param.setUnionid(thirdPartyInfo.unionid);
            call = api.loginQQ(mJSessionId, param);
        }
        enqueue(call, listener);
        return call;
    }

    public Call<?> ThirdPartBind(ThirdPartyInfo thirdPartyInfo, ActionCallbackListener<Void> listener) {
        Call<ApiResponse<Void>> call = null;
        Param param = new Param();
        if (thirdPartyInfo.bindType == ThirdPartyInfo.BIND_WEIXIN) {
//            param.setAccess_token(thirdPartyLogin.accessToken);
//            param.setOpenid(thirdPartyLogin.openId);
            param.setCode(thirdPartyInfo.code);
            param.setType(thirdPartyInfo.type);
            call = api.bindWX(mJSessionId, param);
        } else if (thirdPartyInfo.bindType == ThirdPartyInfo.BIND_QQ) {
            param.setNickName(thirdPartyInfo.nickName);
            param.setOpenid(thirdPartyInfo.openId);
            param.setHeadImg1(thirdPartyInfo.headImgUrl1);
            param.setHeadImg2(thirdPartyInfo.headImgUrl2);
            param.setUnionid(thirdPartyInfo.unionid);
            call = api.bindQQ(mJSessionId, param);
        }
        enqueue(call, listener);
        return call;
    }

    public Call<?> unBindWX(ActionCallbackListener<Void> listener) {
        Call<ApiResponse<Void>> call = api.unBindWX(mJSessionId);
        enqueue(call, listener);
        return call;
    }

    public Call<?> unBindQQ(ActionCallbackListener<Void> listener) {
        Call<ApiResponse<Void>> call = api.unBindQQ(mJSessionId);
        enqueue(call, listener);
        return call;
    }

    public Call<?> Register(String userName, String password, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setUsername(userName);
        param.setPassword(password);
        Call<ApiResponse<Void>> call = api.register(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> RegisterByPhone(String phone, String userName, String code, String password, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setPhone(phone);
        param.setUsername(userName);
        param.setPassword(password);
        param.setCode(code);
        Call<ApiResponse<Void>> call = api.registByPhone(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> isPhoneRegister(String phone, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setPhone(phone);
        Call<ApiResponse<Void>> call = api.validatePhone(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getPhoneCode(String phone, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setPhone(phone);
        Call<ApiResponse<Void>> call = api.getCode(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> updatePwd(String newPwd, String password, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setPassword(password);
        param.setNewPwd(newPwd);
        Call<ApiResponse<Void>> call = api.updatePwd(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> resetPwd(String phone, String code, String password, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setPhone(phone);
        param.setPassword(password);
        param.setCode(code);
        Call<ApiResponse<Void>> call = api.resetPwd(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getAllTimeTask(String collectorID, ActionCallbackListener<List<TimeTaskBean>> listener) {
        Param param = new Param();
        param.setCollectorID(collectorID);
        Call<ApiResponse<List<TimeTaskBean>>> call = api.getDeviceAllTimeTask(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> findTimeControllerBySwitch(String switchId, ActionCallbackListener<List<TimeTaskBean.TimeTask>> listener) {
        Param param = new Param();
        param.setSwitchID(switchId);
        Call<ApiResponse<List<TimeTaskBean.TimeTask>>> call = api.findTimeControllerBySwitch(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> bindDevice(String setupCode, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSetupCode(setupCode);
        Call<ApiResponse<Void>> call = api.bindDevice(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> unbindDevice(String deviceId, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setCollectorCode(deviceId);
        Call<ApiResponse<Void>> call = api.unBindDevice(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    //插入或更新一个时间任务
    public Call<?> updateOrInsertTask(String taskId, String breakId, String time, int weekDay,
                                      int state, int cmdData, int upload, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setTimeControllerID(taskId);
        param.setSwitchID(breakId);
        param.setTime(time);
        param.setWeekday(String.valueOf(weekDay));
        param.setState(String.valueOf(state));
        param.setCmdData(String.valueOf(cmdData));
        param.setUpload(String.valueOf(upload));
        Call<ApiResponse<Void>> call = api.updateOrInsertTimeTask(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    //删除一个定时任务
    public Call<?> deleteTimeTask(String timeTaskid, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setTimeControllerID(timeTaskid);
        Call<ApiResponse<Void>> call = api.deleteTimeTask(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取控制器
     *
     * @param
     * @param listener
     * @return
     */
    public Call<?> getCollector(ActionCallbackListener<List<CollectorBean>> listener) {
        Call<ApiResponse<List<CollectorBean>>> call = api.getCollector(mJSessionId);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取控制器
     *
     * @param
     * @return
     */
//    public Observable<ApiResponse<List<CollectorBean>>> rxGetCollector() {
//        try {
//            return api.rxGetCollector(mJSessionId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 获取断路器
     *
     * @param collectorCode
     * @param hierarchy     是否排序成树状图
     * @param listener
     * @return
     */
    public Call<?> getSwitchByCollector(String collectorCode, String hierarchy, ActionCallbackListener<List<SwitchBean>> listener) {
        Param param = new Param();
        param.setType(hierarchy);
        param.setCollectorCode(collectorCode);
        Call<ApiResponse<List<SwitchBean>>> call = api.getSwitchByCollector(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Observable<ApiResponse<SwitchBean>> rxGetSwitch(String swicthId) {
        Param param = new Param();
        param.setSwitchID(swicthId);
        try {
            return api.getBreakers(mJSessionId, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Call<?> getAllBreakersByUser(ActionCallbackListener<List<SwitchInfoBean>> listener) {
        Call<ApiResponse<List<SwitchInfoBean>>> call = api.findAllBreakbyUser(mJSessionId);
        enqueue(call, listener);
        return call;
    }


    public Call<?> updateBreak(String breakId, int iconType, String name, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSwitchID(breakId);
        param.setIconType(String.valueOf(iconType));
        param.setName(name);
        Call<ApiResponse<Void>> call = api.updateBreak(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * @param breakId
     * @param off      是否关
     * @param listener
     * @return
     */
    public Call<?> controlBreak(String breakId, boolean off, ActionCallbackListener<ControllerId> listener) {
        Param param = new Param();
        param.setSwitchID(breakId);
        param.setCmdData(off ? "0" : "1");
        Call<ApiResponse<ControllerId>> call = api.controlBreak(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 更改被分享的用户操作权限
     *
     * @param collectorShareID
     * @param isEnable
     * @param listener
     * @return
     */
    public Call<?> updateShareCollector(String collectorShareID, int isEnable, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setCollectorShareID(collectorShareID);
        param.setEnable(isEnable);
        Call<ApiResponse<Void>> call = api.updateShareCollector(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Observable<ApiResponse<ControllerId>> rxControlBreak(String breakId, boolean off) {
        Param param = new Param();
        param.setSwitchID(breakId);
        param.setCmdData(off ? "0" : "1");
        try {
            return api.rxControlBreak(mJSessionId, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 增加或删除断路器
     *
     * @param collectorID
     * @param name
     * @param switchCode
     * @param iconType
     * @param listener
     * @return
     */
    public Call<?> addSwitch(String collectorID, String name, String switchCode, int iconType, String pid, ActionCallbackListener<Void> listener) {
        Param param = new Param();
//        param.setSwitchID(switchId);
        param.setCollectorID(collectorID);
        param.setName(name);
        param.setSwitchCode(switchCode);
//        param.setFreq(String.valueOf(freq));
        param.setIconType(String.valueOf(iconType));
//        param.setState(String.valueOf(state));
        param.setPid(pid);
        Call<ApiResponse<Void>> call = api.addSwitch(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 删除断路器
     *
     * @param switchId
     * @param listener
     * @return
     */
    public Call<?> deleteBreak(String switchId, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSwitchID(switchId);
        Call<ApiResponse<Void>> call = api.deleteBreak(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取信号值
     *
     * @param switchId
     * @param listener
     * @return
     */
    public Call<?> getSignals(String switchId, ActionCallbackListener<List<SwitchSignalItem>> listener) {
        Param param = new Param();
        param.setSwitchID(switchId);
        Call<ApiResponse<List<SwitchSignalItem>>> call = api.getSignals(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 强制刷新断路器获取最新值
     *
     * @param switchCode
     * @param listener
     * @return
     */
    public Call<?> refreshSignal(String switchCode, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSwitchCode(switchCode);
        Call<ApiResponse<Void>> call = api.refreshBreakSignals(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取信号值所类型
     *
     * @param listener
     * @return
     */
    public Call<?> getSignalsType(String type, ActionCallbackListener<List<SignalType>> listener) {
        Param param = new Param();
        param.setType(type);
        Call<ApiResponse<List<SignalType>>> call = api.getSignalsType(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取按日电量
     *
     * @param collectorCode
     * @param time          yyyy-MM-dd
     * @return
     */
    public Call<?> getTotalPowerByDay(String type, String collectorCode, String time, ActionCallbackListener<List<SwitchStatementBean>> listener) {
        Param param = new Param();
        param.setType(type);
        param.setCollectorCode(collectorCode);
        param.setTime(time);
        Call<ApiResponse<List<SwitchStatementBean>>> call = api.getTotalPowerByDay(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取按日数据
     *
     * @param switchId
     * @param signalTypeId
     * @param time         yyyy-MM-dd
     * @return
     */
    public Call<?> getAverageValueByDay(String switchId, String signalTypeId, String time, ActionCallbackListener<List<SignalValue>> listener) {
        Param param = new Param();
        param.setSwitchID(switchId);
        param.setSignalsTypeID(signalTypeId);
        param.setTime(time);
        Call<ApiResponse<List<SignalValue>>> call = api.getAverageValueByDay(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取按月电量
     *
     * @param collectorCode
     * @param time          yyyy-MM-dd
     * @return
     */
    public Call<?> getTotalPowerByMonth(String type, String collectorCode, String time, ActionCallbackListener<List<SwitchStatementBean>> listener) {
        Param param = new Param();
        param.setType(type);
        param.setCollectorCode(collectorCode);
        param.setTime(time);
        Call<ApiResponse<List<SwitchStatementBean>>> call = api.getTotalPowerByMonth(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取按月数据
     *
     * @param switchId
     * @param signalTypeId
     * @param time         yyyy-MM
     * @return
     */
    public Call<?> getAverageValueByMonth(String switchId, String signalTypeId, String time, ActionCallbackListener<List<SignalValue>> listener) {
        Param param = new Param();
        param.setSwitchID(switchId);
        param.setSignalsTypeID(signalTypeId);
        param.setTime(time);
        Call<ApiResponse<List<SignalValue>>> call = api.getAverageValueByMonth(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 服务器需要更新数据了
     *
     * @return
     */
    public Call<?> sendGetThreadValueCommand(String switchCode,
                                             String paramID,
                                             ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSwitchCode(switchCode);
        param.setParamID(paramID);
        Call<ApiResponse<Void>> call = api.sendGetThreadValueCommand(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * @param switchCode
     * @param listener
     * @return
     */
    public Call<?> sendSetThreadValueCommand(String switchCode, String values, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSwitchCode(switchCode);
        param.setValue(values);
        Call<ApiResponse<Void>> call = api.sendSetThreadValueCommand(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取过压、欠压当前值
     *
     * @param paramID
     * @param switchID
     * @param listener
     * @return
     */
    public Call<?> findSwitchParamBySwitch(String switchID, String paramID, ActionCallbackListener<List<VoltageValue>> listener) {
        Param param = new Param();
        param.setSwitchID(switchID);
        param.setParamID(paramID);
        Call<ApiResponse<List<VoltageValue>>> call = api.findSwitchParamBySwitch(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取断路器当前开关状态
     *
     * @param controllerID
     * @param listener
     * @return
     */
    public Call<?> getResultOfController(String controllerID, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setControllerID(controllerID);
        Call<ApiResponse<Void>> call = api.getResultOfController(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Observable<ApiResponse<Void>> rxGetResultOfController(String controllerID) {
        Param param = new Param();
        param.setControllerID(controllerID);
        try {
            return api.rxGetResultOfController(mJSessionId, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 新增或修改场景
     *
     * @return
     */
    public Call<?> AddOrUpdateScene(String scene, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setScenes(scene);
        Call<ApiResponse<Void>> call = api.addOrUpdateScene(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 查询所有场景
     *
     * @return
     */
    public Call<?> findSceneByUser(ActionCallbackListener<List<SceneBean>> listener) {
        Param param = new Param();
        Call<ApiResponse<List<SceneBean>>> call = api.findSceneByUser(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 删除场景
     *
     * @return
     */
    public Call<?> deleteScene(String sceneID, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSceneID(sceneID);
        Call<ApiResponse<Void>> call = api.deleteScene(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 新增或者修改场景包含的线路
     *
     * @return
     */
    public Call<?> addOrUpdateSceneSwitch(String sceneSwitchID,
                                          String sceneID,
                                          String switchID,
                                          String cmdData, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setScenes(sceneSwitchID);
        param.setSceneID(sceneID);
        param.setSwitchID(switchID);
        param.setCmdData(cmdData);
        Call<ApiResponse<Void>> call = api.addOrUpdateSceneSwitch(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 查询场景所有线路
     *
     * @return
     */
    public Call<?> findSceneSwitchByScene(String sceneID, ActionCallbackListener<List<SceneItemBean>> listener) {
        Param param = new Param();
        param.setSceneID(sceneID);
        Call<ApiResponse<List<SceneItemBean>>> call = api.findSceneSwitchByScene(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }


    /**
     * 执行场景
     *
     * @param sceneId
     * @param listener
     * @return
     */
    public Call<?> doExcuteScene(String sceneId, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSceneID(sceneId);
        Call<ApiResponse<Void>> call = api.doScene(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 删除场景内线路
     *
     * @return
     */
    public Call<?> deleteSceneSwitch(String sceneSwitchID, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setScenes(sceneSwitchID);
        Call<ApiResponse<Void>> call = api.deleteSceneSwitch(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }


    /**
     * 更新集中器的波特率,上报频率,变化幅度
     *
     * @return
     */
    public Call<?> updateCollectorParam(String collectorID,
                                        String name,
                                        String baud,
                                        String freq,
                                        String ranges,
                                        String faultFreq,
                                        String HBFreq,
                                        ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setCollectorID(collectorID);
        param.setName(name);
        param.setBaud(baud);
        param.setFreq(freq);
        param.setRanges(ranges);
        param.setFaultFreq(faultFreq);
        param.setHBFreq(HBFreq);
        Call<ApiResponse<Void>> call = api.updateCollectorParam(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取指定集中器信息
     *
     * @return
     */
    public Call<?> getCollectorByCollectorID(String collectorID,
                                             ActionCallbackListener<CollectorBean> listener) {
        Param param = new Param();
        param.setCollectorID(collectorID);
        Call<ApiResponse<CollectorBean>> call = api.getCollectorByCollectorID(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    //发送刷新命令
    public Call<?> sendSwitchRefreshCmd(String collectID, String switchIds, ActionCallbackListener<List<CmdExcuteStateBean>> listener) {
        Param param = new Param();
        param.setCollectorID(collectID);
        param.setSwitchIDs(switchIds);
        Call<ApiResponse<List<CmdExcuteStateBean>>> call = api.sendRefreshSwitchCmd(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Observable<ApiResponse<List<CmdExcuteStateBean>>> rxSendSwitchRefreshCmd(String collectID, String switchIds) {
        Param param = new Param();
        param.setCollectorID(collectID);
        param.setSwitchIDs(switchIds);
        try {
            return api.rxSendRefreshSwitchCmd(mJSessionId, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public Observable<ApiResponse<List<CmdExcuteStateBean>>> rxSendCollectRefreshCmd(String collectID) {
        Param param = new Param();
        param.setCollectorID(collectID);
        return api.rxSendRefreshCollectCmd(mJSessionId, param);
    }

    //获取开关刷新后的状态
    public Call<?> getSwitchStateAfterRefresh(String collectID, String switchIds, ActionCallbackListener<List<CmdExcuteStateBean>> listener) {
        Param param = new Param();
        param.setSwitchIDs(switchIds);
        param.setCollectorID(collectID);
        Call<ApiResponse<List<CmdExcuteStateBean>>> call = api.getRefreshSwitchState(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Observable<ApiResponse<List<CmdExcuteStateBean>>> rxGetSwitchStateAfterRefresh(String collectID, String switchIds) {
        Param param = new Param();
        param.setSwitchIDs(switchIds);
        param.setCollectorID(collectID);
        try {
            return api.rxGetRefreshSwitchState(mJSessionId, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //上传头像
    public Call<?> uploadHeadImg(MultipartBody.Part file,
                                 ActionCallbackListener<Headimg> listener) {
//        String  = AccountLoginManage.instance(mLastContext).getLastLogin().get();
        Call<ApiResponse<Headimg>> call = api.uploadHeadImg(mJSessionId,
                file);
        enqueue(call, listener);
        return call;
    }

    //改变头像，图片，昵称
    public Call<?> updateUserMsg(String username, String nickName, String headImg,
                                 String phone,
                                 ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setNickName(nickName);
        param.setUsername(username);
        param.setHeadImg(headImg);
        param.setPhone(phone);
        Call<ApiResponse<Void>> call = api.updateUserMsg(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> resetPassword(String oldPwd, String password, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setPassword(oldPwd);
        param.setNewPwd(password);
        Call<ApiResponse<Void>> call = api.updatePwd(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> addTimeOfUsePrice(String price, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setPrices(price);
        Call<ApiResponse<Void>> call = api.addTimeOfUsePrice(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getUserMsg(ActionCallbackListener<UserMsg> listener) {
        Call<ApiResponse<UserMsg>> call = api.getUserMsg(mJSessionId);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getTimeOfUsePrice(ActionCallbackListener<List<TimePrice>> listener) {
        Call<ApiResponse<List<TimePrice>>> call = api.getTimeOfUsePrice(mJSessionId);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getAllswitchMsg(String type, ActionCallbackListener<List<ElequantityBean>> listener) {
        Param param = new Param();
        param.setType(type);
        Call<ApiResponse<List<ElequantityBean>>> call = api.getAllSwitchMsg(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getSwitchByUser1(ActionCallbackListener<List<ElequantityBean>> listener) {
        Call<ApiResponse<List<ElequantityBean>>> call = api.getSwitchByUser1(mJSessionId);
        enqueue(call, listener);
        return call;
    }


    public Call<?> addOrUpdateEE(String electricalEquipmentID, String switchID, String name, Double gonglv, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setElectricalEquipmentID(electricalEquipmentID);
        param.setSwitchID(switchID);
        param.setName(name);
        param.setGonglv(gonglv);
        Call<ApiResponse<Void>> call = api.addOrUpdateEE(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getAllEE(ActionCallbackListener<List<MyEleBean>> listener) {
        Call<ApiResponse<List<MyEleBean>>> call = api.getAllEE(mJSessionId);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getEEsByCollector(String CollectorId, ActionCallbackListener<List<EleBean>> listener) {
        Param param = new Param();
        param.setCollectorID(CollectorId);
        Call<ApiResponse<List<EleBean>>> call = api.getEEsByCollector(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> deleteEE(String electricalEquipmentIDs, ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setElectricalEquipmentIDs(electricalEquipmentIDs);
        Call<ApiResponse<Void>> call = api.deleteEE(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getOperateRecord(int start, int length, ActionCallbackListener<List<RecordBean>> listener) {
        Param param = new Param();
        param.setStart(start);
        param.setLength(length);
        Call<ApiResponse<List<RecordBean>>> call = api.getOperateRecord(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getOperateRecordByCollector(int start, int length, String collectId, ActionCallbackListener<List<RecordBean>> listener) {
        Param param = new Param();
        param.setStart(start);
        param.setLength(length);
        param.setCollectorID(collectId);
        Call<ApiResponse<List<RecordBean>>> call = api.getOperateRecordByCollector(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getAlarmList(int start, int length, ActionCallbackListener<List<RecordBean>> listener) {
        Param param = new Param();
        param.setStart(start);
        param.setLength(length);
        Call<ApiResponse<List<RecordBean>>> call = api.getAlarmList(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getAlarmListByCollector(int start, int length, String collectId, ActionCallbackListener<List<RecordBean>> listener) {
        Param param = new Param();
        param.setStart(start);
        param.setLength(length);
        param.setCollectorID(collectId);
        Call<ApiResponse<List<RecordBean>>> call = api.getAlarmListByCollector(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getWarnList(int start, int length, ActionCallbackListener<List<WarnBean>> listener) {
        Param param = new Param();
        param.setStart(start);
        param.setLength(length);
        Call<ApiResponse<List<WarnBean>>> call = api.getWarnList(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getWarnListByCollector(int start, int length, String collectorId, ActionCallbackListener<List<WarnBean>> listener) {
        Param param = new Param();
        param.setStart(start);
        param.setLength(length);
        param.setCollectorID(collectorId);
        Call<ApiResponse<List<WarnBean>>> call = api.getWarnListByCollector(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getWXAccessToken(String appId, String secret, String code, String grant_type, final ActionCallbackListener<WXAccessTokenRet> listener) {
        Call<WXAccessTokenRet> call = api.getWXAccessToken(mJSessionId, appId, secret, code, grant_type);
        call.enqueue(new Callback<WXAccessTokenRet>() {
            @Override
            public void onResponse(Call<WXAccessTokenRet> call, Response<WXAccessTokenRet> response) {
                Log.d(TAG, response.raw().networkResponse().request().url().toString());
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(0, "");
                }
            }

            @Override
            public void onFailure(Call<WXAccessTokenRet> call, Throwable t) {
                listener.onFailure(0, "");
            }
        });
        return call;
    }

    public Call<?> shareCollector(boolean share, String userName, String collectId, int enable, ActionCallbackListener<Void> listener) {
        Call<ApiResponse<Void>> call = null;
        Param param = new Param();
        param.setCollectorID(collectId);
        param.setUsername(userName);
        if (share) {
            call = api.shareCollectorToUser(mJSessionId, param);
        } else {
            call = api.deleteShareColletcor(mJSessionId, param);
        }
        enqueue(call, listener);
        return call;
    }

    public Call<?> getShareList(String collectorID, ActionCallbackListener<List<UserMsg>> listener) {
        Param param = new Param();
        param.setCollectorID(collectorID);
        Call<ApiResponse<List<UserMsg>>> call = api.getShareList(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> updatePushSetting(String pushSetting, final ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setSetting(pushSetting);
        Call<ApiResponse<Void>> call = api.updatePushSetting(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    public Call<?> getPushSetting(final ActionCallbackListener<List<UserPushSetting>> listener) {
        Call<ApiResponse<List<UserPushSetting>>> call = api.getPushSetting(mJSessionId);
        enqueue(call, listener);
        return call;
    }

    public Call<?> CollectorReboot(int type, String collectId, final ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setCollectorID(collectId);
        Call<ApiResponse<Void>> call = null;
        if (type == 0) {
            call = api.rebootCollector(mJSessionId, param);
        } else {
            call = api.resetCollector(mJSessionId, param);
        }
        enqueue(call, listener);
        return call;
    }

    /**
     * 查询控制操作状态
     *
     * @param listener
     * @return
     */
    public Call<?> getControllerState(String controllerID, final ActionCallbackListener<SwitchBean> listener) {
        Param param = new Param();
        param.setControllerID(controllerID);
        Call<ApiResponse<SwitchBean>> call = api.getControllerState(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取单个线路状态
     *
     * @param switchCode
     * @param listener
     * @return
     */
    public Call<?> getSwitchState(String switchCode, final ActionCallbackListener<SwitchBean> listener) {
        Param param = new Param();
        param.setSwitchCode(switchCode);
        Call<ApiResponse<SwitchBean>> call = api.getSwitchState(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取集中器下所有线路状态
     *
     * @param collectorCode
     * @param listener
     * @return
     */
    public Call<?> getAllSwitchState(String collectorCode, final ActionCallbackListener<CollectorState> listener) {
        Param param = new Param();
//        param.setCollectorID(collectorID);
        param.setCollectorCode(collectorCode);
        Call<ApiResponse<CollectorState>> call = api.getAllSwitchState(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 验证用户名是否已注册
     *
     * @param username
     * @param listener
     * @return
     */
    public Call<?> validateUsername(String username, final ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setUsername(username);
        Call<ApiResponse<Void>> call = api.validateUsername(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 查询集中器版本升级情况
     *
     * @param collectorId
     * @param verMajor
     * @param verMinor
     * @param listener
     * @return
     */
    public Call<?> getCollectorUpgrade(String collectorId, Integer verMajor, Integer verMinor, Integer fileID, final ActionCallbackListener<CollectorUpgradeInfo> listener) {
        Param param = new Param();
        param.setCollectorID(collectorId);
        param.setVerMajor(verMajor);
        param.setVerMinor(verMinor);
        param.setFileID(fileID);
        Call<ApiResponse<CollectorUpgradeInfo>> call = api.getCollectorUpgrade(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 设置集中器版本升级情况
     *
     * @param collectorId
     * @param verMajor
     * @param verMinor
     * @param ok
     * @param execTime
     * @param listener
     * @return
     */
    public Call<?> setCollectorUpgrade(String collectorId, Integer verMajor, Integer verMinor, Integer fileID, Integer ok, String execTime, final ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setCollectorID(collectorId);
        param.setVerMajor(verMajor);
        param.setVerMinor(verMinor);
        param.setFileID(fileID);
        param.setOk(ok);
        param.setIndex(execTime);
        Call<ApiResponse<Void>> call = api.setCollectorUpgrade(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 主要用于月查询，谷值跟峰值
     *
     * @param switchId
     * @param time         对应的time格式为yyyy-MM/yyyy
     * @param signalTypeId 查询有功电量时 signalsTypeID=ygdl
     * @param type         type=(month/year)
     * @param listener
     * @return
     */
    public Call<?> getSignalByTime(String switchId, String time, String signalTypeId, String type,
                                   ActionCallbackListener<List<SignalPeakValleyValue>> listener) {
        Param param = new Param();
        param.setSwitchID(switchId);
        param.setTime(time);
        param.setSignalsTypeID(signalTypeId);
        param.setType(type);
        Call<ApiResponse<List<SignalPeakValleyValue>>> call = api.getSignalByTime(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 查询周报月报 列表
     *
     * @param index
     * @param listener
     * @return
     */
    public Call<?> getReportList(Integer index,
                                 ActionCallbackListener<List<ReportBean>> listener) {
        Param param = new Param();
        param.setIndex(String.valueOf(index));
        Call<ApiResponse<List<ReportBean>>> call = api.getReportList(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 查询周报月报
     *
     * @param dateListID
     * @param collectorID
     * @param listener
     * @return
     */
    public Call<?> getReport(Integer dateListID, String collectorID,
                             ActionCallbackListener<ReportDetailBean> listener) {
        Param param = new Param();
        param.setDateListID(dateListID);
        param.setCollectorID(collectorID);
        Call<ApiResponse<ReportDetailBean>> call = api.getReport(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取用户提问列表
     *
     * @return
     */
    public Call<?> findChatList(int start, int length,
                                ActionCallbackListener<List<ChartListItemBean>> listener) {
        Param param = new Param();
        param.setStart(start);
        param.setLength(length);
        Call<ApiResponse<List<ChartListItemBean>>> call = api.findChatList(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 获取用户提问内容 包括客服回复
     *
     * @return
     */
    public Call<?> getUserChatContent(int start, int length, int id,
                                      ActionCallbackListener<List<ChartItemBean>> listener) {
        Param param = new Param();
        param.setUserChatID(id);
        param.setStart(start);
        param.setLength(length);
        Call<ApiResponse<List<ChartItemBean>>> call = api.getUserChatContent(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 添加问题主题
     *
     * @return
     */
    public Call<?> addToUserChatList(String title, String content,
                                     ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setTitle(title);
        param.setContent(content);
        Call<ApiResponse<Void>> call = api.addToUserChatList(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 添加问题主题内容
     *
     * @return
     */
    public Call<?> addToUserChatContent(Integer userChatID, String content,
                                        ActionCallbackListener<Void> listener) {
        Param param = new Param();
        param.setUserChatID(userChatID);
        param.setContent(content);
        Call<ApiResponse<Void>> call = api.addToUserChatContent(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 判断是否关注了公众号
     *
     * @return
     */
    public Call<?> validateWechatPublic(ActionCallbackListener<WxSubscribed> listener) {
        Param param = new Param();
        Call<ApiResponse<WxSubscribed>> call = api.validateWechatPublic(mJSessionId, param);
        enqueue(call, listener);
        return call;
    }

    /**
     * 上传日志
     *
     * @param log
     * @param listener
     * @return
     */
    public Call<?> uploadLogFile(MultipartBody.Part log, ActionCallbackListener<Void> listener) {
        Call<ApiResponse<Void>> call = api.AppClientAction_uploadLogFile(mJSessionId, log);
        enqueue(call, listener);
        return call;
    }
//    private boolean oFlag = true;

    private <T> void enqueue(Call<ApiResponse<T>> call, final ActionCallbackListener<T> listener) {
        call.enqueue(new Callback<ApiResponse<T>>() {

            public void onResponse(@NonNull Call<ApiResponse<T>> arg0, @NonNull Response<ApiResponse<T>> resp) {
                if (resp.raw().networkResponse() != null) {
                    Log.d(TAG, (resp.raw().networkResponse()).request().url().toString());
                } else {
                    Log.e(TAG, "resp.raw().networkResponse() is null");
                }
                if (resp.isSuccessful()) {
                    List<String> listSession = resp.headers().values("Set-Cookie");
                    for (String session : listSession) {
                        if (session.contains("JSESSIONID")) {
                            mJSessionId = session;
                            break;
                        }
                    }
                    if (resp.body() != null) {
                        if (resp.body().isSuccess()) {
                            listener.onSuccess(resp.body().getData());
                        } else if (resp.body().isAccessDenied()) {
                            Log.e(TAG, "resp.body() isAccessDenied");
//                            CustomToast.showCustomErrorToast(context, context.getString(R.string.vs94));
//                            if (oFlag) {
                            Intent intent = new Intent("com.example.chencong.broadcastbestpractive.FORCE_OFFLINE");
//                            Android8.0
//                            即是以下两种情况静态receiver不会接收到广播：
//                            发送的intent设置了FLAG --FLAG_RECEIVER_EXCLUDE_BACKGROUND；
//                            以下情况的均满足时：
//                              ①intent没有指定接收组件，也就是没有setComponent
//                              ②intent没有执行接收的package，也就是没有setPackage
//                              ③发送的intent没有设置FLAG-FLAG_RECEIVER_INCLUDE_BACKGROUND
//                              ④给定的权限并不都是签名权限。
//                            根据这两种情况，即是说静态receiver接收不了隐式广播。本来打算采用最简单的方法添加Flag来解决的。
//                            但是奇怪的是，Android Studio里没有FLAG_RECEIVER_INCLUDE_BACKGROUND！！！！
//                            然后，只好在发送intent的时候setPackage。
                            intent.setPackage(context.getPackageName());
                            //发送广播--权限错误 强制重新登录
                            context.sendBroadcast(intent);
//                            }
                        } else {
                            reportError(listener, resp.body().getCode(), resp.body().getMessage());
                        }
                    } else {
                        Log.e(TAG, "resp.body() is null");
                    }
                } else {
                    try {
                        if (resp.errorBody() != null) {
                            Log.d(TAG, "report Error:" + (resp.errorBody().string()));
                        } else {
                            Log.e(TAG, "resp.errorBody() is null");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    reportError(listener, ErrorEvent.CODE_UNKNOW_ERROR, "CODE_UNKNOW_ERROR");
                }
            }

            public void onFailure(@NonNull Call<ApiResponse<T>> arg0, @NonNull Throwable arg1) {
                Log.d(TAG, "onFailure");
                arg1.printStackTrace();
//                Context context = MainApplication.instance().getApplicationContext();
                if (arg1 instanceof SocketTimeoutException) {
                    CustomToast.showCustomErrorToast(context, context.getString(R.string.network_time_out));
                    listener.onFailure(ErrorEvent.CODE_CONNECT_TIMEOUT, "CODE_CONNECT_TIMEOUT");
                } else if (arg1 instanceof ConnectException) {
                    CustomToast.showCustomErrorToast(context, context.getString(R.string.network_cannot_connect));
                    listener.onFailure(ErrorEvent.CODE_NET_UNCONNECTED, "CODE_NET_UNCONNECTED");
                } else {
                    listener.onFailure(ErrorEvent.CODE_UNKNOW_ERROR, "CODE_UNKNOW_ERROR");
                }
            }
        });
    }

    private void reportError(ActionCallbackListener<?> listener, int errorCode, String errorMsg) {
        listener.onFailure(errorCode, errorMsg);
    }

}
