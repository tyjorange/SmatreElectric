package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.core.AbstractBaseFragment;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.model.bean.SignalPeakValleyValue;
import com.rejuvee.smartelectric.family.model.bean.SignalValue;
import com.rejuvee.smartelectric.family.widget.MonitorDataChartView;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.rejuvee.smartelectric.family.api.Core.instance;

public class CurveFragment extends AbstractBaseFragment {

    private TextView tvUnit;

    private String signalTypeId;
    private String switchId;
    private String time;
    private boolean isDay;
    private String unit;
    private MonitorDataChartView monitorDataChartView;
    private LoadingDlg loadingDlg;
    private List<SignalValue> signalValueList;
    private List<SignalValue> signalValueListExtral = new ArrayList<>();//用于月查询时，谷值跟峰值
    private OnShowingListener listener;
//    private boolean isShowing;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    private int position;

    private String tag;
    private Call<?> currentCall = null;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_curve;
    }

    @Override
    protected void initView(View v) {
        Log.e("VpAdapter", "initView: " + position);
        tvUnit = (TextView) v.findViewById(R.id.tv_unit);
        monitorDataChartView = (MonitorDataChartView) v.findViewById(R.id.mdcv);
        loadingDlg = new LoadingDlg(getContext(), -1);
    }

    @Override
    protected void initData() {
        Log.e("VpAdapter", "initData: " + position);
        signalValueList = new ArrayList<>();
        change(getArguments());
//        isShowing = true;
    }


    public void change(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        Log.e("VpAdapter", "changeData: " + position);
        tag = bundle.getCharSequence("tag").toString();
        String signalTypeIdNew = bundle.getCharSequence("signalTypeId").toString();
        String switchIdNew = bundle.getCharSequence("switchId").toString();
        String timeNew = bundle.getCharSequence("time").toString();
        boolean isDayNew = bundle.getBoolean("isDay");
        String unitNew = bundle.getCharSequence("unit").toString();
        if (signalValueList.size() == 0
                || (!isDayNew && signalValueListExtral.size() == 0)
                || !signalTypeIdNew.equals(signalTypeId)
                || !switchIdNew.equals(switchId)
                || !timeNew.equals(time)
                || !(isDayNew == isDay)
                || !unitNew.equals(unit)) {
            signalTypeId = signalTypeIdNew;
            switchId = switchIdNew;
            time = timeNew;
            isDay = isDayNew;
            unit = unitNew;
            loadingDlg.show();
            tvUnit.setText(unit);
            if (isDay) {
                getAverageValueByDay();
            } else {
                getValueByMonth();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onShowing(this);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser && isShowing) {
//            Log.e("VpAdapter", "setUserVisibleHint: " + position);
//            listener.onShowing(this);
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }


    private void notifyDataListChanged() {
        loadingDlg.dismiss();
        List<MonitorDataChartView.MonitorData> data = new ArrayList<>();
        List<MonitorDataChartView.MonitorDataItem> monitorDataItemList = new ArrayList<>();
        MonitorDataChartView.MonitorData monitorData = new MonitorDataChartView.MonitorData();
        for (SignalValue sv : signalValueList) {
            MonitorDataChartView.MonitorDataItem item = new MonitorDataChartView.MonitorDataItem();
            item.setTime(sv.getTime());
            item.setValue(Float.parseFloat(sv.getValue()));
            monitorDataItemList.add(item);
        }
        monitorData.setListData(monitorDataItemList);
        monitorData.setColor(getResources().getColor(R.color.homepage_main));
//                monitorData.setDataName(deviceSignalTypeList.get(currentSignalId).getDeviceSignalTypeName());
        data.add(monitorData);

        if (signalValueListExtral.size() > 0) {
            monitorData = new MonitorDataChartView.MonitorData();
            monitorDataItemList = new ArrayList<>();
            for (SignalValue sv : signalValueListExtral) {
                MonitorDataChartView.MonitorDataItem item = new MonitorDataChartView.MonitorDataItem();
                item.setTime(sv.getTime());
                item.setValue(Float.parseFloat(sv.getValue()));
                monitorDataItemList.add(item);
            }
            monitorData.setListData(monitorDataItemList);
            monitorData.setColor(getResources().getColor(R.color.red_light));
//                monitorData.setDataName(deviceSignalTypeList.get(currentSignalId).getDeviceSignalTypeName());
            data.add(monitorData);
        }

        float max = 0, min = 0;
        if (signalValueListExtral.size() == 0) {
            max = getMValue(signalValueList, true);
        } else {
            max = getMValue(signalValueListExtral, true);
        }
        min = getMValue(signalValueList, false);
        if (max == min) {
            monitorDataChartView.setupData(min - Float.parseFloat(signalValueList.get(0).getValue()) / 2, max + Float.parseFloat(signalValueList.get(0).getValue()), data);
        } else {
            monitorDataChartView.setupData(min - (max - min) / 2, max + (max - min) / 2, data);
        }
        monitorDataChartView.updateData();
    }

    private float getMValue(List<SignalValue> data, boolean isMax) {
        float m = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (isMax) {
            for (SignalValue sv : data) {
                if (Float.parseFloat(sv.getValue()) > m) {
                    m = Float.parseFloat(sv.getValue());
                }
            }
        } else {
            for (SignalValue sv : data) {
                if (Float.parseFloat(sv.getValue()) < m) {
                    m = Float.parseFloat(sv.getValue());
                }
            }
        }
        return m;
    }

    private void getAverageValueByDay() {
        currentCall = instance(getContext()).getAverageValueByDay(switchId, signalTypeId, time, new ActionCallbackListener<List<SignalValue>>() {
            @Override
            public void onSuccess(List<SignalValue> data) {
                signalValueList.clear();
                signalValueListExtral.clear();
                signalValueList.addAll(data);
                notifyDataListChanged();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                loadingDlg.dismiss();
                monitorDataChartView.clearData();
                if (isAdded()) {
                    if (errorEvent == 12) {
//                    CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.local_error_message_no_data));
                    } else {
                        CustomToast.showCustomErrorToast(getContext(), getString(R.string.get_data_fail));
                    }
                } else {
                    Log.e("CurveFragment", "isDetached");
                }

            }
        });
    }

    private void getValueByMonth() {
        currentCall = Core.instance(getContext()).getSignalByTime(switchId, time, signalTypeId,
                "month", new ActionCallbackListener<List<SignalPeakValleyValue>>() {
                    @Override
                    public void onSuccess(List<SignalPeakValleyValue> data) {
                        signalValueListExtral.clear();
                        signalValueList.clear();
                        for (SignalPeakValleyValue valleyValue : data) {
                            SignalValue signalValue = new SignalValue();
                            signalValue.setTime(valleyValue.time);
                            if (valleyValue.min == 0 && valleyValue.max == 0) {
                                signalValue.setValue(valleyValue.value + "");
                                signalValueList.add(signalValue);
                            } else {
                                signalValue.setValue(valleyValue.min + "");
                                signalValueList.add(signalValue);

                                signalValue = new SignalValue();
                                signalValue.setTime(valleyValue.time);
                                signalValue.setValue(valleyValue.max + "");
                                signalValueListExtral.add(signalValue);
                            }
                        }
                        notifyDataListChanged();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        loadingDlg.dismiss();
                        monitorDataChartView.clearData();
                        if (isAdded()) {
                            if (errorEvent == 12) {
//                    CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.local_error_message_no_data));
                            } else {
                                CustomToast.showCustomErrorToast(getContext(), getString(R.string.get_data_fail));
                            }
                        } else {
                            Log.e("CurveFragment", "isDetached");
                        }
                    }
                });
    }

    /*private void getAverageValueByMonth() {
       currentCall = Core.instance(getContext()).getAverageValueByMonth(switchId, signalTypeId, time, new ActionCallbackListener<List<SignalValue>>() {
            @Override
            public void onSuccess(List<SignalValue> data) {
                signalValueList.clear();
                signalValueList.addAll(data);
                notifyDataListChanged();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                loadingDlg.dismiss();
                monitorDataChartView.clearData();
                if (isAdded()) {
                    if (errorEvent == 12) {
                        message = getString(R.string.local_error_message_no_data);
                    } else {
                        message = getString(R.string.get_data_fail);
                    }
                    CustomToast.showCustomErrorToast(getContext(), message);
                } else {
                    Log.e("CurveFragment", "isDetached");
                }

            }
        });
    }
*/
    public void setOnShowingListener(OnShowingListener onShowingListener) {
        listener = onShowingListener;
    }

    public interface OnShowingListener {
        void onShowing(CurveFragment curveFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
            loadingDlg.dismiss();
            Log.d("CurveFragment", "onDestroyView cancel call");
        }
        Log.d("CurveFragment", "onDestroyView");
    }

}
