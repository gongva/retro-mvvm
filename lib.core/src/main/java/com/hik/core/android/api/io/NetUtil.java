package com.hik.core.android.api.io;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

/**
 * 网络工具方法集
 *
 * @author gongwei
 * @time 2019/11/26
 * @mail shmily__vivi@163.com
 */
@SuppressLint("WrongConstant")
public class NetUtil {
    public NetUtil() {
    }

    public static boolean isNetConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo info = connMgr.getNetworkInfo(1);
        return info != null && info.isConnected();
    }

    public static String getIPAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
        WifiInfo info = wifiMgr.getConnectionInfo();
        return info != null ? Formatter.formatIpAddress(info.getIpAddress()) : null;
    }

    public String getMacAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
        WifiInfo info = wifiMgr.getConnectionInfo();
        return info != null ? info.getMacAddress() : null;
    }

    public static String getGateway(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
        DhcpInfo info = wifiMgr.getDhcpInfo();
        return info != null ? Formatter.formatIpAddress(info.gateway) : null;
    }

    public static String getWifiSSID(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
        WifiInfo info = wifiMgr.getConnectionInfo();
        return info != null ? removeDoubleQuotes(info.getSSID()) : null;
    }

    public static String getWifiBSSID(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
        WifiInfo info = wifiMgr.getConnectionInfo();
        return info != null ? info.getBSSID() : null;
    }

    public static String removeDoubleQuotes(String string) {
        if (string == null) {
            return null;
        } else {
            int length = string.length();
            return length > 1 && string.charAt(0) == '"' && string.charAt(length - 1) == '"' ? string.substring(1, length - 1) : string;
        }
    }
}
