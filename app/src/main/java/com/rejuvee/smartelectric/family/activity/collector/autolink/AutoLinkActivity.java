package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.utils.WifiUtil;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.databinding.ActivityAutoLinkBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.viewmodel.AutoLinkViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 电箱WIFI设置
 * <p>
 * 48899端口：C32x系列的端口，用户可以用AT指令更改
 * <p>
 * 49000端口：除C32x系列，其他WIFI模块的端口
 * <p>
 * 1902端口：有人掌控宝系列产品的端口
 *
 * @author usr_liujinqi
 */
public class AutoLinkActivity extends BaseActivity implements WifiUtil.IWifi {
    private final String TAG = "AutoLinkActivity";
    private final int RESQEST_SSID_LIST = 1;
    // 获得ssid列表指令
    private final byte[] searchCode = new byte[]{(byte) 0xff, 0x00, 0x01, 0x01, 0x02};
    //    private EditText etSsid;
    //    private EditText etPasd;
    //    private EditText etPort;
    private WifiUtil mWifiUtil;
    private WifiManager.MulticastLock lock;
    private SearchSSIDThread searchSSIDThread;
    private SendMsgThread sendMsgThread;
    private CollectorBean collectorBean;
    private ProgressDialog dialog;
    private DialogTip mDialogTip;
    //    private TextView tv_change;
    //    private TextView btn_search;
    //    private SuperTextView btn_ok;
    private boolean canSearch = true;
    private ActivityAutoLinkBinding mainBinding;
    private AutoLinkViewModel mViewModel;

    private Handler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AutoLinkActivity> activityWeakReference;

        MyHandler(AutoLinkActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AutoLinkActivity theActivity = activityWeakReference.get();
            switch (msg.what) {
                case SSIDTool.REC_DATA:// 解析接收到的数据
                    byte[] data = (byte[]) msg.obj;
                    SSIDTool.bytesToHexString(data);
                    theActivity.decodeData(data);
                    break;
                case SSIDTool.REC_WIFI:// wifi状态改变
                    theActivity.toggleTip(msg.arg1);
                    break;
                default:
                    Log.w(theActivity.TAG, "default");
                    break;
            }
        }

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_auto_link);
//    }

    @Override
    protected void initView() {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_auto_link);
        mViewModel = ViewModelProviders.of(this).get(AutoLinkViewModel.class);
        mainBinding.setVm(mViewModel);
        mainBinding.setPresenter(new Presenter());
        mainBinding.setLifecycleOwner(this);

        mViewModel.setPort("48899");
        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        mDialogTip = new DialogTip(this);

//        btn_ok = findViewById(R.id.btn_ok);
//        tv_change = findViewById(R.id.tv_change);
//        btn_search = findViewById(R.id.btn_search);

        mWifiUtil = WifiUtil.getInstance(this).setCallback(this);
        WifiManager manager = mWifiUtil.getWifiManager();

        lock = manager.createMulticastLock("fa_wifi");
        lock.acquire();
//        etSsid = findViewById(R.id.et_ssid);
//        etPasd = findViewById(R.id.et_pasd);
//        etPort = findViewById(R.id.et_port);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.vs226));

        searchSSIDThread = new SearchSSIDThread(handler);
        searchSSIDThread.start();

        sendMsgThread = new SendMsgThread(searchSSIDThread);
        sendMsgThread.start();
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.btn_search) {
//            dialog.show();
//            // 发送收索SSID指令
//            sendMsgThread.putMsg(searchCode);
//            dismiss();
//        } else if (v.getId() == R.id.btn_link) {
//            if (canSearch) {
//                linkCollectorAP(collectorBean.getCode(), collectorBean.getCode());
//            } else {
//                Log.w(TAG, "不要点太快");
//            }
//        } else if (v.getId() == R.id.btn_ok) {
//            String port = etPort.getText().toString();
//            if (TextUtils.isEmpty(port)) {
//                CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs227));
//
//                return;
//            }
//            int targetPort = Integer.parseInt(port);
//            if (targetPort < 0 || targetPort > 65535) {
//                CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs228));
//                return;
//            }
//            searchSSIDThread.setTargetPort(Integer.parseInt(port));
//            String ssid = mViewModel.getSsid().getValue();//etSsid.getText().toString();
//            String pwd = etPasd.getText().toString();
//            if (TextUtils.isEmpty(ssid)) {
//                CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs229));
//                return;
//            }
//            if (TextUtils.isEmpty(pwd)) {
//                CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs230));
//                return;
//            }
//            ensure(ssid, pwd, targetPort);
//        }
//    }

    private void ensure(String ssid, String pwd, int port) {
        mDialogTip.setTitle(getString(R.string.vs231));
        mDialogTip.setContent(getString(R.string.vs232));
        mDialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                mDialogTip.dismiss();
            }

            @Override
            public void onEnsure() {
                searchSSIDThread.setTargetPort(port);
                byte[] data = SSIDTool.generate_02_data(ssid, pwd, 0);
                sendMsgThread.putMsg(data);
                mDialogTip.dismiss();
            }
        });
        mDialogTip.show();
    }

    /**
     * 连接指定WIFI
     */
    private void linkCollectorAP(String ssid, String pass) {
        boolean b = mWifiUtil.OpenWifi();
        if (b) {
            Log.i(TAG, "OpenWifi true");
            boolean connectNet = mWifiUtil.addNetWork(ssid, pass, 3);
            Log.i(TAG, "addTempNetWork " + connectNet);
        } else {
            Log.i(TAG, "OpenWifi false");
        }
        canSearch = false;
    }

    /**
     * 根据连接结果改变提示文字
     *
     * @param isCon
     */
    private void toggleTip(int isCon) {
        String code = collectorBean.getCode();
        switch (isCon) {
            case LINK_OK:
                mainBinding.tvChange.setText(String.format(getString(R.string.vs233), code));
                mainBinding.tvChange.setTextColor(getResources().getColor(R.color.green_light));
                mainBinding.btnSearch.setEnabled(true);
                mainBinding.btnSearch.setTextColor(getResources().getColor(R.color.white));
//                btn_ok.setOnClickListener(this);
                break;
            case LINK_FAILED:
                mainBinding.tvChange.setText(String.format(getString(R.string.vs234), code));
                mainBinding.tvChange.setTextColor(getResources().getColor(R.color.red_light));
                mainBinding.btnSearch.setEnabled(false);
                mainBinding.btnSearch.setTextColor(getResources().getColor(R.color.grey));
//                btn_ok.setOnClickListener(null);
                break;
            case LINKING:
                mainBinding.tvChange.setText(getString(R.string.vs235));
                mainBinding.tvChange.setTextColor(getResources().getColor(R.color.gray));
                mainBinding.btnSearch.setEnabled(false);
                mainBinding.btnSearch.setTextColor(getResources().getColor(R.color.grey));
//                btn_ok.setOnClickListener(null);
                break;
        }
        canSearch = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESQEST_SSID_LIST && data != null) {
            String ssid = data.getStringExtra("ssid");
//            etSsid.setText(ssid);
            mViewModel.setSsid(ssid);
        }
    }


    private void dismiss() {
        handler.postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 3000);
    }

    public class Presenter {

        public void onCancel(View view) {
            finish();
        }

        public void onSubmit(View view) {
            String port = mViewModel.getPort().getValue();//etPort.getText().toString();
            if (TextUtils.isEmpty(port)) {
                CustomToast.showCustomErrorToast(AutoLinkActivity.this, getResources().getString(R.string.vs227));
                return;
            }
            String ssid = mViewModel.getSsid().getValue();//etSsid.getText().toString();
            if (TextUtils.isEmpty(ssid)) {
                CustomToast.showCustomErrorToast(AutoLinkActivity.this, getResources().getString(R.string.vs229));
                return;
            }
            String pwd = mViewModel.getPasd().getValue();//etPasd.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                CustomToast.showCustomErrorToast(AutoLinkActivity.this, getResources().getString(R.string.vs230));
                return;
            }
            int targetPort = Integer.parseInt(port);
            if (targetPort < 0 || targetPort > 65535) {
                CustomToast.showCustomErrorToast(AutoLinkActivity.this, getResources().getString(R.string.vs228));
                return;
            }
            searchSSIDThread.setTargetPort(Integer.parseInt(port));
            ensure(ssid, pwd, targetPort);
        }

        public void onLink(View view) {
            if (canSearch) {
                linkCollectorAP(collectorBean.getCode(), collectorBean.getCode());
            } else {
                Log.w(TAG, "不要点太快");
            }
        }

        public void onSearch(View view) {
            dialog.show();
            // 发送收索SSID指令
            sendMsgThread.putMsg(searchCode);
            dismiss();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
    // 退出处理
//        lock.release();
//        sendMsgThread.setSend(false);
//        searchSSIDThread.setReceive(false);
//        searchSSIDThread.close();
//        String code = collectorBean.getCode();
//        if (mWifiUtil.forgetWifi(code)) {
//            Log.i(TAG, "forgetWifi[" + code + "] true");
//        } else {
//            Log.i(TAG, "forgetWifi[" + code + "] false");
//        }
//    }

    @Override
    protected void dealloc() {
        // 退出处理
        lock.release();
        sendMsgThread.setSend(false);
        searchSSIDThread.setReceive(false);
        searchSSIDThread.close();
        String code = collectorBean.getCode();
        if (mWifiUtil.forgetWifi(code)) {
            Log.i(TAG, "forgetWifi[" + code + "] true");
        } else {
            Log.i(TAG, "forgetWifi[" + code + "] false");
        }
    }

    private final int LINK_OK = 0;
    private final int LINK_FAILED = 1;
    private final int LINKING = 2;

    @Override
    public void onLost() {
        Message msg = handler.obtainMessage(SSIDTool.REC_WIFI, LINKING, 0);
        handler.sendMessage(msg);
    }

    @Override
    public void onAvailable() {
//        String code = collectorBean != null ? collectorBean.getCode() : "";
        boolean connectedWifi = mWifiUtil.isConnectedWifi(this, collectorBean.getCode());
        if (connectedWifi) {
            Message msg = handler.obtainMessage(SSIDTool.REC_WIFI, LINK_OK, 0);
            handler.sendMessage(msg);
        } else {
            Message msg = handler.obtainMessage(SSIDTool.REC_WIFI, LINK_FAILED, 0);
            handler.sendMessage(msg);
        }
    }

    /**
     * 解析数据
     *
     * @param data
     */
    private void decodeData(byte[] data) {
        if ((data[0] & 0xff) != 0xff)// 如果接收到的数据不是0xff开头,那么丢弃
            return;
        int i = data[3] & 0xff;
//        System.out.println(i);
        switch (i) {
            case 0x81:// 解析返回列表指令
                dialog.dismiss();
                ArrayList<SSIDItem> ssids = SSIDTool.decode_81_data(data);
                if (ssids.size() != 0) {
                    Intent intent = new Intent(AutoLinkActivity.this, SsidListActivity.class);
                    intent.putParcelableArrayListExtra("ssids", ssids);
                    startActivityForResult(intent, RESQEST_SSID_LIST);
                } else {
                    CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs236));
                }
                break;
            case 0x82://  返回校验结果
                int[] values = SSIDTool.decode_82_data(data);
                if (values[0] == 0) {
                    CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs237));
                } else if (values[1] == 0) {
                    CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs238));
                } else if (values[0] == 1 && values[1] == 1) {
                    CustomToast.showCustomToast(this, getResources().getString(R.string.vs239));
                    finish();
                }
                break;
        }
    }

    /**
     * 发送消息的队列，每次发送数据时，只需要调用putMsg(byte[] data)方法
     *
     * @author usr_liujinqi
     */
    private class SendMsgThread extends Thread {
        // 发送消息的队列
        private Queue<byte[]> sendMsgQuene = new LinkedList<>();
        // 是否发送消息
        private boolean send = true;

        private SearchSSIDThread ss;

        SendMsgThread(SearchSSIDThread ss) {
            this.ss = ss;
        }

        synchronized void putMsg(byte[] msg) {
            // 唤醒线程
            if (sendMsgQuene.size() == 0)
                notify();
            sendMsgQuene.offer(msg);
        }

        public void run() {
            synchronized (this) {
                while (send) {
                    // 当队列里的消息发送完毕后，线程等待
                    while (sendMsgQuene.size() > 0) {
                        byte[] msg = sendMsgQuene.poll();
                        if (ss != null)
                            ss.sendMsg(msg);
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        void setSend(boolean send) {
            this.send = send;
        }
    }
}
