package com.hik.core.android.view.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 多类型布局的列表适配器
 *
 * @author gongwei
 * @time 2020/03/11
 * @mail shmily__vivi@163.com
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T> {
    public SimpleAdapter(Context context) {
        super(context);
    }

    public SimpleAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.newView(this.mContext, parent, position);
        }

        this.bindView(this.mContext, convertView, position);
        return convertView;
    }

    public abstract View newView(Context var1, ViewGroup var2, int var3);

    public abstract void bindView(Context var1, View var2, int var3);
}
