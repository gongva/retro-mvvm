package com.gongva.library.app.ui.view.databindadapter;

/**
 * @author gongwei
 * @data 2019/3/8
 * @email shmily__vivi@163.com
 */
public interface IRefreshViewModel {
    // 用于在ViewModel刷新数据
    void refresh();
    // 用于在ViewModel加载更多数据
    void loadMore();
}
