package com.gongva.retromvvm.ui.common.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.KeyEvent;

import com.gongva.library.app.ui.web.X5WebViewClientCallBack;
import com.gongva.retromvvm.R;
import com.gongva.retromvvm.base.component.BaseActivity;
import com.gongva.retromvvm.databinding.ActivityX5WebBinding;
import com.hik.core.android.api.LogCat;
import com.tencent.smtt.sdk.WebView;

/**
 * X5内核WebActivity
 *
 * @author gongwei
 * @time 2020/03/24
 * @mail shmily__vivi@163.com
 */
public class AppX5WebActivity extends BaseActivity<ActivityX5WebBinding> {

    String mContentUrl;// 网页地址
    /**
     * WebViewClientCallBack for WebView's LifeCycle
     */
    private X5WebViewClientCallBack mX5WebViewClientCallBack = new X5WebViewClientCallBack() {
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
                if (mBinding.x5Webview != null) {
                    setTitle(mBinding.x5Webview.getTitle());
                }
            }
            super.updateProgress(progress);
        }
    };


    public static void start(Context context, String url) {
        Intent intent = new Intent(context, AppX5WebActivity.class);
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
        return R.layout.activity_x5_web;
    }

    @Override
    protected void initView() {
        //避免网页中的视频，上屏幕的时候，可能出现闪烁的情况
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //初始化View
        mBinding.x5Webview.setProgressBar(mBinding.progressWeb);
        mBinding.x5Webview.setWebViewClientCallBack(mX5WebViewClientCallBack);
    }

    @Override
    protected void initData() {
        LogCat.i("Load url : " + mContentUrl);
        mBinding.x5Webview.loadUrl(mContentUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.x5Webview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.x5Webview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding.x5Webview != null) {
            mBinding.x5Webview.loadUrl("javascript:pause()");
            mBinding.x5Webview.destroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //文件上传处理
        mBinding.x5Webview.onImageChooserCallBack(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.x5Webview.canGoBack()) {
            mBinding.x5Webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}