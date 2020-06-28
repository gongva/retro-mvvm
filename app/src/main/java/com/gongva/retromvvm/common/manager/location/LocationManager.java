package com.gongva.retromvvm.common.manager.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.base.PermissionRequestListener;
import com.gongva.library.app.ui.UI;
import com.gongva.retromvvm.base.component.ActivitySupportWrapper;
import com.gongva.retromvvm.common.GvConfig;
import com.gongva.retromvvm.common.GvContext;
import com.hik.core.android.api.LogCat;

import java.util.List;

/**
 * 定位管理器
 *
 * @ gongwei
 * @time 2019/12/20
 * @mail shmily__vivi@163.com
 */
public class LocationManager {

    //data
    protected static BDLocation _bdLocationLast;//最后一次定位成功后保存的位置数据
    //tools
    protected HikLocationManagerCallBack mCallBack;
    protected HikLocationListener _locationListener;
    protected LocationClient _mLocationClient;
    protected boolean needSettingGuide;

    /**
     * 获取百度定位
     *
     * @return
     */
    public static BDLocation getBdLocationLast() {
        return _bdLocationLast;
    }

    /**
     * 获取定位数据，如果曾定位成功过，曾返回缓存数据，否则发起实时定位
     *
     * @param needSettingGuide
     * @param callBack
     */
    public static void startLocation(boolean needSettingGuide, HikLocationManagerCallBack callBack) {
        if (_bdLocationLast != null) {
            if (callBack != null) {
                callBack.onLocationGet(_bdLocationLast);
            }
            return;
        }
        startLocationOnline(needSettingGuide, callBack);
    }

    /**
     * 发起实时定位
     *
     * @param needSettingGuide 如果用户未同意权限，或设备未打开定位权限时，是否需要弹框提示引导用户前往系统设置？
     *                         若传true，则自行在onAndroidResult中截获requestCode为BMCContext.REQUEST_CODE_CHECK_LOCATION_SERVICE（前往系统设置后）的回调
     *                         并重新调用此方法发起定位
     * @param callBack
     */
    public static void startLocationOnline(boolean needSettingGuide, HikLocationManagerCallBack callBack) {
        LocationManager locationManager = new LocationManager(needSettingGuide, callBack);
        locationManager.startLocationOnline();
    }

    private LocationManager(boolean needSettingGuide, HikLocationManagerCallBack callBack) {
        this.needSettingGuide = needSettingGuide;
        this.mCallBack = callBack;
        _mLocationClient = new LocationClient(TinkerApplicationCreate.getApplication()); // 声明LocationClient类
        _locationListener = new HikLocationListener(this);
        _mLocationClient.registerLocationListener(_locationListener); // 注册监听函数
        _mLocationClient.setLocOption(getLocationOption());
    }

    /**
     * 检查权限
     */
    protected void startLocationOnline() {
        ActivitySupportWrapper.requestRunPermission(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionRequestListener() {
            @Override
            public void onGranted() {
                checkLocationService();
            }

            @Override
            public void onDenied(Activity activity, List<String> deniedPermission) {
                if (needSettingGuide) {
                    super.onDenied(activity, deniedPermission);
                }
                onResult(null);
            }
        });
    }

    /**
     * 检查定位服务是否开启
     */
    protected void checkLocationService() {
        CheckServiceCallBack checkServiceCallBack = new CheckServiceCallBack() {
            @Override
            public void pass() {
                if (_mLocationClient != null) {
                    _mLocationClient.start();
                }
            }

            @Override
            public void fail(Activity activity) {
                if (needSettingGuide && activity != null) {
                    super.fail(activity);
                }
                onResult(null);
            }
        };
        android.location.LocationManager locationService = (android.location.LocationManager) TinkerApplicationCreate.getApplication().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsLocateOpen = locationService.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);//GPS定位
        boolean networkLocateOpen = locationService.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);//网络定位
        if (gpsLocateOpen || networkLocateOpen) {//定位服务：开
            checkServiceCallBack.pass();
        } else {//定位服务：关
            checkServiceCallBack.fail(TinkerApplicationCreate.getInstance().getActivityTop());
        }
    }

    /**
     * 定位成功，返回结果给业务方
     *
     * @param bdLocation or null
     */
    protected void onResult(BDLocation bdLocation) {
        LogCat.i("Location success. bdLocation:" + bdLocation);
        if (_mLocationClient != null) {
            _mLocationClient.unRegisterLocationListener(_locationListener);
            _mLocationClient.stop();
            _mLocationClient = null;
        }

        _bdLocationLast = bdLocation;
        if (mCallBack != null) {
            mCallBack.onLocationGet(bdLocation);
        }
    }

    /**
     * 获取定位参数
     *
     * @return
     */
    public LocationClientOption getLocationOption() {
        LocationClientOption option = new LocationClientOption();
        //设置定位模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 打开gps
        option.setOpenGps(true);
        // 设置坐标类型
        option.setCoorType(GvConfig.MAP_COORD_TYPE);//统一成国标（火星坐标）
        //发起定位请求的间隔，int类型，单位ms
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        return option;
    }

    /**
     * 定位成功后百度提供的回调
     */
    class HikLocationListener extends BDAbstractLocationListener {
        private LocationManager manager;

        public HikLocationListener(@NonNull LocationManager manager) {
            this.manager = manager;
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            manager.onResult(bdLocation);
        }
    }

    /**
     * 定位完成后业务方使用的回调
     */
    public interface HikLocationManagerCallBack {
        /**
         * @param bdLocation or null
         */
        void onLocationGet(BDLocation bdLocation);
    }

    /**
     * 检查定位服务的回调
     */
    public abstract class CheckServiceCallBack {

        public abstract void pass();

        public void fail(Activity activity) {
            UI.showConfirmDialog(activity, "定位获取失败，请在权限管理中检查定位权限", "以后再说", null, "去开启", (View v) -> {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, GvContext.REQUEST_CODE_CHECK_LOCATION_SERVICE);
            });
        }
    }
}
