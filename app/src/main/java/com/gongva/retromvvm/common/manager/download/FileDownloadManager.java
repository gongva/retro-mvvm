package com.gongva.retromvvm.common.manager.download;

import android.support.annotation.NonNull;

import com.gongva.library.plugin.netbase.NetConstant;
import com.gongva.library.plugin.netbase.scheduler.SchedulerProvider;
import com.gongva.retromvvm.repository.common.remote.ApiGenerator;
import com.gongva.retromvvm.repository.common.remote.InterceptorFactory;
import com.hik.core.android.api.LogCat;
import com.hik.core.android.api.io.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * 文件下载管理器
 *
 * @data 2019/4/9
 */
public class FileDownloadManager {
    private final static String TAG = FileDownloadManager.class.getSimpleName();

    private volatile static FileDownloadManager INSTANCE;
    private ConcurrentHashMap<String, Disposable> mDownloadDisposableMap;

    public static FileDownloadManager getInstance() {
        if (INSTANCE == null) {
            synchronized (FileDownloadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FileDownloadManager();
                }
            }
        }
        return INSTANCE;
    }

    public FileDownloadManager() {
        mDownloadDisposableMap = new ConcurrentHashMap<>();
    }

    /**
     * 下载文件
     *
     * @param urlPath          下载文件路径
     * @param downloadCallback 下载回调
     */
    public void downLoadFile(@NonNull String urlPath, @NonNull IDownloadCallback downloadCallback) {
        InterceptorFactory.getInstance().addProgressListener(urlPath, (bytesRead, contentLength, done) -> {
            downloadCallback.update(urlPath, bytesRead, contentLength, done);
        });
        Disposable disposable = ApiGenerator.getApi()
                .downloadFile(urlPath)
                .subscribeOn(SchedulerProvider.getInstance().io())//在新线程中实现该方法
                .map(responseBody -> {
                    MediaType mediaType = responseBody.contentType();
                    if (mediaType != null && (NetConstant.MiniType.IMAGE.equals(mediaType.type())
                            || NetConstant.MiniType.AUDIO.equals(mediaType.type()) || NetConstant.MiniType.VIDEO.equals(mediaType.type()))) {
                        LogCat.d("download type:" + mediaType.type());
                    }
                    return writeResponseBodyToDisk(getFileName(urlPath), responseBody);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> {
                    downloadCallback.downloadSuccess(path);
                    releaseDownload(urlPath);
                }, throwable -> {
                    throwable.printStackTrace();
                    downloadCallback.downloadFail(throwable.getMessage());
                    releaseDownload(urlPath);
                });
        mDownloadDisposableMap.put(urlPath, disposable);
    }

    /**
     * 取消下载
     *
     * @param urlPath 下载文件路径
     */
    public void cancleDownload(@NonNull String urlPath) {
        InterceptorFactory.getInstance().removeProgressListener(urlPath);
        releaseDownload(urlPath);
    }

    private void releaseDownload(@NonNull String urlPath) {
        Disposable disposable = mDownloadDisposableMap.remove(urlPath);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private static String getFileName(@NonNull String urlPath) {
        int index = urlPath.lastIndexOf(File.separatorChar);
        int qMask = urlPath.lastIndexOf('?');
        if (index < 0) {
            index = urlPath.lastIndexOf('/');
        }
        if (qMask > index) index = qMask;
        return urlPath.substring(index + 1);
    }

    /**
     * 保存下载的图片流写入SD卡文件
     *
     * @param imageName xxx.jpg
     * @param body      image stream
     */
    public String writeResponseBodyToDisk(String imageName, ResponseBody body) throws IOException {
        if (body == null) {
            return null;
        }

        LogCat.d(TAG, "download local path:" + FileUtil.FILE_CACHE);
        InputStream is = body.byteStream();
        File appDir = new File(FileUtil.FILE_CACHE);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File destFile = new File(FileUtil.FILE_CACHE, imageName);
        LogCat.d(TAG, "download local file path:" + destFile.getAbsolutePath());
        if (destFile.exists()) {
            destFile.delete();
        } else {
            destFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(destFile);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            //此处进行更新操作
            //len即可理解为已下载的字节数
            //onLoading(len, totalLength);
        }
        fos.flush();
        fos.close();
        bis.close();
        is.close();
        return destFile.getAbsolutePath();
    }
}
