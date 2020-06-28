package com.hik.core.android.view.widget;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author gongwei
 * @time 2020/03/11
 * @mail gongwei5@hikcreate.com
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context mContext;
    protected List<T> mData;

    public BaseAdapter(Context context) {
        this(context, new ArrayList());
    }

    public BaseAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    public Context getContext() {
        return this.mContext;
    }

    public List<T> getData() {
        return this.mData;
    }

    public void setData(Collection<? extends T> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }

        this.notifyDataSetChanged();
    }

    public void setData(T... data) {
        this.mData.clear();
        if (data != null) {
            Collections.addAll(this.mData, data);
        }

        this.notifyDataSetChanged();
    }

    public void add(T object) {
        this.mData.add(object);
        this.notifyDataSetChanged();
    }

    public void add(int index, T object) {
        this.mData.add(index, object);
        this.notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection != null) {
            this.mData.addAll(collection);
        }

        this.notifyDataSetChanged();
    }

    public void addAll(int index, Collection<? extends T> collection) {
        if (collection != null) {
            this.mData.addAll(index, collection);
        }

        this.notifyDataSetChanged();
    }

    public void addAll(T... items) {
        if (items != null) {
            Collections.addAll(this.mData, items);
        }

        this.notifyDataSetChanged();
    }

    public void insert(T object, int index) {
        this.mData.add(index, object);
        this.notifyDataSetChanged();
    }

    public void remove(T object) {
        this.mData.remove(object);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.mData.clear();
        this.notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        Collections.sort(this.mData, comparator);
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.mData.size();
    }

    public T getItem(int position) {
        return this.mData.get(position);
    }

    public int getPosition(T item) {
        return this.mData.indexOf(item);
    }

    public long getItemId(int position) {
        return (long)position;
    }
}