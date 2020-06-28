package com.hik.core.android.api.io;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hik.core.android.api.LogCat;
import com.hik.core.android.api.MediaUtil;
import com.hik.core.android.view.imageview.BitmapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * 文件工具类
 * 注意：使用前，请在Application初始化时调用initCachePath()方法设置应用根路径(appRootFilePath)与英文应用名称(appName)
 *
 * @author gongwei
 * @date 2019.1.25
 */
public class FileUtil {

    public static String APP_ROOT_FILE_PATH;//sd卡根路劲，在Application中初始化
    public static String PICTURE_FILES;//图片保存路径
    public static String IMAGE_CACHE;//图片缓存路径
    public static String FILE_CACHE;//文件缓存路径
    public static String APP_NAME;//应用名称
    public static final String APP_ROOT_FILE = "/gongva/";//app文件根路径 //todo company's app root path
    public static final String PICTURE_FILE = "gongva/";//图片保存路径目录
    public static final String IMAGE_CACHE_FILE = "cache/image/";//图片缓存路径目录
    public static final String FILE_CACHE_FILE = "cache/file/";//文件缓存路径目录

    static {
        setCachePath("/sdcard/", "");//默认不区分App目录，需要区分请在Application初始化时调用initCachePath()
    }

    /**
     * 在Application初始化APP_ROOT_FILE_PATH之后重设一下这个值，否则部分机型可能会得到null/cache/image/...这样的错误路径
     *
     * @param appRootFilePath
     */
    public static void initCachePath(String appRootFilePath, String appName) {
        setCachePath(appRootFilePath, appName);
        createDirFile(PICTURE_FILES);
        createDirFile(IMAGE_CACHE + ".nomedia");
        createDirFile(FILE_CACHE + ".nomedia");
    }

    /**
     * 设置文件路径
     *
     * @param appRootFilePath
     */
    public static void setCachePath(String appRootFilePath, String appName) {
        /*
        eg:
        /sdcard/gongva/cache/imgae/
        /sdcard/gongva/bmc/cache/imgae/
        /sdcard/gongva/otherApp/cache/image/
        */
        APP_ROOT_FILE_PATH = appRootFilePath;
        APP_NAME = appName;
        if (TextUtils.isEmpty(appName)) {
            PICTURE_FILES = APP_ROOT_FILE_PATH + APP_ROOT_FILE + PICTURE_FILE;
            IMAGE_CACHE = APP_ROOT_FILE_PATH + APP_ROOT_FILE + IMAGE_CACHE_FILE;
            FILE_CACHE = APP_ROOT_FILE_PATH + APP_ROOT_FILE + FILE_CACHE_FILE;
        } else {
            PICTURE_FILES = APP_ROOT_FILE_PATH + APP_ROOT_FILE + appName + File.separator + appName + File.separator;
            IMAGE_CACHE = APP_ROOT_FILE_PATH + APP_ROOT_FILE + appName + File.separator + IMAGE_CACHE_FILE;
            FILE_CACHE = APP_ROOT_FILE_PATH + APP_ROOT_FILE + appName + File.separator + FILE_CACHE_FILE;
        }
    }

    /**
     * 获取相册存储路径
     *
     * @return
     */
    public static String getDCIMBasePath() {
        return PICTURE_FILES;
    }
    /**
     * 创建目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    public static String getImgCachePath() {
        return IMAGE_CACHE;
    }

    public static String getImageCacheFilePath() {
        return getImageCacheFilePath("pic.jpg");
    }

    public static String getImageCacheFilePath(String fileName) {
        createDirFile(FileUtil.getImgCachePath());
        return FileUtil.getImgCachePath() + File.separator + fileName;
    }

    /**
     * 创建临时图片文件
     *
     * @param context
     * @return
     */
    public static File createImageTmpFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }
    }

    /**
     * 获取http缓存地址
     *
     * @param context
     * @return
     */
    public static File createAHttpDefaultCacheDir(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "ahttp-cache");
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    /**
     * 保存图片到SD卡App图片缓存目录
     *
     * @param bitmap 图片的bitmap对象
     * @return
     */
    public static String saveImageToSDCard(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        createDirFile(FileUtil.getImgCachePath());
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String newFilePath = FileUtil.getImgCachePath() + fileName;
        File file = createNewFile(newFilePath);
        if (file == null) {
            return null;
        }
        try {
            fileOutputStream = new FileOutputStream(newFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
        } catch (FileNotFoundException e1) {
            return null;
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return newFilePath;
    }

    /**
     * 保存图片到SD卡
     *
     * @param bitmap
     * @param path   保存路径(带文件名)
     * @return
     */
    public static boolean saveImageToSDCard(Bitmap bitmap, String path) {
        try {
            if (bitmap == null) {
                return false;
            }
            if (TextUtils.isEmpty(path)) {
                createDirFile(FileUtil.getImgCachePath());
                String fileName = UUID.randomUUID().toString() + ".jpg";
                path = FileUtil.getImgCachePath() + fileName;
            }
            File file = new File(path);
            file.createNewFile();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据路径获取图片Bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getImage(String path) {
        return scaledBitmap(path, 720, 720);
    }

    public static Bitmap scaledBitmap(String filePath, int dstWidth, int dstHeight) {
        return BitmapUtil.scaledBitmap(filePath, dstWidth, dstHeight, Bitmap.Config.RGB_565);
    }

    /**
     * 通过图片路径创建Uri
     *
     * @param context
     * @param path
     */
    public static Uri makeUriByImagePath(Context context, String path) {
        if (context == null || TextUtils.isEmpty(path)) return null;
        Uri uriForFile = FileProvider7.getUriForFile(context, new File(path));
        return uriForFile;
    }

    /**
     * 通过Uri获取图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getImagePathFromUri(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        String filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // 如果是document类型的 uri, 则通过document id来进行处理
                String documentId = DocumentsContract.getDocumentId(uri);
                if (_isMediaDocument(uri)) { // MediaProvider
                    // 使用':'分割
                    String id = documentId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = {id};
                    filePath = _getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
                } else if (_isDownloadsDocument(uri)) { // DownloadsProvider
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                    filePath = _getDataColumn(context, contentUri, null, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 如果是 content 类型的 Uri
                filePath = _getDataColumn(context, uri, null, null);
            } else if ("file".equals(uri.getScheme())) {
                // 如果是 file 类型的 Uri,直接获取图片对应的路径
                filePath = uri.getPath();
            }
            return filePath;
        } else {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(proj[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        }
    }

    /**
     * 根据Uri获取数据库_data一列的数据
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    private static String _getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    private static boolean _isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean _isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
