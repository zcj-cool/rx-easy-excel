package com.rxliuli.rxeasyexcel.domain.select;

import com.rxliuli.rxeasyexcel.ExcelException;

import java.util.Map;

/**
 * 默认的下拉框获取数据的类
 *
 * @author rxliuli
 */
public class DefaultSelectMap implements ISelectMap<Object> {
    @Override
    public Map<Object, String> getMap() {
        throw new ExcelException("不能使用默认的获取下拉框数据的方法");
    }
}
