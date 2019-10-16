package com.rejuvee.smartelectric.family.api;

import com.base.frame.net.ApiResponse;
import com.rejuvee.smartelectric.family.api.converter.Param;
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
import com.rejuvee.smartelectric.family.model.bean.TimePrice;
import com.rejuvee.smartelectric.family.model.bean.TimeTaskBean;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.model.bean.UserPushSetting;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;
import com.rejuvee.smartelectric.family.model.bean.WXAccessTokenRet;
import com.rejuvee.smartelectric.family.model.bean.WarnBean;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by SH on 2017/12/19.
 * <p>
 * Todo： 现有接口大多针对于集中器来进行操作，后期需要调整至针对用户。
 */

public interface Api {

    /**
     * 登录
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_login.do")
    Call<ApiResponse<AccountInfo>> login(@Header("Cookie") String session,
                                         @Body Param param);

    @POST("PowerManager/AppClientAction_wechatLogin.do")
    Call<ApiResponse<AccountInfo>> loginWX(@Header("Cookie") String session,
                                           @Body Param param);

    @POST("PowerManager/AppClientAction_qqLogin.do")
    Call<ApiResponse<AccountInfo>> loginQQ(@Header("Cookie") String session,
                                           @Body Param param);

    @POST("PowerManager/AppClientAction_bindingWechat.do")
    Call<ApiResponse<Void>> bindWX(@Header("Cookie") String session,
                                   @Body Param param);

    @POST("PowerManager/AppClientAction_unbindingWechat.do")
    Call<ApiResponse<Void>> unBindWX(@Header("Cookie") String session
    );

    @POST("PowerManager/AppClientAction_unbindingQQ.do")
    Call<ApiResponse<Void>> unBindQQ(@Header("Cookie") String session
    );

    @POST("PowerManager/AppClientAction_bindingQQ.do")
    Call<ApiResponse<Void>> bindQQ(@Header("Cookie") String session,
                                   @Body Param param);

    @POST("PowerManager/AppClientAction_regist.do")
    Call<ApiResponse<Void>> register(@Header("Cookie") String session,
                                     @Body Param param);

    //修改密码

    @POST("PowerManager/AppClientAction_updatePwd.do")
    Call<ApiResponse<Void>> updatePwd(@Header("Cookie") String session,
                                      @Body Param param);

    //忘记密码 重设密码

    @POST("PowerManager/AppClientAction_resetPwd.do")
    Call<ApiResponse<Void>> resetPwd(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_getCode.do")
    Call<ApiResponse<Void>> getCode(@Header("Cookie") String session,
                                    @Body Param param);


    @POST("PowerManager/AppClientAction_validatePhone.do")
    Call<ApiResponse<Void>> validatePhone(@Header("Cookie") String session,
                                          @Body Param param);


    @POST("PowerManager/AppClientAction_registByPhone.do")
    Call<ApiResponse<Void>> registByPhone(@Header("Cookie") String session,
                                          @Body Param param);


    @POST("PowerManager/AppClientAction_addOrUpdateScene.do")
    Call<ApiResponse<Void>> addOrUpdateScene(@Header("Cookie") String session,
                                             @Body Param param
    );


    @POST("PowerManager/AppClientAction_addOrUpdateSceneSwitch.do")
    Call<ApiResponse<Void>> addOrUpdateSceneSwitch(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_updateCollectorParam.do")
    Call<ApiResponse<Void>> updateCollectorParam(
            @Header("Cookie") String session,
            @Body Param param
    );


    @POST("PowerManager/AppClientAction_getCollectorByCollectorID.do")
    Call<ApiResponse<CollectorBean>> getCollectorByCollectorID(
            @Header("Cookie") String session,
            @Body Param param
    );

    @POST("PowerManager/AppClientAction_rebootCollector.do")
    Call<ApiResponse<Void>> rebootCollector(
            @Header("Cookie") String session,
            @Body Param param
    );


    @POST("PowerManager/AppClientAction_resetCollector.do")
    Call<ApiResponse<Void>> resetCollector(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 绑定设备
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_bindingWithCollector.do")
    Call<ApiResponse<Void>> bindDevice(
            @Header("Cookie") String session,
            @Body Param param);


    /**
     * 解除绑定设备
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_unbindingWithCollector.do")
    Call<ApiResponse<Void>> unBindDevice(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取用户所有定时任务
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_findTimeControllerByUser.do")
    Call<ApiResponse<List<TimeTaskBean>>> getDeviceAllTimeTask(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取Switch所有定时任务
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_findTimeControllerBySwitch.do")
    Call<ApiResponse<List<TimeTaskBean.TimeTask>>> findTimeControllerBySwitch(
            @Header("Cookie") String session,
            @Body Param param);


    /**
     * 新增或修改一个定时任务
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_addOrUpdateTimeController.do")
    Call<ApiResponse<Void>> updateOrInsertTimeTask(
            @Header("Cookie") String session,
            @Body Param param);


    /**
     * 删除一个定时任务
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_deleteTimeController.do")
    Call<ApiResponse<Void>> deleteTimeTask(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取控制器
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_getCollector.do")
    Call<ApiResponse<List<CollectorBean>>> getCollector(
            @Header("Cookie") String session
    );


//    @POST("PowerManager/AppClientAction_getCollector.do")
//    Observable<ApiResponse<List<CollectorBean>>> rxGetCollector(
//            @Header("Cookie") String session
//    );

    /**
     * 获取断路器
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_getSwitch.do")
    Observable<ApiResponse<SwitchBean>> getBreakers(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取集中器下断路器信息
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_getSwitchByCollector.do")
    Call<ApiResponse<List<SwitchBean>>> getSwitchByCollector(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_getSwitch.do")
    Observable<SwitchBean> rxGetSwitch(
            @Header("Cookie") String session,
            @Body Param param);

    /* switchID 	string 	是 	新增时不传,修改时传
     collectorID 	string 	是 	集中器id
     name 	string 	是 	名称
     switchCode 	string 	是 	断路器唯一编码
     freq 	string 	是 	频率
     iconType 	string 	是 	图标
     state 	string 	是 	开关状态*/

    //    @POST("PowerManager/AppClientAction_addOrUpdateSwitch.do")
    @POST("PowerManager/AppClientAction_addSwitch.do")
    Call<ApiResponse<Void>> addSwitch(
            @Header("Cookie") String session,
            @Body Param param
    );


    @POST("PowerManager/AppClientAction_deleteSwitch.do")
    Call<ApiResponse<Void>> deleteBreak(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 修改断路器
     *
     * @return
     */

    @POST("PowerManager/AppClientAction_updateSwitch.do")
    Call<ApiResponse<Void>> updateBreak(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 控制断路器开关
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_operateSwitch.do")
    Call<ApiResponse<ControllerId>> controlBreak(
            @Header("Cookie") String session,
            @Body Param param
    );


    @POST("PowerManager/AppClientAction_operateSwitch.do")
    Observable<ApiResponse<ControllerId>> rxControlBreak(
            @Header("Cookie") String session,
            @Body Param param
    );


    /**
     * 获取信号值
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_getSignals.do")
    Call<ApiResponse<List<SwitchSignalItem>>> getSignals(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_refreshSwitch.do")
    Call<ApiResponse<Void>> refreshBreakSignals(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取信号值所类型
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_findSignalsType.do")
    Call<ApiResponse<List<SignalType>>> getSignalsType(
            @Header("Cookie") String session,
            @Body Param param
    );


    /**
     * 获取按日数据
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_getSignalByDay.do")
    Call<ApiResponse<List<SignalValue>>> getAverageValueByDay(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取按日电量
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_getTotalPowerByDay_v1.do")
    Call<ApiResponse<List<SwitchStatementBean>>> getTotalPowerByDay(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 获取按月电量
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_getTotalPowerByMonth_v1.do")
    Call<ApiResponse<List<SwitchStatementBean>>> getTotalPowerByMonth(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 获取按月数据
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_getSignalByMonth.do")
    Call<ApiResponse<List<SignalValue>>> getAverageValueByMonth(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取用户所有场景
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_findSceneByUser.do")
    Call<ApiResponse<List<SceneBean>>> findSceneByUser(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 删除场景
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_deleteScene.do")
    Call<ApiResponse<Void>> deleteScene(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 查询场景所有线路
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_findSceneSwitchByScene.do")
    Call<ApiResponse<List<SceneItemBean>>> findSceneSwitchByScene(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 删除场景内线路
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_deleteSceneSwitch.do")
    Call<ApiResponse<Void>> deleteSceneSwitch(
            @Header("Cookie") String session,
            @Body Param param
    );


    @POST("PowerManager/AppClientAction_doScene.do")
    Call<ApiResponse<Void>> doScene(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 获取用户所有断路器信息
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_getSwitchByUser.do")
    Call<ApiResponse<List<SwitchInfoBean>>> findAllBreakbyUser(
            @Header("Cookie") String session
    );


    /**
     * 服务器需要更新数据了
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_sendGetThreadValueCommand1.do")
    Call<ApiResponse<Void>> sendGetThreadValueCommand(
            @Header("Cookie") String session,
            @Body Param param);


    /**
     * 获取服务器新数据
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_sendSetThreadValueCommand1.do")
    Call<ApiResponse<Void>> sendSetThreadValueCommand(
            @Header("Cookie") String session,
            @Body Param param);


    /**
     * 获取过压、欠压当前值
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_findSwitchParamBySwitch.do")
    Call<ApiResponse<List<VoltageValue>>> findSwitchParamBySwitch(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取断路器获取断路器当前开关状态
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_getResultOfController.do")
    Call<ApiResponse<Void>> getResultOfController(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_getResultOfController.do")
    Observable<ApiResponse<Void>> rxGetResultOfController(
            @Header("Cookie") String session,
            @Body Param param);


    //发送断路器开关状态刷新命令
    @POST("PowerManager/AppClientAction_sendRefreshSwitch.do")
    Call<ApiResponse<List<CmdExcuteStateBean>>> sendRefreshSwitchCmd(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_sendRefreshSwitch.do")
    Observable<ApiResponse<List<CmdExcuteStateBean>>> rxSendRefreshSwitchCmd(
            @Header("Cookie") String session,
            @Body Param param);

    //发送集中器状态刷新命令
    @POST("PowerManager/AppClientAction_sendRefreshCollector.do")
    Observable<ApiResponse<List<CmdExcuteStateBean>>> rxSendRefreshCollectCmd(
            @Header("Cookie") String session,
            @Body Param param);

    //查询断路器刷新后的状态
    @POST("PowerManager/AppClientAction_refreshSwitchState.do")
    Call<ApiResponse<List<CmdExcuteStateBean>>> getRefreshSwitchState(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_refreshSwitchState.do")
    Observable<ApiResponse<List<CmdExcuteStateBean>>> rxGetRefreshSwitchState(
            @Header("Cookie") String session,
            @Body Param param);

    //设置电价
    @POST("PowerManager/AppClientAction_addOrUpdatePowerPrice.do")
    Call<ApiResponse<Void>> addTimeOfUsePrice(
            @Header("Cookie") String session,
            @Body Param param);

    //获取电价
    @POST("PowerManager/AppClientAction_getPowerPrice.do")
    Call<ApiResponse<List<TimePrice>>> getTimeOfUsePrice(
            @Header("Cookie") String session
    );


    //设置头像，昵称，号码
    @Multipart
    @POST("PowerManager/AppClientAction_uploadHeadImg.do")
    public Call<ApiResponse<Headimg>> uploadHeadImg(
            @Header("Cookie") String session,
            @Part MultipartBody.Part photo
    );

    //更新用户信息
    @POST("PowerManager/AppClientAction_updateUserMsg.do")
    Call<ApiResponse<Void>> updateUserMsg(
            @Header("Cookie") String session,
            @Body Param param
    );

    @POST("PowerManager/AppClientAction_getUserMsg.do")
    Call<ApiResponse<UserMsg>> getUserMsg(
            @Header("Cookie") String session
    );

    //获取线路的电量   获取所有线路信息
    @POST("PowerManager/AppClientAction_getAllSwitchMsg.do")
    Call<ApiResponse<List<ElequantityBean>>> getAllSwitchMsg(
            @Header("Cookie") String session,
            @Body Param param
    );


    @POST("PowerManager/AppClientAction_getSwitchByUser1.do")
    Call<ApiResponse<List<ElequantityBean>>> getSwitchByUser1(
            @Header("Cookie") String session
    );


    //增加 删除 修改 電器信息
    @POST("PowerManager/AppClientAction_addOrUpdateEE.do")
    Call<ApiResponse<Void>> addOrUpdateEE(
            @Header("Cookie") String session,
            @Body Param param);


    //获取所有电器
    @POST("PowerManager/AppClientAction_getAllEE.do")
    Call<ApiResponse<List<MyEleBean>>> getAllEE(
            @Header("Cookie") String session
    );

    //获取集中器电器
    @POST("PowerManager/AppClientAction_getEEsByCollector.do")
    Call<ApiResponse<List<EleBean>>> getEEsByCollector(
            @Header("Cookie") String session,
            @Body Param param
    );


    //删除电器
    @POST("PowerManager/AppClientAction_deleteEE.do")
    Call<ApiResponse<Void>> deleteEE(
            @Header("Cookie") String session,
            @Body Param param);

    // 更改分享用户
    @POST("PowerManager/AppClientAction_updateShareCollector.do")
    Call<ApiResponse<Void>> updateShareCollector(
            @Header("Cookie") String session,
            @Body Param param
    );

    //获取所有操作记录
    @POST("PowerManager/AppClientAction_getOperateRecord.do")
    Call<ApiResponse<List<RecordBean>>> getOperateRecord(
            @Header("Cookie") String session,
            @Body Param param);

    //获取集中器操作记录
    @POST("PowerManager/AppClientAction_getOperateRecordByCollector.do")
    Call<ApiResponse<List<RecordBean>>> getOperateRecordByCollector(
            @Header("Cookie") String session,
            @Body Param param);


    //获取所有故障报警
    @POST("PowerManager/AppClientAction_getAlarmList.do")
    Call<ApiResponse<List<RecordBean>>> getAlarmList(
            @Header("Cookie") String session,
            @Body Param param
    );

    //获取集中器故障报警
    @POST("PowerManager/AppClientAction_getAlarmListByCollector.do")
    Call<ApiResponse<List<RecordBean>>> getAlarmListByCollector(
            @Header("Cookie") String session,
            @Body Param param
    );

    //获取所有预警
    @POST("PowerManager/AppClientAction_getWarnList.do")
    Call<ApiResponse<List<WarnBean>>> getWarnList(
            @Header("Cookie") String session,
            @Body Param param
    );

    //获取集中器预警
    @POST("PowerManager/AppClientAction_getWarnListByCollector.do")
    Call<ApiResponse<List<WarnBean>>> getWarnListByCollector(
            @Header("Cookie") String session,
            @Body Param param
    );

    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Call<WXAccessTokenRet> getWXAccessToken(
            @Header("Cookie") String session,
            @Query("appid") String appid,
            @Query("secret") String secret,
            @Query("code") String code,
            @Query("grant_type") String grant_type);


    @POST("PowerManager/AppClientAction_shareCollectorToUser.do")
    Call<ApiResponse<Void>> shareCollectorToUser(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_deleteShareCollector.do")
    Call<ApiResponse<Void>> deleteShareColletcor(
            @Header("Cookie") String session,
            @Body Param param);


    @POST("PowerManager/AppClientAction_getShareList.do")
    Call<ApiResponse<List<UserMsg>>> getShareList(
            @Header("Cookie") String session,
            @Body Param param);

    @POST("PowerManager/AppClientAction_updatePushSetting.do")
    Call<ApiResponse<Void>> updatePushSetting(
            @Header("Cookie") String session,
            @Body Param param
    );

    @POST("PowerManager/AppClientAction_getUserPushSetting.do")
    Call<ApiResponse<List<UserPushSetting>>> getPushSetting(
            @Header("Cookie") String session
    );

    /**
     * 查询控制操作状态
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_getControllerState.do")
    Call<ApiResponse<SwitchBean>> getControllerState(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 获取单个线路状态
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_getSwitchState.do")
    Call<ApiResponse<SwitchBean>> getSwitchState(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 获取集中器下所有线路状态
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_getAllSwitchState.do")
    Call<ApiResponse<CollectorState>> getAllSwitchState(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 验证用户名是否已注册
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_validateUsername.do")
    Call<ApiResponse<Void>> validateUsername(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 查询集中器版本升级情况
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_getCollectorUpgrade.do")
    Call<ApiResponse<CollectorUpgradeInfo>> getCollectorUpgrade(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 设置集中器版本升级情况
     * 确认结果 0=不同意 1=同意
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_setCollectorUpgrade.do")
    Call<ApiResponse<Void>> setCollectorUpgrade(
            @Header("Cookie") String session,
            @Body Param param
    );

    @POST("PowerManager/AppClientAction_getSignalByTime.do")
    Call<ApiResponse<List<SignalPeakValleyValue>>> getSignalByTime(
            @Header("Cookie") String session,
            @Body Param param
    );

    @POST("PowerManager/AppClientAction_getDateList.do")
    Call<ApiResponse<List<ReportBean>>> getReportList(
            @Header("Cookie") String session,
            @Body Param param
    );

    @POST("PowerManager/AppClientAction_getStatistics.do")
    Call<ApiResponse<ReportDetailBean>> getReport(
            @Header("Cookie") String session,
            @Body Param param
    );

    /**
     * 获取用户提问列表
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_getUserChatList.do")
    Call<ApiResponse<List<ChartListItemBean>>> findChatList(@Header("Cookie") String session,
                                                            @Body Param param);

    /**
     * 添加问题主题
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_addToUserChatList.do")
    Call<ApiResponse<Void>> addToUserChatList(@Header("Cookie") String session,
                                              @Body Param param);

    /**
     * 添加回复内容到主题
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_addToUserChatContent.do")
    Call<ApiResponse<Void>> addToUserChatContent(@Header("Cookie") String session,
                                                 @Body Param param);

    /**
     * 获取主题下的回复内容
     *
     * @param session
     * @param param
     * @return
     */
    @POST("PowerManager/AppClientAction_getUserChatContent.do")
    Call<ApiResponse<List<ChartItemBean>>> getUserChatContent(@Header("Cookie") String session,
                                                              @Body Param param);

    /**
     * 上传日志
     *
     * @param session
     * @param log
     * @return
     */
    @Multipart
    @POST("PowerManager/AppClientAction_uploadLogFile.do")
    Call<ApiResponse<Void>> AppClientAction_uploadLogFile(
            @Header("Cookie") String session,
            @Part MultipartBody.Part log
    );
}

