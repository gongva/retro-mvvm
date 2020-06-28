package com.gongva.retromvvm.common.manager.download;

/**
 * 文件下载接口
 *
 * @date 2019/9/19
 */
public interface IDownloadCallback {
    /**
     * 下载成功
     *
     * @param filePath 本地保存的文件路径
     */
    void downloadSuccess(String filePath);

    /**
     * 下载失败
     *
     * @param msg 下载失败的原因
     */
    void downloadFail(String msg);

    /**
     * 下载状态
     *
     * @param urlPath
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    void update(String urlPath, long bytesRead, long contentLength, boolean done);
}
