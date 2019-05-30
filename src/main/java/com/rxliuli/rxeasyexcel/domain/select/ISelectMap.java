package com.rxliuli.rxeasyexcel.domain.select;

import com.rxliuli.rxeasyexcel.domain.base.BaseMap;

/**
 * Select 下拉框接口
 * 子类实现 {@link #getMap()} 函数返回下拉框的键值映射
 *
 * @author rxliuli
 */
public interface ISelectMap<ColumnType> extends BaseMap<ColumnType, String> {

}
