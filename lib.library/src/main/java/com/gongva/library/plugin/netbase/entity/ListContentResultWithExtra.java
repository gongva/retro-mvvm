package com.gongva.library.plugin.netbase.entity;

/**
 * 带有额外数据的，分页接口
 *
 * @author yslei
 * @data 2019/3/11
 * @email leiyongsheng@hikcreate.com
 */
public class ListContentResultWithExtra<T, D> extends ListContentResult<T> {
    private D extra;

    public D getExtra() {
        return extra;
    }

    public void setExtra(D extra) {
        this.extra = extra;
    }

    @Override
    public boolean isEmpty() {
        if (extra != null && super.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ListContentResultWithExtra{" +
                "data=" + super.toString() +
                "extra=" + extra +
                '}';
    }
}
