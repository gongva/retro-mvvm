package com.hik.core.android.api.network;

/**
 * @author gongwei
 * @time 2020/03/11
 * @mail gongwei5@hikcreate.com
 */
public interface NetworkStateCallback {
    void onConnected(int var1);

    void onDisconnected(int var1);
}
