package com.gongva.retromvvm.repository.common.remote;

import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.retromvvm.common.SystemConfig;
import com.gongva.retromvvm.common.manager.login.LoginInfo;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * API
 *
 * @data 2019/3/11
 */
public interface IApi {

    /**
     * 获取系统配置
     *
     * @return
     */
    @GET("/sys/config")
    Observable<ResponseResult<SystemConfig>> getSysConfig();

    /**
     * Streaming作用：访问网络，下载大文件。
     * 默认情况下，Retrofit在处理结果前会将服务器端的Response全部读进内存。
     * 如果服务器端返回的是一个非常大的文件，则容易oom。
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String urlString);

    /**
     * postDemo
     *
     * @return
     */
    @POST("/sys/postDemo")
    Observable<ResponseResult<Void>> postDemo(@Body RequestBody body);

    /**
     * getDemo
     *
     * @return
     */
    @GET("/sys/getDemo")
    Observable<ResponseResult<JsonObject>> getDemo(@Query("userId") String userId);

    /**
     * headerDemo
     *
     * @return
     */
    @POST("/sys/headerDemo")
    Observable<ResponseResult<Void>> headerDemo(@Header("Token") String token);

    /**
     * urlDemo 完整Api Url
     *
     * @return
     */
    @POST("/sys/urlDemo")
    Observable<ResponseResult<Void>> urlDemo(@Url String url, @Body RequestBody Body);

    /**
     * fieldDemo form表单
     *
     * @return
     */
    @POST("/sys/fieldDemo")
    @FormUrlEncoded
    Observable<ResponseResult<Void>> fieldDemo(@Field("description") String description);

    /**
     * 登录
     *
     * @return
     */
    @POST("/sys/login")
    @FormUrlEncoded
    Observable<ResponseResult<LoginInfo>> login(@Body RequestBody body);

    /**
     * 登出
     *
     * @return
     */
    @POST("/sys/logout")
    @FormUrlEncoded
    Observable<ResponseResult<Void>> logout(@Header("Token") String token);
}
