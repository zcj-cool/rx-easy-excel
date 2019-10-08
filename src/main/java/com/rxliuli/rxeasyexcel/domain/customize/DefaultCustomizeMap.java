package com.rxliuli.rxeasyexcel.domain.customize;

import com.rxliuli.rxeasyexcel.ExcelException;

import java.util.Map;

/**
 * 默认的获取自定义数据的类
 *
 * @author rxliuli
 */
public class DefaultCustomizeMap implements CustomizeMap<String, String> {
    @Override
    public Map<String, String> getMap() {
        throw new ExcelException("不能使用默认的获取自定义数据的方法");
    }
}
