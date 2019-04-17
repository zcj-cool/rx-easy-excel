package com.rxliuli.rxeasyexcel.domain.select;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下拉框数据工厂
 *
 * @author rxliuli
 */
public class SelectMapFactory {
    private static final Logger log = LoggerFactory.getLogger(SelectMapFactory.class);

    /**
     * 缓存 Map
     */
    private static Map<Class, Map<?, String>> cacheMap = new ConcurrentHashMap<>();

    private SelectMapFactory() {
    }

    /**
     * 根据类型获取到对应的下拉框数据
     *
     * @param clazz 类型
     * @param <T>   下拉框对应字段的类型
     * @return 数据 Map
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<T, String> get(Class<? extends ISelectMap<?>> clazz) {
        if (clazz == DefaultSelectMap.class) {
            return null;
        }
        Map<?, String> map = SelectMapFactory.cacheMap.getOrDefault(clazz, null);
        if (map == null) {
            final ISelectMap<?> instance;
            try {
                instance = clazz.newInstance();
                map = instance.getMap();
                SelectMapFactory.cacheMap.put(clazz, map);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("获取 Map 错误，当前类没有无参的构造函数: {}", clazz);
            }
        }
        return (Map<T, String>) map;
    }

    /**
     * 清除指定类的缓存 Map
     *
     * @param clazz 类型
     */
    public static void remove(Class<? extends ISelectMap<T>> clazz) {
        cacheMap.remove(clazz);
    }
}
