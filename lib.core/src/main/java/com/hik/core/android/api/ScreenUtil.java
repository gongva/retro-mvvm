package com.hik.core.android.api;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * 屏幕相关Api库
 *
 * @author gongwei
 * @time 2019/10/11
 * @mail shmily__vivi@163.com
 */
public class ScreenUtil {
    /**
     * 获取屏幕Display
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获得状态栏高度
     * ps:非全屏+非沉浸式的Activity才可用
     *
     * @param activity
     */
    public static int getStateHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 根据宽度和比例计算高度
     *
     * @param ratio eg: 16f/7
     * @param width
     * @return
     */
    public static int getHeightByRatioAndWidth(float ratio, int width) {
        return (int) (width / ratio);
    }

    /**
     * 根据屏幕宽度和比例计算高度
     *
     * @param context
     * @param ratio   eg: 16f/7
     * @return
     */
    public static int getHeightByRatioAndScreenWidth(Activity context, float ratio) {
        return getHeightByRatioAndWidth(ratio, getScreenWidth(context));
    }
}
