package com.rejuvee.smartelectric.family.common;

import android.Manifest;
import android.app.Activity;
import android.os.Build;

import com.base.library.utils.PermissionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuchengran on 2018/3/28.
 */

public class PermisionManage {
    private static PermisionManage instance;
    private Map<String, Boolean> mapPermisionState = new HashMap<>();
    private String[] needPermissions = new String[]{//app需要的所有动态申请权限列表
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.REQUEST_INSTALL_PACKAGES
    };


    private int requestCode = 1001;

    private PermisionManage() {
        mapPermisionState.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
        mapPermisionState.put(Manifest.permission.CAMERA, true);
        mapPermisionState.put(Manifest.permission.ACCESS_NETWORK_STATE, true);
        mapPermisionState.put(Manifest.permission.ACCESS_FINE_LOCATION, true);
        mapPermisionState.put(Manifest.permission.REQUEST_INSTALL_PACKAGES, true);
    }

    public static PermisionManage getInstance() {
        if (instance == null) {
            instance = new PermisionManage();
        }
        return instance;
    }

    private PermissionUtils.OnPermissionListener mListener = new PermissionUtils.OnPermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(String[] deniedPermissions) {
            for (String permission : deniedPermissions) {
                mapPermisionState.put(permission, false);
            }
        }
    };

    public void initPermisstion(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.requestPermissions(activity, requestCode, needPermissions, mListener);
        }
    }

    public void startRequest(Activity activity, PermissionUtils.OnPermissionListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.requestPermissions(activity, requestCode, needPermissions, listener);
        }
    }

    public void startRequest(Activity activity, String[] permissions, PermissionUtils.OnPermissionListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.requestPermissions(activity, requestCode, permissions, listener);
        }

    }

    public Boolean isPermissionGranted(String permission) {
        return mapPermisionState.get(permission);
    }


}
