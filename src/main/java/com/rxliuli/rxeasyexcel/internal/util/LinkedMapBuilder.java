package com.rxliuli.rxeasyexcel.internal.util;

import org.apache.commons.collections4.map.LinkedMap;

/**
 * map 构建器
 *
 * @author rxliuli
 */
public class LinkedMapBuilder<K, V> {
    private final LinkedMap<K, V> innerMap = new LinkedMap<>();

    private LinkedMapBuilder() {
    }

    public static <K, V> LinkedMapBuilder<K, V> builder() {
        return new LinkedMapBuilder<>();
    }

    public LinkedMapBuilder<K, V> put(K k, V v) {
        this.innerMap.put(k, v);
        return this;
    }

    public LinkedMap<K, V> build() {
        return this.innerMap;
    }
}
