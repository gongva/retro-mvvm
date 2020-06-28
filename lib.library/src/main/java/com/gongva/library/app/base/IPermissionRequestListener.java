package com.gongva.library.app.base;

import android.app.Activity;

import java.util.List;

/**
 * 6.0以上请求授权的Listener
 *
 * @author gongwei
 * @time 2020/04/28
 * @mail shmily__vivi@163.com
 */
public interface IPermissionRequestListener {
    void onGranted();
    void onDenied(Activity activity, List<String> deniedPermission);
}
