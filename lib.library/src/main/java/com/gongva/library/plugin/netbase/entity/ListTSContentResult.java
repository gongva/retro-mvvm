package com.gongva.library.plugin.netbase.entity;

import java.util.List;

/**
 * 时间搓数据分页加载
 *
 * @author
 * @data 2019/3/11
 * @email
 */
public class ListTSContentResult<T> extends ABaseResult<T> {
    private long timestamp;
    private int totalCount;
    private int size;
    private List<T> list;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean hasNextPage() {
        return list != null && list.size() == size;
    }

    @Override
    public boolean isEmpty() {
        if (totalCount == 0 || timestamp <= 0 || list == null || list.isEmpty()) {
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
        return "ListTSContentResult{" +
                "timestamp=" + timestamp +
                ", totalCount=" + totalCount +
                ", size=" + size +
                ", list=" + list +
                '}';
    }
}
