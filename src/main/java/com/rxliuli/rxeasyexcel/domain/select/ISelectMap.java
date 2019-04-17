package com.rxliuli.rxeasyexcel.domain.select;

import java.util.Map;

/**
 * Select 下拉框接口
 * 子类实现 {@link #getMap()} 函数返回下拉框的键值映射
 *
 * @author rxliuli
 */
public interface ISelectMap<ColumnType> {
    /**
     * 获取到映射表
     *
     * @return 下拉框键值映射表
     */
    Map<ColumnType, String> getMap();
}
