package com.rejuvee.smartelectric.family.api;

import com.base.frame.net.ApiResponse;
import com.rejuvee.smartelectric.family.api.converter.Param;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by liuchengran on 2018/6/26.
 */

public interface ApiBreakEVSet {

    /**
     * 设置数据
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_sendSetThreadValueCommand1.do")
    Observable<ApiResponse<Void>> sendSetThreadValueCommand(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 在获取数据之前，发送刷新命令
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_sendGetThreadValueCommand1.do")
    Observable<ApiResponse<Void>> sendGetThreadValueCommand(
            @Header("Cookie") String session,
            @Body Param param);

    /**
     * 获取过压、欠压 及 过流值
     *
     * @return
     */
    @POST("PowerManager/AppClientAction_findSwitchParamBySwitch.do")
    Observable<ApiResponse<List<VoltageValue>>> findSwitchParamBySwitch(
            @Header("Cookie") String session,
            @Body Param param);
}
