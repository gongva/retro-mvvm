package com.gongva.library.plugin.netbase.linstener;

import android.graphics.Bitmap;

import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.library.plugin.netbase.exception.ApiException;

/**
 * 图片加载请求回调接口
 *
 * @author gongwei
 * @date 2019/7/2
 * @email shmily__vivi@163.com
 */
public interface IImageRequestCallback {
    /**
     * 请求加载执行
     */
    void loadStart();

    /**
     * 加载bitmap回调结果回调
     *
     * @param responseResult
     */
    void loadData(ResponseResult<Bitmap> responseResult);

    /**
     * 错误回调
     *
     * @param e
     */
    void loadError(ApiException e);


    /**
     * 加载结束
     */
    void loadFinish();
}
