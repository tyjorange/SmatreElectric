package com.rejuvee.smartelectric.family.api;

import android.content.Context;
import android.util.Log;

import com.base.frame.net.ApiResponse;
import com.rejuvee.smartelectric.family.api.converter.Param;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by liuchengran on 2018/6/26.
 * 断路器 最大电流获取及设置
 * 过压 欠压 获取及设置
 */
@Deprecated
public class BreakEVSetHttpCall {
    private ApiBreakEVSet api;
    private int RECONNECT_COUNT = 4;
    private int reconnetedCount = 0;

    private String TAG = "BreakEVSetHttpCall";
    private LoadingDlg waitDialog;

    public BreakEVSetHttpCall(Context context) {
        Retrofit retrofit = Core.instance(null).getRetrofit();
        api = retrofit.create(ApiBreakEVSet.class);
        waitDialog = new LoadingDlg(context, -1);
    }

    //刷新服务器数据
    private void refreshServerData(IRefreshCallback callback) {

    }

    private class refreshError extends RuntimeException {
        public refreshError() {
            super("refresh error");
        }
    }

    @Deprecated
    public void getOvervoltage(String switchCode,
                               String swichId,
                               IBreakEVGetCallback callback) {
//        getValueLimit(switchCode, swichId, "2", 0x00000005 + "", callback);
    }

    @Deprecated
    public void getUnderVoltage(String switchCode,
                                String swichId,
                                IBreakEVGetCallback callback) {
//        getValueLimit(switchCode, swichId, "3", 0x0000000D + "", callback);
    }

    @Deprecated
    public void getElectricLimit(String switchCode,
                                 String swichId,
                                 IBreakEVGetCallback callback) {
//        getValueLimit(switchCode, swichId, "1", 0x00000011 + "", callback);
    }

    public void getAllLimit(String switchCode,
                            int swichId,
                            IBreakEVGetCallback callback) {
        getValueLimit(switchCode, swichId, callback);
    }

    public void setLimitValue(final String switchCode,
                              final String values,
                              final IBreakSetCallback callback) {
        waitDialog.show();
        Param param = new Param();
        param.setSwitchCode(switchCode);
        param.setValue(values);
        api.sendSetThreadValueCommand(Core.mJSessionId, param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse<Void> voidApiResponse) {
                        if (voidApiResponse.isSuccess()) {
//                            getValueAfterSet(type, switchCode, switchId, value, callback);
                            callback.setCallback(true);
                        } else {
                            callback.setCallback(false);
                        }
                        waitDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.setCallback(false);
                        waitDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private final int reGetDataCount = 8;
    private int currentGetDataCount = 0;

//    private void getValueAfterSet(final String type, final String breakCode, final String breakId, final String value, final IBreakSetCallback callback) {
//        if (!waitDialog.isShowing()) {
//            waitDialog.show();
//        }
//        IBreakEVGetCallback callback1 = new IBreakEVGetCallback() {
//            @Override
//            public void getCallback(VoltageValue newValue) {
//                if (newValue == null) {
//                    callback.setCallback(false);
//                    waitDialog.dismiss();
//                } else {
//                    try {
//                        float fNewValue = Float.valueOf(newValue.getParamValue());
//                        float fSetValue = Float.valueOf(value);
//                        Log.d(TAG, "setValue=" + fSetValue + " serverValue=" + fNewValue);
//                        if (fNewValue == fSetValue) {
//                            callback.setCallback(true);
//                            currentGetDataCount = 0;
//                            waitDialog.dismiss();
//                        } else {
//                            if (currentGetDataCount < reGetDataCount) {
//                                Thread.sleep(500);
//                                currentGetDataCount++;
//                                getValueAfterSet(type, breakCode, breakId, value, callback);
//                            } else {
//                                callback.setCallback(false);
//                                currentGetDataCount = 0;
//                                waitDialog.dismiss();
//                            }
//                        }
//                    } catch (Exception e) {
//                        waitDialog.dismiss();
//                        callback.setCallback(false);
//                        currentGetDataCount = 0;
//                    }
//
//                }
//
//            }
//        };
//        if (type.equals("1")) {
//            getElectricLimit(breakCode, breakId, callback1);
//        } else if (type.equals("2")) {
//            getOvervoltage(breakCode, breakId, callback1);
//        } else if (type.equals("3")) {
//            getUnderVoltage(breakCode, breakId, callback1);
//        }
//
//    }


    private void resetConnectedCount() {
        reconnetedCount = 0;
    }

    /**
     * 过流：0x00000011 (17)  过压：0x00000005 (5) 欠压：0x0000000D (13)
     * 电量下限 0x00000018 (24) 电量上限 0x00000019 (25)
     *
     * @param switchCode
     * @param swichId
     * @param callback
     */
    private void getValueLimit(final String switchCode,
                               final int swichId,
                               final IBreakEVGetCallback callback) {
        Param param = new Param();
        param.setSwitchCode(switchCode);
//        param.setType(type);
//        param.setIndex("0");
        param.setParamID("00000011,00000005,0000000D,00000018,00000019,");
        api.sendGetThreadValueCommand(Core.mJSessionId, param)
                .flatMap((Function<ApiResponse<Void>, ObservableSource<ApiResponse<List<VoltageValue>>>>) voidApiResponse -> {
                    if (voidApiResponse.isSuccess()) {
                        Log.d(TAG, "发送刷新命令成功, threadId = " + Thread.currentThread().getId());
                        Param param1 = new Param();
                        param1.setSwitchID(swichId);
                        param1.setParamID("00000011,00000005,0000000D,00000018,00000019,");
                        return api.findSwitchParamBySwitch(Core.mJSessionId, param1);
                    } else {
                        Log.e(TAG, "发送刷新命令失败");
                        return Observable.create(e -> {
                            e.tryOnError(new refreshError());
                            e.onComplete();
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ApiResponse<List<VoltageValue>>>() {
            private Disposable mDisposeable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposeable = d;
            }

            @Override
            public void onNext(ApiResponse<List<VoltageValue>> voidApiResponse) {
                //waitDialog.dismiss();
                if (voidApiResponse.isSuccess()) {
                    Log.d(TAG, "获取数据成功, threadId = " + Thread.currentThread().getId());
                    callback.getCallback(voidApiResponse.getData());
                } else {
                    callback.getCallback(null);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof refreshError) {
                    if (reconnetedCount <= RECONNECT_COUNT) {
                        Log.e(TAG, "发送刷新命令失败。准备重发，重发次数" + reconnetedCount);
                        getValueLimit(switchCode, swichId, callback);
                        reconnetedCount++;
                    } else {
                        Log.e(TAG, "发送刷新命令失败。退出重发，重发次数" + reconnetedCount);
                        reconnetedCount = 0;
                        mDisposeable.dispose();
                        callback.getCallback(null);
                        waitDialog.dismiss();
                    }
                } else {
                    callback.getCallback(null);
                    waitDialog.dismiss();
                }
            }

            @Override
            public void onComplete() {

            }
        });

    }

    public interface IBreakSetCallback {
        void setCallback(boolean success);
    }

    public interface IBreakEVGetCallback {
        void getCallback(List<VoltageValue> value);
    }

    private interface IRefreshCallback {
        void refreshCallback(boolean success);
    }


}
