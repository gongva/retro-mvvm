package com.hik.core.android.api;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * 进程/线程相关的Api库
 *
 * @author gongwei
 * @time 2019/10/11
 * @mail shmily__vivi@163.com
 */
public class ProcessUtil {

    /**
     * 判断application是否是在主线程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }

    /**
     * 获取当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        String processName = null;

        // MessageFlowActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        //fix bug 21902
        if (am == null || am.getRunningAppProcesses() == null) {
            return processName;
        }
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
