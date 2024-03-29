package com.rejuvee.smartelectric.family.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * wifi设置工具类
 */
public class WifiUtil {
    private final String TAG = "WifiUtil";
    private static WifiUtil instance;
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    private DhcpInfo dhcpInfo;
    private IWifi iWifi;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    //    private Context context;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    private ConnectivityManager mConnectivityManager;

    // 私有构造器
    private WifiUtil(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        if (mWifiManager != null) {
            mWifiInfo = mWifiManager.getConnectionInfo();
        }
        if (mWifiManager != null) {
            dhcpInfo = mWifiManager.getDhcpInfo();
        }
//        List<WifiConfiguration> mWifiConfigurations = mWifiManager.getConfiguredNetworks();
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 网络状态改变监听
        NetworkRequest build = new NetworkRequest.Builder().build();
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                Log.i(TAG, "onLost");
                ///网络不可用的情况下的方法
                if (iWifi != null) {
                    iWifi.onLost();
                }
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                Log.i(TAG, "onAvailable");
                ///网络可用的情况下的方法
                if (iWifi != null) {
                    iWifi.onAvailable();
                }
            }
        };
        if (mConnectivityManager != null) {
            mConnectivityManager.requestNetwork(build, networkCallback);
        }
    }

    public static WifiUtil getInstance(Context context) {
        if (instance == null) {
            instance = new WifiUtil(context);
        }
        return instance;
    }

    public WifiUtil setCallback(IWifi iWifi) {
        this.iWifi = iWifi;
        return this;
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    private void startScan() {
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
    }

    /**
     * 获取扫描到的wifi列表
     */
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 得到MAC地址
    @SuppressLint("HardwareIds")
    public String GetMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到接入点的BSSID
    public String GetBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到接入点的SSID
    public String GetSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    // 得到接入点的IP地址
    public String getIPAddress() {
        return (mWifiInfo == null) ? "NULL" : intToIp(dhcpInfo.ipAddress);
    }

    // 得到接入点的子网掩码
    public String getNetMask() {
        return (mWifiInfo == null) ? "NULL" : intToIp(dhcpInfo.netmask);
    }

    // 关闭WIFI
    public void CloseWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }


    /**
     * 判断wifi是否连接
     *
     * @return
     */
    public boolean isWifiConnect() {
        NetworkInfo mWifi = null;
        if (mConnectivityManager != null) {
            mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        if (mWifi != null) {
            return mWifi.getState() == NetworkInfo.State.CONNECTED;
        }
        return false;
    }

    public String getSSID() {
        if (mWifiInfo != null) {
            String temp = mWifiInfo.getSSID();
            if (temp != null && (temp.startsWith("\"") && temp.endsWith("\""))) {
                temp = temp.substring(1, temp.length() - 1);
                return temp;
            }
            return temp;
        } else {
            return "";
        }
    }

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    /*
     * public enum WifiCipherType { NONE,WEP,WPA,EAP }
     */

    // 判断wifi是否加密：
    private static String getSecuritys(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return "WEP";
        } else if (result.capabilities.contains("PSK")) {
            return "WPA";
        } else if (result.capabilities.contains("EAP")) {
            return "EAP";
        }
        return "NONE";
    }

    //获取wifi是那个加密类型
    public String getWifiCipherType(Context context, String ssid) {
        String type = null;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            mWifiList = mWifiManager.getScanResults();
        }
        if (mWifiList != null) {
            for (int i = 0; i < mWifiList.size(); i++) {
                ScanResult sr = mWifiList.get(i);
                if (sr.SSID.equals(ssid))
                    type = getSecuritys(sr);
            }
        }
        if (type == null) {
            type = "WPA";
        }
        return type;
    }

    //获取wifi的信号大小
    public int getSignal(Context context, String ssid) {
        int signal = 100;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            mWifiList = mWifiManager.getScanResults();
        }
        if (mWifiList != null) {
            for (int i = 0; i < mWifiList.size(); i++) {
                ScanResult sr = mWifiList.get(i);
                if (sr.SSID.equals(ssid)) {
                    signal = Math.abs(sr.level);
                }
            }
        }

        return signal;
    }

    // 打开WIFI
    public boolean OpenWifi() {
        boolean bRet = true;
        if (!mWifiManager.isWifiEnabled()) {
            bRet = mWifiManager.setWifiEnabled(true);
            while (mWifiList == null || mWifiList.size() == 0) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                startScan();
            }
        }
        return bRet;
    }

    private String currentSSID = "";

    /**
     * 断判当前wifi 是否是连接成功 myssid
     *
     * @param context c
     * @param myssid  m
     * @return true  false
     */
    public boolean isConnectedWifi(Context context, String myssid) {
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = null;
        if (mWifiManager != null) {
            wifiInfo = mWifiManager.getConnectionInfo();
        }
        String ssid = null;
        if (wifiInfo != null) {
            ssid = wifiInfo.getSSID();
        }
        Log.i(TAG, "[" + ssid + "][" + myssid + "]");
        if (ssid != null) {
            if (ssid.contains(myssid)) {
                currentSSID = myssid;
                return true;
            }
        } else {
            // 9.0华为手机无法获取解决方案
            int networkId = 0;
            if (wifiInfo != null) {
                networkId = wifiInfo.getNetworkId();
            }
            List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                if (wifiConfiguration.networkId == networkId) {
                    ssid = wifiConfiguration.SSID;
                    if (ssid.contains(myssid)) {
                        currentSSID = myssid;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 查看以前是否也配置过这个网络
    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }

    //删除原来的所有连接
    private void deleteWifiConnect() {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                mWifiManager.disableNetwork(existingConfig.networkId);
            }

        }
    }

    // 提供一个外部接口，传入要连接的无线网
    public boolean connectNet(String SSID, String Password, String Type) {
        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
//    deleteWifiConnect();
//    forgetWifi(SSID);
        WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, Password, Type);
        int netID = mWifiManager.addNetwork(wifiConfig);
        boolean bRet = mWifiManager.enableNetwork(netID, true);
        mWifiManager.reconnect();
        return bRet;
    }

    public boolean connectNewNet(String SSID, String Password, int Type) {
        WifiConfiguration configuration = this.createWifiInfo(SSID, Password, Type);
        int wcgId = mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(wcgId, true);
        return mWifiManager.reconnect();
    }

    private WifiConfiguration CreateWifiInfo(String SSID, String Password, String Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        switch (Type) {
            case "NONE":
                config.wepKeys[0] = "";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case "WEP":
                config.preSharedKey = "\"" + Password + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case "WPA":
                config.preSharedKey = "\"" + Password + "\"";
                config.hiddenSSID = true;
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);// 对应wpa2加密方式
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);// 对应wpa加密方式
                config.status = WifiConfiguration.Status.ENABLED;
                break;
            case "EAP":
                config.preSharedKey = "\"" + Password + "\"";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);// 20120723新增
                break;
        }
        return config;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password, int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        if (Type == 1) { //WIFICIPHER_NOPASS
            config.hiddenSSID = true;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        if (Type == 2) {//WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) {//WIFICIPHER_WPA
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    public void addNetWork(WifiConfiguration configuration) {
        //mWifiManager.saveConfiguration();
    }

    /**
     * 移除wifi，因为权限，无法移除的时候，需要手动去翻wifi列表删除
     * 注意：！！！只能移除自己应用创建的wifi。
     * 删除掉app，再安装的，都不算自己应用，具体看removeNetwork源码
     */
    public String forgetWifi() {
        if (forgetWifi(currentSSID)) {
            return currentSSID;
        }
        return "";
    }

    /**
     * 移除wifi，因为权限，无法移除的时候，需要手动去翻wifi列表删除
     * 注意：！！！只能移除自己应用创建的wifi。
     * 删除掉app，再安装的，都不算自己应用，具体看removeNetwork源码
     */
    public boolean forgetWifi(String SSID) {
        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
//            Log.i(TAG, "tempConfig.networkId=" + tempConfig.networkId);
            return mWifiManager.removeNetwork(tempConfig.networkId);
            //mWifiManager.saveConfiguration();
        }
        return false;
    }

    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    public boolean addNetWork(String SSID, String password, int Type) {
        int netId = -1;
    /*先执行删除wifi操作，1.如果删除的成功说明这个wifi配置是由本APP配置出来的；
                       2.这样可以避免密码错误之后，同名字的wifi配置存在，无法连接；
                       3.wifi直接连接成功过，不删除也能用, netId = getExitsWifiConfig(SSID).networkId;*/
        if (forgetWifi(SSID)) {
            //移除成功，就新建一个
            netId = mWifiManager.addNetwork(createWifiInfo(SSID, password, Type));
        } else {
            //删除不成功，要么这个wifi配置以前就存在过，要么是还没连接过的
            if (getExitsWifiConfig(SSID) != null) {
                //这个wifi是连接过的，如果这个wifi在连接之后改了密码，那就只能手动去删除了
                netId = Objects.requireNonNull(getExitsWifiConfig(SSID)).networkId;
            } else {
                //没连接过的，新建一个wifi配置
                netId = mWifiManager.addNetwork(createWifiInfo(SSID, password, Type));
            }
        }

        //这个方法的第一个参数是需要连接wifi网络的networkId，第二个参数是指连接当前wifi网络是否需要断开其他网络
        //无论是否连接上，都返回true。。。。
        return mWifiManager.enableNetwork(netId, true);
    }

    /**
     * 获取配置过的wifiConfiguration
     */
    private WifiConfiguration getExitsWifiConfig(String SSID) {
        List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfiguration : wifiConfigurationList) {
            if (wifiConfiguration.SSID.equals("\"" + SSID + "\"")) {
                return wifiConfiguration;
            }
        }
        return null;
    }

    public interface IWifi {
        void onLost();

        void onAvailable();
    }
}

