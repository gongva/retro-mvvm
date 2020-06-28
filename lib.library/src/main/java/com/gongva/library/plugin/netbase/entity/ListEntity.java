package com.gongva.library.plugin.netbase.entity;

import java.util.List;

/**
 * 只有一个list的Object
 *
 * @author gongwei
 * @date 2019/3/29
 * @mail shmily__vivi@163.com
 */
public class ListEntity<T> extends ABaseResult {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public boolean isEmpty() {
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (T data : list) {
            if (!isDataEmpty(data)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ListEntity{" +
                "list=" + list +
                '}';
    }
}
