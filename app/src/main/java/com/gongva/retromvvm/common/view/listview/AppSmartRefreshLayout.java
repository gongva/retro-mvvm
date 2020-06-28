package com.gongva.retromvvm.common.view.listview;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * SmartRefreshLayout 扩展库
 *
 * @data 2019/3/12
 */
public class AppSmartRefreshLayout extends SmartRefreshLayout {

    public final static int LOAD_MORE_ING = 1;
    public final static int LOAD_MORE_FINISH = 2;
    public final static int LOAD_MORE_NO_MORE_DATA = 3;

    public AppSmartRefreshLayout(Context context) {
        super(context);
    }

    public AppSmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppSmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            autoRefresh();//自动刷新
        } else {
            finishRefresh();//结束刷新
        }
    }

    public void setMoreWithNoMoreDataLoading(boolean loadMoreWithNoMoreData) {
        if (loadMoreWithNoMoreData) {
            finishLoadMore();//自动加载
        } else {
            finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
        }
    }

    public void setMoreLoadState(int state) {
        switch (state) {
            case LOAD_MORE_ING:
                autoLoadMore();//自动加载
                break;
            case LOAD_MORE_FINISH:
                finishLoadMore();//结束加载
                break;
            case LOAD_MORE_NO_MORE_DATA:
                finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                break;
        }
    }
}
