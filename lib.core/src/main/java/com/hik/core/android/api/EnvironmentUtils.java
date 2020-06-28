package com.hik.core.android.api;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Environment方法库
 *
 * @author gongwei
 * @time 2020/03/11
 * @mail gongwei5@hikcreate.com
 */
public class EnvironmentUtils extends Environment {
    public EnvironmentUtils() {
    }

    public static DisplayMetrics getResolution(Context context) {
        WindowManager wmManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wmManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String[] getExternalStorageDirectoryAll() {
        List<String> results = new ArrayList();
        if (hasSDCard()) {
            results.add(Environment.getExternalStorageDirectory().getPath());
            File mountFile = new File("/proc/mounts");
            if (mountFile.exists()) {
                try {
                    Scanner scanner = new Scanner(mountFile);

                    while(scanner.hasNext()) {
                        String line = scanner.nextLine().trim();
                        if (line.startsWith("/dev/block/vold/")) {
                            String[] lineElements = line.split(" ");
                            String element = lineElements[1];
                            File root = new File(element);
                            if (!results.contains(root.getPath()) && root.exists() && root.isDirectory() && root.canWrite()) {
                                results.add(root.getPath());
                            }
                        }
                    }

                    scanner.close();
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            }
        }

        return (String[])results.toArray(new String[0]);
    }

    public static String[] getExternalStorageVolumePaths(Context context) {
        StorageManager sm = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);

        try {
            return (String[])StorageManager.class.getMethod("getVolumePaths").invoke(sm);
        } catch (Exception var3) {
            return new String[0];
        }
    }

    public static boolean isRootPath(String path) {
        String[] var4;
        int var3 = (var4 = getExternalStorageDirectoryAll()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            String root = var4[var2];
            if (root.equals(path)) {
                return true;
            }
        }

        return false;
    }

    /** @deprecated */
    public static boolean hasSDCard() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean hasMounted() {
        return hasSDCard();
    }

    public static long getSDTotalSize() {
        return getStatFsTotalSize(Environment.getExternalStorageDirectory().getPath());
    }

    public static String getSDTotalSize(Context context) {
        return Formatter.formatFileSize(context, getSDTotalSize());
    }

    public static long getSDAvailableSize() {
        return getStatFsAvailableSize(Environment.getExternalStorageDirectory().getPath());
    }

    public static String getSDAvailableSize(Context context) {
        return Formatter.formatFileSize(context, getSDAvailableSize());
    }

    public static long getRomTotalSize() {
        return getStatFsTotalSize(Environment.getDataDirectory().getPath());
    }

    public static String getRomTotalSize(Context context) {
        return Formatter.formatFileSize(context, getRomTotalSize());
    }

    public static long getRomAvailableSize() {
        return getStatFsAvailableSize(Environment.getDataDirectory().getPath());
    }

    public static String getRomAvailableSize(Context context) {
        return Formatter.formatFileSize(context, getRomAvailableSize());
    }

    public static long getStatFsTotalSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize;
        long totalBlocks;
        if (Build.VERSION.SDK_INT < 18) {
            blockSize = (long)stat.getBlockSize();
            totalBlocks = (long)stat.getBlockCount();
            return blockSize * totalBlocks;
        } else {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
            return blockSize * totalBlocks;
        }
    }

    public static long getStatFsAvailableSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize;
        long availableBlocks;
        if (Build.VERSION.SDK_INT < 18) {
            blockSize = (long)stat.getBlockSize();
            availableBlocks = (long)stat.getAvailableBlocks();
            return blockSize * availableBlocks;
        } else {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
            return blockSize * availableBlocks;
        }
    }

    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId();
        String tmSerial = tm.getSimSerialNumber();
        String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        UUID deviceUuid = new UUID((long)androidId.hashCode(), (long)tmDevice.hashCode() << 32 | (long)tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public static String getNativePhoneNumber(Context context) {
        TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telManager.getLine1Number();
    }

    public static String getProvidersName(Context context) {
        String result = null;
        TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telManager.getSubscriberId();
        if (!IMSI.startsWith("46000") && !IMSI.startsWith("46002")) {
            if (IMSI.startsWith("46001")) {
                result = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                result = "中国电信";
            }
        } else {
            result = "中国移动";
        }

        return result;
    }

    public static void hideSoftInputFromWindow(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static boolean checkPermission(Context context, String permName) {
        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(permName, context.getPackageName());
        return hasPerm == 0;
    }
}