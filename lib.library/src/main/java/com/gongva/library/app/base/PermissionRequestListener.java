package com.gongva.library.app.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.gongva.library.app.config.AppConfig;
import com.gongva.library.app.ui.UI;

import java.util.List;

/**
 * 6.0以上请求授权的Listener
 *
 * @author gongwei 2018.12.20
 */
public abstract class PermissionRequestListener implements IPermissionRequestListener {

    //拒绝授权
    public void onDenied(Activity activity, List<String> deniedPermission) {
        if (deniedPermission != null && deniedPermission.size() > 0) {
            String notice = "部分权限获取失败，请在权限管理中开启";
            String denied = deniedPermission.get(0);
            switch (denied) {
                case Manifest.permission.CAMERA:
                    notice = "相机权限获取失败，请在权限管理中开启";
                    break;
                case Manifest.permission.RECORD_AUDIO:
                    notice = "录音权限获取失败，请在权限管理中开启";
                    break;
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    notice = "媒体与文件访问权限获取失败，请在权限管理中开启";
                    break;
                case Manifest.permission.CALL_PHONE:
                    notice = "拨号权限获取失败，请在权限管理中开启";
                    break;
                case Manifest.permission.READ_PHONE_STATE:
                    notice = "设备信息读取权限获取失败，请在权限管理中开启";
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                    notice = "定位获取失败，请在权限管理中开启以获取手机精准定位";
                    break;
                case Manifest.permission.WRITE_SETTINGS:
                    notice = "修改系统设置权限获取失败，请在权限管理中开启";
                    break;
            }
            UI.showConfirmDialog(activity, notice, "以后再说", null, "去开启", (View v) -> {
                startToPermissionSetting(activity);
            });
        }
    }

    /**
     * 跳转权限设置
     */
    private void startToPermissionSetting(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            localIntent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", AppConfig.getAppPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", AppConfig.getAppPackageName());
        }
        activity.startActivity(localIntent);
    }
}
