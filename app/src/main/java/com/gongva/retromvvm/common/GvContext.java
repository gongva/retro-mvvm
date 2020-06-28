package com.gongva.retromvvm.common;

import com.gongva.retromvvm.BuildConfig;
import com.gongva.library.app.config.AppContext;
import com.hik.core.android.api.io.SharePreferencesUtil;

/**
 * 接口地址定义
 *
 * @author gongwei 2018/12/18
 */
public abstract class GvContext extends AppContext {
    public static boolean hasLog = BuildConfig.SHOW_LOG;//是否开启日志，配置见build.gradle中SHOW_LOG参数配置
    public static boolean serverSwitchEnabled = BuildConfig.SERVER_SWITCH_ENABLE; //是否启用服务器地址选择的菜单，配置见build.gradle中SERVER_ADDRESS_ENABLE参数配置

    public static final String[] SERVICE_ADDRESS = new String[]{"开发", "测试", "正式"};//第一个为开发时的默认地址，最后一个为上线地址，中间随意

    //SSL证书校验
    public static final String[] SSL_CERTIFICATE_FILE = {"gongva.cer"};//https证书，存放于app\src\main\assets中 todo company's cert
    public static boolean openSslCheck = false; //SSL证书校验开关
    public static String BASE_URL_HTTP;//Api接口前缀
    public static String APP_LOGO;//Logo地址 //todo AppLogo地址

    static {
        int serverAddressType = getServerAddressType();
        switch (serverAddressType) {
            case 0://开发 todo Api域名配置：开发
                openSslCheck = false;
                GvContext.BASE_URL_HTTP = "http://devbmcapp.gongva.com";
                break;
            case 2://测试 todo Api域名配置：测试
                openSslCheck = false;
                GvContext.BASE_URL_HTTP = "http://testbmcapp.gongva.com";
                break;
            case 5://正式 todo Api域名配置：正式
                openSslCheck = true;
                GvContext.BASE_URL_HTTP = "https://bmcappn.gongva.com/";
                break;
        }
    }

    private static int getServerVersionType() {
        if (serverSwitchEnabled) {
            return SharePreferencesUtil.getInt(SPF_SERVER_ADDRESS_TYPE, 0);
        } else {
            return SERVICE_ADDRESS.length - 1;
        }
    }

    public static int getServerAddressType() {
        int serverAddressType = getServerVersionType();
        setServerAddressType(serverAddressType);
        return serverAddressType;
    }

    public static void setServerAddressType(int serverAddressType) {
        SharePreferencesUtil.setInt(SPF_SERVER_ADDRESS_TYPE, serverAddressType, true);
    }

    /************SharedPreferences key***********/
    public static final String SPF_SERVER_ADDRESS_TYPE = "spf_server_address_type";//是否允许切换环境（是否为debug版）
    public static final String SPF_LOGIN_USER = "spf_login_user";//登录用户缓存
    public static final String SPF_LOGIN_LATEST_USER = "spf_login_latest_user";//最后一次登录的用户缓存
    public static final String SPF_SYSTEM_CONFIG = "spf_system_config";//系统配置

    /*Request Codes*/
    public static final int REQUEST_CODE_CHECK_LOCATION_SERVICE = 1001;//检查定位服务
    public static final int REQUEST_CODE_O_PERMISSION = 1002;//8.0安全授权申请的
    public static final int REQUEST_CODE_SELECTED_IMAGES = 1003;//选择（单张/多张）图片
    public static final int REQUEST_CODE_SELECTED_IMAGES_EDIT = 1004;//已选图片列表的编辑
    public static final int REQUEST_CODE_OPEN_CAMERA = 1005;//打开相机
    public static final int REQUEST_CODE_IMAGE_CROP = 1006;//图片裁剪


    /*Api Codes*/
    public static final int SUCCESS_CODE = 200;//接口请求成功Code
    public static final int ERROR_CODE_LOGOFF = 100100;//账号被挤下线
    public static final int ERROR_CODE_LOGIN_ERROR = 100101;//登录失效
}
