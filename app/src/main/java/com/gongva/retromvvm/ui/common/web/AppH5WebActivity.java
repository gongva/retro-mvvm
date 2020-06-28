package com.gongva.retromvvm.ui.common.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.gongva.library.app.ui.web.H5WebViewClientCallBack;
import com.gongva.retromvvm.R;
import com.gongva.retromvvm.base.component.BaseActivity;
import com.gongva.retromvvm.databinding.ActivityH5WebBinding;
import com.hik.core.android.api.LogCat;

/**
 * 非X5内核WebActivity
 *
 * @author gongwei
 * @time 2020/03/24
 * @mail shmily__vivi@163.com
 */
@Deprecated //尽量使用X5WebActivity，某个版本以后webkit内核的WebView将不再维护
public class AppH5WebActivity extends BaseActivity<ActivityH5WebBinding> {

    String mContentUrl;// 网页地址
    /**
     * WebViewClientCallBack for WebView's LifeCycle
     */
    private H5WebViewClientCallBack mH5WebViewClientCallBack = new H5WebViewClientCallBack() {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (errorCode == -2 || errorCode == -6) {
                showErrorPageForHttp(errorCode, "糟糕，加载失败了");
                setTitle("");
            }
        }

        @Override
        public void updateProgress(int progress) {
            if (progress == 100) {
                if (mBinding.h5Webview != null) {
                    setTitle(mBinding.h5Webview.getTitle());
                }
            }
            super.updateProgress(progress);
        }
    };


    public static void start(Context context, String url) {
        Intent intent = new Intent(context, AppH5WebActivity.class);
        intent.putExtra("mContentUrl", url);
        context.startActivity(intent);
    }

    @Override
    protected void initIntentData(Intent intent) {
        super.initIntentData(intent);
        mContentUrl = intent.getStringExtra("mContentUrl");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_h5_web;
    }

    @Override
    protected void initView() {
        //避免网页中的视频，上屏幕的时候，可能出现闪烁的情况
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //初始化View
        mBinding.h5Webview.setProgressBar(mBinding.progressWeb);
        mBinding.h5Webview.setH5WebViewClientCallBack(mH5WebViewClientCallBack);
    }

    @Override
    protected void initData() {
        LogCat.i("Load url : " + mContentUrl);
        mBinding.h5Webview.loadUrl(mContentUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.h5Webview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.h5Webview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding.h5Webview != null) {
            mBinding.h5Webview.loadUrl("javascript:pause()");
            mBinding.h5Webview.destroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //文件上传处理
        mBinding.h5Webview.onImageChooserCallBack(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.h5Webview.canGoBack()) {
            mBinding.h5Webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}