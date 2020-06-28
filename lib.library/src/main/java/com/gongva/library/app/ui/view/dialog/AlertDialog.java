package com.gongva.library.app.ui.view.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hik.core.android.view.ViewUtil;
import com.gongva.library.R;
import com.gongva.library.app.TinkerApplicationCreate;

public abstract class AlertDialog {

    private Context context;

    private TouchableDialog dialog;

    //views
    protected TextView tvTitle;
    protected View vTitleLine;
    protected TextView tvMsg;
    protected TextView tvNegButton;
    protected TextView tvPosButton;
    protected View vButtonLine;
    protected LinearLayout lltExtendContainer;
    protected RelativeLayout rltButtons;

    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean showView = false;

    public static AlertDialog newInstance(Context context, boolean cancelAble, String title, String message, int messageGravity, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        return new AlertDialog(context, cancelAble, title, message, messageGravity, buttonLeft, listenerLeft, buttonRight, listenerRight){

            @Override
            protected int getContentLayout() {
                return R.layout.dialog_confirm_dialog;
            }
        };
    }

    public AlertDialog(Context context, boolean cancelAble, String title, String message, int messageGravity, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        this.context = context;
        builder().setMsg(message, messageGravity);

        if (!cancelAble) {
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }
        if (!TextUtils.isEmpty(buttonLeft)) {
            setPositiveButton(buttonLeft, listenerLeft);
        }
        if (!TextUtils.isEmpty(buttonRight)) {
            setNegativeButton(buttonRight, listenerRight);
        }
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        checkPosAndNegBackground();
        initExtends();
    }

    /**
     * 初始化过程中留给子类做扩展用的方法
     * 需要扩展时请Override
     */
    protected void initExtends() {

    }

    protected View createView(Context context) {
        return LayoutInflater.from(context).inflate(getContentLayout(), null);
    }

    //请基于dialog_confirm_dialog.xml扩展布局，允许新增，不允许修改类型与ID
    protected abstract int getContentLayout();

    public AlertDialog builder() {
        // 获取Dialog布局
        View view = createView(context);
        initView(view);
        // 定义Dialog布局和参数
        dialog = new TouchableDialog(context, R.style.AlertDialogStyle);
        if (context == TinkerApplicationCreate.getApplication()) {
            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
            dialog.getWindow().setType(LAYOUT_FLAG);
        }
        dialog.setContentView(view);
        return this;
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    protected void initView(View view) {
        // 获取自定义Dialog布局中的控件
        tvTitle = view.findViewById(R.id.txt_title);
        tvTitle.setVisibility(View.GONE);
        vTitleLine = view.findViewById(R.id.txt_title_line);
        vTitleLine.setVisibility(View.GONE);
        tvMsg = view.findViewById(R.id.txt_msg);
        tvMsg.setLineSpacing(ViewUtil.dip2px(this.context, 4), 1);
        tvMsg.setVisibility(View.GONE);
        rltButtons = view.findViewById(R.id.btn_layout);
        tvNegButton = view.findViewById(R.id.btn_neg);
        tvNegButton.setVisibility(View.GONE);
        tvPosButton = view.findViewById(R.id.btn_pos);
        tvPosButton.setVisibility(View.GONE);
        vButtonLine = view.findViewById(R.id.img_line);
        vButtonLine.setVisibility(View.GONE);

        lltExtendContainer = view.findViewById(R.id.add_view);
        lltExtendContainer.setVisibility(View.GONE);
    }

    public AlertDialog setTitle(String title) {
        showTitle = true;
        tvTitle.setText(title);
        vTitleLine.setVisibility(View.VISIBLE);
        return this;
    }

    public AlertDialog setMsg(String msg, int gravity) {
        showMsg = true;
        tvMsg.setText(msg);
        tvMsg.setGravity(gravity);
        return this;
    }

    public AlertDialog addView(View view) {
        showView = true;
        if (view == null) {
            view = new TextView(context);
            lltExtendContainer.addView(view);
        } else {
            lltExtendContainer.addView(view);
        }
        return this;
    }

    public AlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public AlertDialog setPositiveButton(String text, final View.OnClickListener listener) {
        showPosBtn = true;
        if (TextUtils.isEmpty(text)) {
            tvPosButton.setText("取消");
        } else {
            tvPosButton.setText(text);
        }
        tvPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(String text, final View.OnClickListener listener) {
        showNegBtn = true;
        if (TextUtils.isEmpty(text)) {
            tvNegButton.setText("确定");
        } else {
            tvNegButton.setText(text);
        }
        tvNegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public void checkPosAndNegBackground() {
        if (showPosBtn && showNegBtn) {
            Drawable btnLeft = context.getResources().getDrawable(R.drawable.selector_alert_button_left);
            Drawable btnRight = context.getResources().getDrawable(R.drawable.selector_alert_button_right);
            tvPosButton.setBackground(btnLeft);
            tvNegButton.setBackground(btnRight);
        } else {
            Drawable btnSingle = context.getResources().getDrawable(R.drawable.selector_alert_button_single);
            tvPosButton.setBackground(btnSingle);
            tvNegButton.setBackground(btnSingle);
        }
    }

    public void cancelDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    //返回显示Message的TextView，有时候会有需要设置一下Gravity居中or居左的需求
    public TextView getMessageView() {
        return tvMsg;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            tvTitle.setText("提示");
            tvTitle.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        if (showMsg) {
            tvMsg.setVisibility(View.VISIBLE);
        }

        if (showView) {
            lltExtendContainer.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            rltButtons.setVisibility(View.GONE);
        } else {
            rltButtons.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && showNegBtn) {
            tvPosButton.setVisibility(View.VISIBLE);
            tvNegButton.setVisibility(View.VISIBLE);
            vButtonLine.setVisibility(View.VISIBLE);
        } else {
            vButtonLine.setVisibility(View.GONE);
        }

        if (showPosBtn && !showNegBtn) {
            tvPosButton.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && showNegBtn) {
            tvNegButton.setVisibility(View.VISIBLE);
        }
    }

    public AlertDialog show() {
        if (dialog != null) {
            setLayout();
            dialog.show();
        }
        return this;
    }
}
