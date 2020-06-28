package com.hik.core.android.api.network;

import android.telephony.PhoneStateListener;
import android.util.Log;

/**
 * 连接状态改变Listener
 *
 * @author gongwei
 * @time 2020/03/11
 * @mail gongwei5@hikcreate.com
 */
public class PhoneStateChangeListener extends PhoneStateListener {
    private static final String TAG = PhoneStateChangeListener.class.getSimpleName();
    private final NetworkStateCallback callback;

    public PhoneStateChangeListener(NetworkStateCallback callback) {
        this.callback = callback;
    }

    public void onDataConnectionStateChanged(int state) {
        super.onDataConnectionStateChanged(state);
        Log.i(TAG, "onDataConnectionStateChanged()...Connection State = " + this.getState(state));
        if (state == 2) {
            this.callback.onConnected(0);
        }

    }

    private String getState(int state) {
        switch (state) {
            case 0:
                return "DATA_DISCONNECTED";
            case 1:
                return "DATA_CONNECTING";
            case 2:
                return "DATA_CONNECTED";
            case 3:
                return "DATA_SUSPENDED";
            default:
                return "DATA_<UNKNOWN>";
        }
    }
}
