package com.gongva.library.app.ui.view.customview.errorpage;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongva.library.R;
import com.gongva.library.app.base.ILoadingErrorController;
import com.gongva.library.plugin.glide.AImage;

/**
 * 错误页，空白页
 * 子类请重新getContentLayout方法定制显示UI
 *
 * @author gongwei 2018/12/18
 */
public class ErrorPage extends FrameLayout implements ILoadingErrorController {

    private Context context;

    //view
    private TextView tvMessage;
    private ImageView ivIcon;
    private TextView tvAction;

    public ErrorPage(Context context) {
        this(context, null);
    }

    public ErrorPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        LayoutInflater inf = LayoutInflater.from(context);
        View v = inf.inflate(getContentLayout(), this);
        tvMessage = v.findViewById(R.id.tv_error_page_message);
        ivIcon = v.findViewById(R.id.iv_error_page_icon);
        tvAction = v.findViewById(R.id.tv_error_page_action);
    }

    protected int getContentLayout() {
        return R.layout.view_error_page;
    }

    @Override
    public void showErrorPage() {
        showErrorPage(null);
    }

    @Override
    public void showErrorPage(String message) {
        showErrorPage(0, message);
    }

    @Override
    public void showErrorPage(int icResource, String message) {
        showErrorPage(icResource, message, null, null);
    }

    @Override
    public void showErrorPage(String icResource, String message) {
        showErrorPage(icResource, message, null, null);
    }

    @Override
    public void showErrorPage(String message, String action, OnClickListener listener) {
        showErrorPage(0, message, action, listener);
    }

    @Override
    public void showErrorPageForHttp(int statusCode, String responseString) {
        if (statusCode == 0 || (statusCode >= 400 && statusCode < 600)) {
            showErrorPage(R.drawable.ic_error_no_net, "网络连接有问题");
        } else {
            showErrorPage(R.drawable.ic_error_no_net, responseString);
        }
    }

    @Override
    public void showErrorPage(int icResource, String message, String action, OnClickListener actionListener) {
        if (icResource > 0) {
            ivIcon.setImageResource(icResource);
        }
        showErrorPageContent(message, action, actionListener);
    }

    @Override
    public void showErrorPage(String icResource, String message, String action, OnClickListener actionListener) {
        if (!TextUtils.isEmpty(icResource)) {
            AImage.load(icResource, ivIcon, R.drawable.ic_error_no_net);
        }
        showErrorPageContent(message, action, actionListener);
    }

    private void showErrorPageContent(String message, String action, OnClickListener actionListener) {
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        }
        if (!TextUtils.isEmpty(action)) {
            tvAction.setVisibility(VISIBLE);
            tvAction.setText(action);
            tvAction.setOnClickListener(actionListener);
        } else {
            tvAction.setVisibility(GONE);
        }
        this.setVisibility(VISIBLE);
    }

    @Override
    public void dismissErrorPage() {

    }

    public void dismiss() {
        this.setVisibility(GONE);
    }
}