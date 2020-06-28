package com.hik.core.android.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.hik.core.android.api.LogCat;

/**
 * 用于存放View的公共方法
 *
 * @date 2019/5/23
 */
public class ViewUtil {


    /**
     * 启动CoordinatorLayout滑动折叠
     * https://www.jianshu.com/p/7b997c2e436c
     *
     * @param appBarLayout
     */
    public static void enableExitUtilCollapsed(AppBarLayout appBarLayout) {
        int i0 = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
        int i1 = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
        View appBarChildAt = appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
        appBarParams.setScrollFlags(i0 | i1);// 重置折叠效果
        appBarChildAt.setLayoutParams(appBarParams);
    }

    /**
     * 关闭CoordinatorLayout滑动折叠
     *
     * @param appBarLayout
     */
    public static void disableCollapsed(AppBarLayout appBarLayout) {
        View appBarChildAt = appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
        appBarParams.setScrollFlags(0);//这个加了之后不可滑动
        appBarChildAt.setLayoutParams(appBarParams);
    }

    /**
     * 通过View获取Activity实例
     * 使用请判空
     *
     * @return host activity; or null if not available
     */
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        if (context != null) {
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }

    /**
     * TextView的文字是否是否超过行数
     *
     * @param textView
     * @param maxLines
     * @param callBack
     */
    public static void isOverFlowed(final TextView textView, final int maxLines, final TextOverFlowedCallBack callBack) {
        if (callBack == null) return;
        textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这个回调会调用多次，获取完行数记得注销监听
                textView.getViewTreeObserver().removeOnPreDrawListener(this);
                int lines = textView.getLineCount();
                LogCat.e("xwm", textView.getText() + "--" + lines);
                if (lines > maxLines) {
                    callBack.overFlowed(true);
                } else {
                    callBack.overFlowed(false);
                }
                return true;
            }
        });
    }

    public interface TextOverFlowedCallBack {
        void overFlowed(boolean isOverFlowed);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }
}