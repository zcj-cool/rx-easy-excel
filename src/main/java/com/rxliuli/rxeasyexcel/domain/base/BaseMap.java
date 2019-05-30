package com.rxliuli.rxeasyexcel.domain.base;

import java.util.Map;

/**
 * 注解 map的抽象
 *
 * @param <KeyType>
 * @param <ValType>
 */
public interface BaseMap<KeyType, ValType> {
    /**
     * 获取到映射表
     *
     * @return 映射表
     */
    Map<KeyType, ValType> getMap();
}
