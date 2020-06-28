package com.gongva.library.app.base;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.config.AppContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity基类
 *
 * @author gongwei
 * @time 2019/11/06
 * @mail shmily__vivi@163.com
 */
public class ActivitySupport extends AppCompatActivity {

    private static IPermissionRequestListener mPermissionRequestListener;//6.0以上动态授权回调
    private AudioManager audioManager;//设置应用为媒体音量
    private LocalBroadcastManager mBroadcastManager;//用户主动调用exitApp时关闭所有页面
    private BroadcastReceiver exitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        super.onCreate(savedInstanceState);
        //用于接收来自Intent的参数
        Intent intent = getIntent();
        if (intent != null) {
            initIntentData(intent);
        }
        //标题栏文字默认黑色，因为主题色是白色
        setStatusIconDark(true);
        //设置默认横竖屏
        setRequestedOrientation(getOrientation());
        //监听是否关闭App
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastManager.registerReceiver(exitReceiver, new IntentFilter(getPackageName()));
        //设置媒体音量
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            initIntentData(intent);
        }
    }

    /**
     * 请求动态权限
     *
     * @param permissions
     * @param listener
     */
    public static void requestRunPermission(String[] permissions, IPermissionRequestListener listener) {
        final Activity topActivity = TinkerApplicationCreate.getInstance().getActivityTop();
        if (topActivity == null) {
            return;
        }
        requestRunPermission(topActivity, permissions, listener);
    }

    public static void requestRunPermission(Activity activity, String[] permissions, IPermissionRequestListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> deniedPermissions = new ArrayList<>();//存放用户未授权的权限
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }

            if (deniedPermissions.isEmpty()) {
                //已授权
                if (listener != null) {
                    listener.onGranted();
                }
            } else {
                //未授权 or 未全部授权
                mPermissionRequestListener = listener;
                ActivityCompat.requestPermissions(activity, deniedPermissions.toArray(new String[deniedPermissions.size()]), AppContext.REQUEST_CODE_PERMISSION);
            }
        } else if (listener != null) {
            listener.onGranted();
        }
    }

    /**
     * 动态权限请求系统回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppContext.REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0) {
                    //存放没授权的权限
                    StringBuffer stringBuffer = new StringBuffer();
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                            stringBuffer.append(permission);
                            stringBuffer.append(" ");
                        }
                    }
                    if (mPermissionRequestListener != null) {
                        if (deniedPermissions.isEmpty()) {
                            //用户全部同意授权
                            mPermissionRequestListener.onGranted();
                        } else {
                            //返回用户拒绝的授权
                            //BMCUI.showToast(String.format("您拒绝了权限：%s", stringBuffer.toString()));
                            mPermissionRequestListener.onDenied(this, deniedPermissions);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断当前activity是否在顶部
     *
     * @return
     */
    public boolean isTopActivity() {
        Activity topActivity = TinkerApplicationCreate.getInstance().getActivityTop();
        return topActivity == this;
    }

    /**
     * 设置沉浸式状态栏，4.4以上生效
     */
    protected void setStatusBarTransparent(boolean trans) {
        if (trans) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 5.0+ 实现
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.setStatusBarColor(Color.TRANSPARENT);
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //window.setStatusBarColor(Color.parseColor("#00000000"));
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 4.4 实现
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    /**
     * 设置状态栏文字和标图的颜色,6.0以上生效
     *
     * @param dark
     */
    protected void setStatusIconDark(boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView == null) return;

            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(Color.parseColor("#000000"));
            }
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(color));
        }
    }

    protected int getOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    //重写字体缩放比例 api<25
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Configuration config = res.getConfiguration();
            config.fontScale = 1;//1 设置正常字体大小的倍数
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return res;
    }

    //重写字体缩放比例  api>25
    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            final Resources res = newBase.getResources();
            final Configuration config = res.getConfiguration();
            config.fontScale = 1;//1 设置正常字体大小的倍数
            final Context newContext = newBase.createConfigurationContext(config);
            super.attachBaseContext(newContext);
        } else {
            super.attachBaseContext(newBase);
        }
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        mBroadcastManager.sendBroadcast(new Intent(getPackageName()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBroadcastManager.unregisterReceiver(exitReceiver);
    }

    protected void initIntentData(Intent intent) {

    }
}
