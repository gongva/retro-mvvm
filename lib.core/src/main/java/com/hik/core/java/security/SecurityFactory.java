package com.hik.core.java.security;

import android.content.Context;

/**
 * 秘钥管理器
 *
 * @author gongwei
 * @time 2020/03/20
 * @mail shmily__vivi@163.com
 */
public class SecurityFactory {

    static {
        System.loadLibrary("getkey-lib");
    }

    /**
     * 获取加密秘钥
     *
     * @param context
     * @return
     */
    public static native String getKeyFromNative(Context context);
}
