package com.hik.core.android.api.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 网络监听管理器
 *
 * @author gongwei
 * @time 2020/03/11
 * @mail gongwei5@hikcreate.com
 */
public class NetworkManager implements NetworkStateCallback {
    static final String TAG = NetworkManager.class.getSimpleName();
    private Context context;
    private TelephonyManager telephonyManager;
    private BroadcastReceiver connectivityReceiver;
    private PhoneStateListener phoneStateListener;
    private NetworkStateCallback call;

    public NetworkManager(Context context) {
        this.context = context;
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.connectivityReceiver = new ConnectivityReceiver(this);
        this.phoneStateListener = new PhoneStateChangeListener(this);
    }

    public void registerConnectivityReceiver(NetworkStateCallback call) {
        this.call = call;
        Log.d(TAG, "registerConnectivityReceiver()...");
        this.telephonyManager.listen(this.phoneStateListener, 64);
        this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public void unregisterConnectivityReceiver() {
        Log.d(TAG, "unregisterConnectivityReceiver()...");
        this.telephonyManager.listen(this.phoneStateListener, 0);
        this.context.unregisterReceiver(this.connectivityReceiver);
    }

    public void onConnected(int netType) {
        if (this.call != null) {
            this.call.onConnected(netType);
        }

    }

    public void onDisconnected(int netType) {
        if (this.call != null) {
            this.call.onDisconnected(netType);
        }

    }
}
