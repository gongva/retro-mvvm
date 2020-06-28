package com.hik.core.android.api.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络监听广播接收器
 *
 * @author gongwei
 * @time 2020/03/11
 * @mail gongwei5@hikcreate.com
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = ConnectivityReceiver.class.getSimpleName();
    private NetworkStateCallback callback;

    public ConnectivityReceiver(NetworkStateCallback callback) {
        this.callback = callback;
    }

    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "ConnectivityReceiver.onReceive()...");
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Log.i(TAG, "Network connected Type  = " + netInfo.getTypeName() + ", State = " + netInfo.getState());
            this.callback.onConnected(netInfo.getType());
        } else {
            Log.i(TAG, "Network unavailable");
            this.callback.onDisconnected(-1);
        }

    }
}
