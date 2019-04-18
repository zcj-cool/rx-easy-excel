package com.rxliuli.rxeasyexcel.internal.util;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * map 的工具类
 *
 * @author rxliuli
 */
public class MapUtil {
    /**
     * 反转 Map 的键和值
     *
     * @param map map 集合
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 反转后的 Map 集合
     */
    public static <K, V> Map<V, K> reverse(Map<K, V> map) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
