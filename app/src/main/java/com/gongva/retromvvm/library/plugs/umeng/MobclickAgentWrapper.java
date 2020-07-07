package com.gongva.retromvvm.library.plugs.umeng;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Fragment的访问统计，需要结合UserVisibleHint、onResume和onPause一起来解决
 *
 * @author gongwei
 * @time 2019/12/26
 */
public class MobclickAgentWrapper {

    /**
     * 请在Fragment的onResume中调用
     *
     * @param fragment
     */
    public static void onResume(@NonNull Fragment fragment) {
        if (fragment.getUserVisibleHint()) {
            onVisibilityChangedToUser(fragment.getClass().getSimpleName(), true);
        }
    }

    /**
     * 请在Fragment的onPause中调用
     *
     * @param fragment
     */
    public static void onPause(@NonNull Fragment fragment) {
        if (fragment.getUserVisibleHint()) {
            onVisibilityChangedToUser(fragment.getClass().getSimpleName(), false);
        }
    }

    /**
     * 请在Fragment的setUserVisibleHint中调用
     *
     * @param fragment
     */
    public static void setUserVisibleHint(@NonNull Fragment fragment, boolean isVisibleToUser) {
        if (fragment.isResumed()) {
            onVisibilityChangedToUser(fragment.getClass().getSimpleName(), isVisibleToUser);
        }
    }

    /**
     * @param pageSimpleName
     * @param isVisibleToUser 用户可见传true，不可见传false
     */
    private static void onVisibilityChangedToUser(@NonNull String pageSimpleName, boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //todo if need. Fragment's PageStart for Umeng.
            //MobclickAgent.onPageStart(pageSimpleName);
        } else {
            //todo if need. Fragment's PageEnd for Umeng.
            //MobclickAgent.onPageEnd(pageSimpleName);
        }
    }

}
