package com.gongva.library.app.ui.view.recycleview;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @data 2019/3/8
 */
public class RecycleViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;
    private View itemView;
    private int viewTag;

    public RecycleViewHolder(View itemView) {
        super(itemView);
        setItemView(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }

    public View getItemView() {
        return itemView;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }

    public int getViewTag() {
        return viewTag;
    }

    public void setViewTag(int viewTag) {
        this.viewTag = viewTag;
    }
}
