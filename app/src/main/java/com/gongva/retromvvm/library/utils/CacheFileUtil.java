package com.gongva.retromvvm.library.utils;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.hik.core.android.api.io.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理器
 *
 * @author gongwei
 * @time 2019/10/29
 * @mail shmily__vivi@163.com
 */
public class CacheFileUtil {

    /**
     * 计算所有缓存所占空间
     *
     * @param context
     * @return
     */
    public static long calcAllCache(Context context) {
        List<File> listFile = getAllCacheFile(context);
        long allCacheSize = 0;
        for (File file : listFile) {
            allCacheSize += calcFileTotalSize(file);
        }
        return allCacheSize;
    }

    /**
     * 递归计算File空间
     *
     * @param file
     * @return
     */
    public static long calcFileTotalSize(File file) {
        if (file != null && file.exists()) {
            long totalSize = 0;
            if (file.isFile() && !file.getName().startsWith(".")) {//隐藏文件不计入统计了，华为...
                totalSize += file.length();
            } else if (file.isDirectory()) {
                try {
                    for (File child : file.listFiles()) {
                        totalSize += calcFileTotalSize(child);
                    }
                } catch (Exception e) {
                    //如果未授权有可能报错
                }
            }
            return totalSize;
        } else {
            return 0;
        }
    }

    /**
     * 计算所有缓存所占空间
     *
     * @param context
     * @return
     */
    public static void deleteAllCache(Context context) {
        List<File> listFile = getAllCacheFile(context);
        for (File file : listFile) {
            deleteFile(file);
        }
    }

    /**
     * 递归删除文件(加6)
     *
     * @param file
     * @return
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                try {
                    for (File child : file.listFiles()) {
                        deleteFile(child);
                    }
                } catch (Exception e) {
                    //如果未授权有可能报错
                }
            }
        }
    }

    /**
     * 获取所有缓存目录
     *
     * @param context
     * @return
     */
    public static List<File> getAllCacheFile(Context context) {
        List<File> list = new ArrayList<>();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
            File internalCacheDir = context.getCacheDir();
            list.add(internalCacheDir);
            File cacheLog = new File(context.getExternalCacheDir().getAbsolutePath() + "/log");
            list.add(cacheLog);
        } else {
            File cacheHttp = new File(context.getCacheDir().getAbsolutePath() + "/ahttp-cache");
            list.add(cacheHttp);
        }
        File cacheExternalImage = new File(FileUtil.IMAGE_CACHE);
        list.add(cacheExternalImage);

        File cacheExternalFile = new File(FileUtil.FILE_CACHE);
        list.add(cacheExternalFile);

        File cacheDICMIamge = new File(FileUtil.getDCIMBasePath());
        list.add(cacheDICMIamge);

        File gideCache = Glide.getPhotoCacheDir(context);
        list.add(gideCache);
        return list;
    }
}
