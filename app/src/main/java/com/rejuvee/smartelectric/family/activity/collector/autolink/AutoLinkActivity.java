package com.rejuvee.smartelectric.family.activity.collector.autolink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.base.library.widget.SuperTextView;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.utils.WifiUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 48899端口：C32x系列的端口，用户可以用AT指令更改
 * 49000端口：除C32x系列，其他WIFI模块的端口
 * 1902端口：有人掌控宝系列产品的端口
 *
 * @author usr_liujinqi
 */
public class AutoLinkActivity extends Activity implements OnClickListener {
    private final String TAG = "AutoLinkActivity";
    private EditText etSsid;
    private EditText etPasd;
    private EditText etPort;
    private WifiUtil mWifiUtil;
    private WifiManager.MulticastLock lock;
    private SearchSSID searchSSID;
    private SendMsgThread smt;
    private CollectorBean collectorBean;
    private ProgressDialog dialog;
    private TextView btn_change;
    private TextView btn_search;
    private SuperTextView btn_ok;
    public final int RESQEST_SSID_LIST = 1;

    // 获得ssid列表指令
    private final byte[] searchCode = new byte[]{(byte) 0xff, 0x00, 0x01, 0x01, 0x02};

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Tool.REC_DATA:// 解析接收到的数据
                    byte[] data = (byte[]) msg.obj;
                    Tool.bytesToHexString(data);
                    decodeData(data);
                    break;
                case Tool.REC_WIFI:// wifi状态改变
                    toggleBtn(msg.arg1);
                    break;
                default:
                    Log.w(TAG, "default");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_link);
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        findViewById(R.id.img_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_ok = findViewById(R.id.btn_ok);
        btn_change = findViewById(R.id.btn_change);
        btn_search = findViewById(R.id.btn_search);

        WifiManager manager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        lock = manager.createMulticastLock("fa_wifi");
        lock.acquire();

        etSsid = (EditText) findViewById(R.id.et_ssid);
        etPasd = (EditText) findViewById(R.id.et_pasd);
        etPort = (EditText) findViewById(R.id.et_port);

        dialog = new ProgressDialog(this);
        dialog.setMessage("搜索中...");

        searchSSID = new SearchSSID(handler);
        searchSSID.start();

        smt = new SendMsgThread(searchSSID);
        smt.start();

        changeWifi(collectorBean.getCode(), collectorBean.getCode());

        // 网络状态改变监听
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                super.onLost(network);
                ///网络不可用的情况下的方法
                Log.i(TAG, "onLost");
            }

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                ///网络可用的情况下的方法
                Log.i(TAG, "onAvailable");
                if (mWifiUtil != null) {
                    String code = collectorBean != null ? collectorBean.getCode() : "";
                    boolean connectedWifi = mWifiUtil.isConnectedWifi(getApplicationContext(), code);
                    if (connectedWifi) {
                        Message msg = handler.obtainMessage(Tool.REC_WIFI, 0, 0);
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage(Tool.REC_WIFI, 1, 0);
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String port = etPort.getText().toString();
        if (TextUtils.isEmpty(port)) {
            UIUtil.toastShow(this, "请输入端口");
            return;
        }
        int targetPort = Integer.parseInt(port);
        if (targetPort < 0 || targetPort > 65535) {
            UIUtil.toastShow(this, "请输入正确的端口");
            return;
        }
        searchSSID.setTargetPort(Integer.parseInt(port));
        if (v.getId() == R.id.btn_search) {
            dialog.show();
            // 发送收索SSID指令
            smt.putMsg(searchCode);
            dismiss();
        } else if (v.getId() == R.id.btn_ok) {
            String ssid = etSsid.getText().toString();
            String pwd = etPasd.getText().toString();
            if (TextUtils.isEmpty(ssid)) {
                UIUtil.toastShow(this, "请输入WIFI名");
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                UIUtil.toastShow(this, "请输入WIFI密码");
                return;
            }
            searchSSID.setTargetPort(Integer.parseInt(port));
            byte[] data = Tool.generate_02_data(ssid, pwd, 0);
            smt.putMsg(data);
        }
    }

    /**
     * 连接指定WIFI
     */
    private void changeWifi(String ssid, String pass) {
        mWifiUtil = new WifiUtil(this);
        boolean b = mWifiUtil.OpenWifi();
        if (b) {
            Log.i(TAG, "OpenWifi true");
            boolean connectNet = mWifiUtil.addNetWork(ssid, pass, 3);
            Log.i(TAG, "addNetWork " + connectNet);
        } else {
            Log.i(TAG, "OpenWifi false");
        }
    }

    private void toggleBtn(int isCon) {
        switch (isCon) {
            case 0:
                btn_change.setText("电箱[" + collectorBean.getCode() + "]连接成功");
                btn_change.setTextColor(getResources().getColor(R.color.green_light));
                btn_search.setEnabled(true);
                btn_ok.setOnClickListener(this);
                break;
            case 1:
                btn_change.setText("电箱[" + collectorBean.getCode() + "]未连接");
                btn_change.setTextColor(getResources().getColor(R.color.red_light));
                btn_search.setEnabled(false);
                btn_ok.setOnClickListener(null);
                break;
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
                ArrayList<Item> ssids = Tool.decode_81_data(data);
                if (ssids.size() != 0) {
                    Intent intent = new Intent(AutoLinkActivity.this, SsidListActivity.class);
                    intent.putExtra("ssids", ssids);
                    startActivityForResult(intent, RESQEST_SSID_LIST);
                } else {
                    UIUtil.toastShow(this, "没有搜索到SSID");//TODO
                }
                break;
            case 0x82://  返回校验结果
                int[] values = Tool.decode_82_data(data);
                if (values[0] == 0)
                    UIUtil.toastShow(this, "未发现此SSID");
                else if (values[1] == 0)
                    UIUtil.toastShow(this, "密码长度不正确");
                else if (values[0] == 1 && values[1] == 1)
                    UIUtil.toastShow(this, "发送成功，配置完成，请检查模块的状态");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESQEST_SSID_LIST && data != null) {
            String ssid = data.getStringExtra("ssid");
            etSsid.setText(ssid);
        }
    }


    private void dismiss() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 3000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出处理
        lock.release();
        smt.setSend(false);
        searchSSID.setReceive(false);
        searchSSID.close();
        if (mWifiUtil.forgetWifi(collectorBean.getCode())) {
            Log.i(TAG, "forgetWifi[" + collectorBean.getCode() + "]true");
        } else {
            Log.i(TAG, "forgetWifi[" + collectorBean.getCode() + "]false");
        }
    }

    /**
     * 发送消息的队列，每次发送数据时，只需要调用putMsg(byte[] data)方法
     *
     * @author usr_liujinqi
     */
    private class SendMsgThread extends Thread {
        // 发送消息的队列
        private Queue<byte[]> sendMsgQuene = new LinkedList<byte[]>();
        // 是否发送消息
        private boolean send = true;

        private SearchSSID ss;

        SendMsgThread(SearchSSID ss) {
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
