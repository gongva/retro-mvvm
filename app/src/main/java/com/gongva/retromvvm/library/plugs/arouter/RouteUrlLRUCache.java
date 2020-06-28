package com.gongva.retromvvm.library.plugs.arouter;

import java.util.LinkedHashMap;

/**
 * 用于记录最近跳转的n个地址，Key:routeUrl，Value:timestamp
 *
 * @author gongwei
 * @date 2019/6/4
 * @mail gongwei5@hikcreate.com
 */
public class RouteUrlLRUCache extends LinkedHashMap<String, Long> {

    //n=10
    public static final int MAX_ELEMENTS = 10;

    public RouteUrlLRUCache() {
        super(MAX_ELEMENTS, 0.75F, true);
    }

    protected boolean removeEldestEntry(Entry eldest) {
        return size() > MAX_ELEMENTS;
    }
}
