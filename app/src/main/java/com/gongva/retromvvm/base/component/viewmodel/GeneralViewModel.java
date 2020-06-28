package com.gongva.retromvvm.base.component.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.gongva.library.app.ui.view.databindadapter.IClearViewModel;
import com.gongva.retromvvm.common.GvContext;
import com.gongva.retromvvm.common.view.listview.AppSmartRefreshLayout;

/**
 * 通用view model
 *
 * @data 2019/3/8
 */
public class GeneralViewModel extends ARefreshViewModel implements IClearViewModel {

    private final MutableLiveData<ExceptionTip> exceptionTipLD = new MutableLiveData<>();
    private final MutableLiveData<ViewModelOperateEnum> operateEnumLD = new MutableLiveData<>();

    private final ObservableBoolean refreshLoadingOB;
    private final ObservableInt moreLoadStateOB;

    private ClearGeneralModelCallback clearGeneralModelCallback;

    public GeneralViewModel(@NonNull Application application) {
        super(application);

        refreshLoadingOB = new ObservableBoolean();
        moreLoadStateOB = new ObservableInt();
    }

    public MutableLiveData<ViewModelOperateEnum> getOperateEnumLD() {
        return operateEnumLD;
    }

    public MutableLiveData<ExceptionTip> getExceptionTipLD() {
        return exceptionTipLD;
    }

    public ObservableBoolean getRefreshLoadingOB() {
        return refreshLoadingOB;
    }

    public ObservableInt getMoreLoadStateOB() {
        return moreLoadStateOB;
    }

    public ClearGeneralModelCallback getClearGeneralModelCallback() {
        return clearGeneralModelCallback;
    }

    public void setClearGeneralModelCallback(ClearGeneralModelCallback clearGeneralModelCallback) {
        this.clearGeneralModelCallback = clearGeneralModelCallback;
    }

    public void showExceptionTip(String msg) {
        exceptionTipLD.setValue(new ExceptionTip(GvContext.SUCCESS_CODE, msg));
    }

    public void showExceptionTip(int code, String msg) {
        exceptionTipLD.setValue(new ExceptionTip(code, msg));
    }

    public void showExceptionTip(int code, String msg, String msgIcon) {
        exceptionTipLD.setValue(new ExceptionTip(code, msg, msgIcon));
    }

    public void showExceptionTip(String msg, int tag) {
        exceptionTipLD.setValue(new ExceptionTip(GvContext.SUCCESS_CODE, msg, tag));
    }

    public void showExceptionTip(int code, String msg, int tag) {
        exceptionTipLD.setValue(new ExceptionTip(code, msg, tag));
    }

    public void showExceptionTip(int code, String msg, int tag, String msgIcon) {
        exceptionTipLD.setValue(new ExceptionTip(code, msg, tag, msgIcon));
    }

    private void triggerAction(ViewModelOperateEnum operate) {
        operateEnumLD.setValue(operate);
    }

    public void hideSoftKeyboard() {
        triggerAction(ViewModelOperateEnum.HIDE_SOFT_KEYBOARD);
    }

    public void finishCurrentActivity() {
        triggerAction(ViewModelOperateEnum.FINISH_CURRENT_ACTIVITY);
    }

    public void stopAllLoading(boolean hasExtraPage) {
        stopLoading();
        refreshLoadingOB.set(false);
        if (hasExtraPage) {
            moreLoadStateOB.set(AppSmartRefreshLayout.LOAD_MORE_FINISH);
        } else {
            moreLoadStateOB.set(AppSmartRefreshLayout.LOAD_MORE_NO_MORE_DATA);
        }
    }

    @Override
    public void refresh() {
        refreshLoadingOB.set(true);
        moreLoadStateOB.set(AppSmartRefreshLayout.LOAD_MORE_FINISH);
        if (getaRefreshViewModelCallback() != null) {
            getaRefreshViewModelCallback().onRefreshCallback();
        }
    }

    @Override
    public void loadMore() {
        moreLoadStateOB.set(AppSmartRefreshLayout.LOAD_MORE_ING);
        if (getaLoadMoreViewModelCallback() != null) {
            getaLoadMoreViewModelCallback().onLoadMoreCallback();
        }
    }

    @Override
    public void clear() {
        if (clearGeneralModelCallback != null) {
            clearGeneralModelCallback.onClearCallback();
        }
    }

    public static class ExceptionTip {

        private int code;
        private String tipMsg;
        private String tipMsgIconUrl;
        private int tag;

        public ExceptionTip(int code, String tipMsg) {
            this.code = code;
            this.tipMsg = tipMsg;
        }

        public ExceptionTip(int code, String tipMsg, String tipMsgIconUrl) {
            this.code = code;
            this.tipMsg = tipMsg;
            this.tipMsgIconUrl = tipMsgIconUrl;
        }

        public ExceptionTip(int code, String tipMsg, int tag) {
            this.code = code;
            this.tipMsg = tipMsg;
            this.tag = tag;
        }

        public ExceptionTip(int code, String tipMsg, int tag, String tipMsgIconUrl) {
            this.code = code;
            this.tipMsg = tipMsg;
            this.tag = tag;
            this.tipMsgIconUrl = tipMsgIconUrl;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getTipMsg() {
            return tipMsg;
        }

        public void setTipMsg(String tipMsg) {
            this.tipMsg = tipMsg;
        }

        public String getTipMsgIconUrl() {
            return tipMsgIconUrl;
        }

        public void setTipMsgIconUrl(String tipMsgIconUrl) {
            this.tipMsgIconUrl = tipMsgIconUrl;
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            return "ExceptionTip{" +
                    "code=" + code +
                    ", tipMsg='" + tipMsg + '\'' +
                    ", tipMsgIcon='" + tipMsgIconUrl + '\'' +
                    ", tag=" + tag +
                    '}';
        }
    }

    public interface ClearGeneralModelCallback {
        void onClearCallback();
    }
}
