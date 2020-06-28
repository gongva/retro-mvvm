package com.gongva.retromvvm.library.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.base.ActivitySupport;
import com.gongva.library.app.base.PermissionRequestListener;
import com.gongva.library.app.ui.UI;
import com.gongva.retromvvm.common.GvContext;
import com.hik.core.android.api.GsonUtil;
import com.hik.core.android.api.MediaUtil;
import com.hik.core.android.api.io.FileUtil;
import com.hik.core.android.api.io.SharePreferencesUtil;
import com.hik.core.java.security.AESUtil;
import com.hik.core.java.security.DESUtil;
import com.hik.core.java.security.MD5Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desc:通用工具类
 * CreateTime: 2018/12/25
 */
public class Tools {

    private static String cameraFilePath; //相机拍照文件路径

    /**
     * SD卡是否可用
     *
     * @return
     */
    private static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 把文件扫描到媒体库
     *
     * @param filePath
     * @return
     */
    public static String notifyFileToMedia(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            MediaUtil.scanFile(TinkerApplicationCreate.getApplication(), filePath);
        }
        return filePath;
    }

    /**
     * 以当前时间作为文件名的图片保存路径
     * 目标目录:
     *
     * @return
     */
    public static String newPicPath() {
        return newPicPath(String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 获取照片存储文件夹
     *
     * @return
     */
    public static String newPicPathDir() {
        File myPath = new File(FileUtil.getDCIMBasePath());
        if (!myPath.exists()) {
            myPath.mkdir();
        }
        return FileUtil.getDCIMBasePath();
    }

    /**
     * 图片保存路径
     *
     * @param fileName
     * @return
     */
    public static String newPicPath(String fileName) {
        cameraFilePath = FileUtil.getDCIMBasePath() + fileName + ".jpg";
        new File(cameraFilePath).getParentFile().mkdirs();
        return cameraFilePath;
    }

    /**
     * 获取相机拍摄的照片
     *
     * @return
     */
    public static String getCameraFilePath() {
        return cameraFilePath;
    }

    /**
     * 判断字符串是否 base64
     *
     * @param str
     * @return
     */
    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * 使用AES解密，key使用DES默认key
     *
     * @param ext
     * @return
     */
    public synchronized static String decryptWithAES(String ext) {
        if (TextUtils.isEmpty(ext) || !isBase64(ext)) return null;
        String key32 = MD5Util.getMD5(DESUtil.getKeyFromNative(TinkerApplicationCreate.getApplication()));
        return AESUtil.decrypt(ext, key32);
    }

    /**
     * 使用AES加密，key使用DES默认key
     *
     * @param ext
     * @return
     */
    public synchronized static String encryptWithAES(String ext) {
        if (TextUtils.isEmpty(ext)) return null;
        String key32 = MD5Util.getMD5(DESUtil.getKeyFromNative(TinkerApplicationCreate.getApplication()));
        return AESUtil.encrypt(ext, key32);
    }

    /**
     * 打开相机，默认RequestCode：BMCContext.REQUEST_CODE_OPEN_CAMERA
     *
     * @param activity
     */
    public static void openCamera(final Activity activity) {
        openCamera(activity, GvContext.REQUEST_CODE_OPEN_CAMERA);
    }

    /**
     * 打开相机，校验权限
     *
     * @param activity
     * @param requestCode
     */
    public static void openCamera(final Activity activity, final int requestCode) {
        if (!isCanUseSD()) {
            UI.showToast("未找到存储卡，不能拍照");
            return;
        }
        ActivitySupport.requestRunPermission(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, new PermissionRequestListener() {
            @Override
            public void onGranted() {
                openCameraGo(activity, requestCode);
            }
        });
    }

    /**
     * 打开相机
     *
     * @param activity
     * @param requestCode
     */
    private static void openCameraGo(Activity activity, int requestCode) {
        newPicPath();
        File cameraFile = new File(cameraFilePath);
        cameraFile.getParentFile().mkdirs();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFileCompat(activity, cameraFile));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取打开相机的URI
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriFromFileCompat(Context context, File file) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return Uri.fromFile(file);
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            return FileProvider.getUriForFile(context, getFileProviderAuthorities(context), file);
        }
    }

    /**
     * 获取fileProvider
     *
     * @param context
     * @return
     */
    public static String getFileProviderAuthorities(Context context) {
        return context.getPackageName() + ".fileProvider";
    }

    /**
     * 打开裁剪
     *
     * @param activity
     * @param filePath
     * @param aspectWidth
     * @param aspectHeight
     * @param outputWidth
     * @param outputHeight
     */
    public static void openCrop(Activity activity, String filePath, int aspectWidth, int aspectHeight, int outputWidth, int outputHeight) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        File file = new File(filePath);
        if (file != null && !file.exists()) {
            UI.showToast("图片错误");
            return;
        }
        intent.setDataAndType(getUriFromFileCompat(activity, file), "image/*");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra("crop", "true");
        if (aspectWidth != 0 && aspectHeight != 0) {
            intent.putExtra("aspectX", aspectWidth);
            intent.putExtra("aspectY", aspectHeight);
        }
        intent.putExtra("outputX", outputWidth);
        intent.putExtra("outputY", outputHeight);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        Uri outUri = Uri.fromFile(newCropTmpFile(activity));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        if (Build.VERSION.SDK_INT >= 26) {
            activity.grantUriPermission(activity.getPackageName(), outUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
        activity.startActivityForResult(intent, GvContext.REQUEST_CODE_IMAGE_CROP);
    }

    /**
     * 获取裁剪时的临时文件
     *
     * @param context
     * @return
     */
    public static File getCropTmpFile(Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            file = context.getCacheDir();
        }
        File tmpFile = new File(file.getAbsolutePath() + File.separator + "cropTmp.jpg");
        return tmpFile;
    }

    /**
     * 新建裁剪时的临时文件
     *
     * @param context
     * @return
     */
    private static File newCropTmpFile(Context context) {
        File tmpFile = getCropTmpFile(context);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        try {
            tmpFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpFile;
    }

    /**
     * 复制文件
     *
     * @param oldFile
     * @param newFile
     */
    public static void copyFile(File oldFile, File newFile) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            if (oldFile.exists()) {
                inStream = new FileInputStream(oldFile);
                fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder insertSpaces(String numbers) {
        if (numbers == null) {
            return new StringBuilder();
        }
        StringBuilder sbCardNumForShow = new StringBuilder(numbers);
        if (!numbers.contains(" ")) {
            //没有空格分隔，添加
            int sum = numbers.length() / 4;
            for (int i = 1; i <= sum; i++) {
                int index = sum * i;
                if (index < numbers.length()) {
                    sbCardNumForShow.insert(index + i - 1, " ");
                }
            }
        }
        return sbCardNumForShow;
    }


    public static String decodeUrl(String url) {
        return URLDecoder.decode(url);
    }
    
    /**
     * 从一段html代码中扣出文字
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        // 定义HTML标签的正则表达式
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>";

        // 过滤script标签
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        // 过滤style标签
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        // 过滤html标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        htmlStr = htmlStr.replace("&nbsp", "");
        htmlStr = htmlStr.replace("\\n", "");
        htmlStr = htmlStr.replace("\\r", "");
        htmlStr = htmlStr.replace("\n", "");
        htmlStr = htmlStr.replace("\r", "");

        return htmlStr.trim(); //返回文本字符串
    }

    /**
     * 工具方法：生成带颜色的文字
     * 应用场景：搜索时关键字高亮或变色
     *
     * @param originalString 完整的字符串
     * @param colorKeys      需要变色的关键字
     * @param colors         关键字颜色
     * @return
     */
    public static CharSequence generateColorText(String originalString, String[] colorKeys, int[] colors) {
        if (originalString == null || colorKeys == null || colors == null || colorKeys.length == 0 || colors.length == 0 || (colorKeys.length != colors.length)) {
            return originalString;
        }
        StringBuilder sb = new StringBuilder(originalString);
        SpannableString spannableString = new SpannableString(sb);
        for (int i = 0; i < colorKeys.length; i++) {
            int fromIndex = -colorKeys[i].length();
            List<Integer> listIndex = new ArrayList<>();
            while ((fromIndex = originalString.indexOf(colorKeys[i], fromIndex + colorKeys[i].length())) > -1) {
                listIndex.add(fromIndex);
            }
            for (Integer index : listIndex) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(colors[i]);
                spannableString.setSpan(colorSpan, index, index + colorKeys[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    /**
     * 截屏View到相册
     * 注意：截屏保存可能需要时间，过程中请控制View所在Activity不要finish
     *
     * @param v
     */
    public static void saveViewToDCIM(ViewGroup v) {
        ActivitySupport.requestRunPermission(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionRequestListener() {
            @Override
            public void onGranted() {
                saveViewToDCIMGo(v);
            }
        });
    }

    private static void saveViewToDCIMGo(ViewGroup v) {
        boolean success = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            int h = 0;
            if (v.getHeight() > 0) {
                h = v.getHeight();
            } else {
                for (int i = 0; i < v.getChildCount(); i++) {
                    h += v.getChildAt(i).getMeasuredHeight();
                }
            }
            if (v.getWidth() <= 0 || h <= 0) {
                UI.showToast("保存失败，请重试");
                return;
            }
            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), h, Bitmap.Config.RGB_565);
            final Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            String picPath = newPicPath();
            File saveFile = new File(picPath);
            FileOutputStream fos = null;
            if (saveFile != null && saveFile.exists()) {
                saveFile.delete();
            }
            try {
                saveFile.createNewFile();
                fos = new FileOutputStream(saveFile);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                    success = true;
                    MediaUtil.scanFile(TinkerApplicationCreate.getApplication(), saveFile.getAbsolutePath());
                    UI.showToast(String.format("已保存至%s", picPath));
                } else {
                    success = false;
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            success = false;
        }
        if (!success) {
            UI.showToast("保存失败，请检查存储卡状态");
        }
    }
}
