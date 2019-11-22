package com.rejuvee.smartelectric.family.common.manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

public class PermissionManage {
    private static PermissionManage instance;
    private PermissionCallBack callBack;

    private PermissionManage() {

    }

    public static PermissionManage getInstance() {
        if (instance == null) {
            instance = new PermissionManage();
        }
        return instance;
    }

    public PermissionManage setCallBack(PermissionCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    /**
     * 获取存储定位权限
     *
     * @param context
     */
    @SuppressLint("WrongConstant")
    public void hasLocationStorage(Context context) {
        AndPermission.with(context)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    callBack.onGranted();
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    callBack.onDenied();
                })
                .start();
    }

    /**
     * 获取相机权限
     *
     * @param context
     */
    @SuppressLint("WrongConstant")
    public void hasCamera(Context context) {
        AndPermission.with(context)
                .runtime()
                .permission(Manifest.permission.CAMERA)
                .onGranted(permissions -> {
                    callBack.onGranted();
                    // Storage permission are allowed.
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    callBack.onDenied();
                })
                .start();
    }

    /**
     * 获取读写系统权限
     *
     * @param context
     */
    @SuppressLint("WrongConstant")
    public void hasWriteSetting(Context context) {
        AndPermission.with(context)
                .runtime()
                .permission(Manifest.permission.WRITE_SETTINGS)
                .onGranted(permissions -> {
                    callBack.onGranted();
                    // Storage permission are allowed.
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    callBack.onDenied();
                })
                .start();
    }

    /**
     * 获取安装权限
     *
     * @param context
     */
    public void hasInstall(Context context) {
        AndPermission.with(context)
                .install()
//                    .permission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                .onGranted(permissions -> {
                    callBack.onGranted();
                    // Storage permission are allowed.
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    callBack.onDenied();
                })
                .start();
    }

    public interface PermissionCallBack {
        void onGranted();

        void onDenied();
    }
}
