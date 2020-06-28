package com.gongva.library.plugin.netbase.linstener;

/**
 * 用于数据清理
 *
 * @author
 * @data 2019/3/18
 * @email
 */
public interface IDataSourceDataClear {
    /**
     * 用于数据源数据清理指定数据
     *
     * @param tag tag对应数据
     */
    void clear(int tag);

    /**
     * 用于数据源清理所有数据
     */
    void clearAll();
}
