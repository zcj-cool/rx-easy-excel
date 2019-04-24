package com.rxliuli.rxeasyexcel.domain.select;

import com.rxliuli.rxeasyexcel.internal.util.MapUtil;
import com.rxliuli.rxeasyexcel.internal.util.tuple.Tuple;
import com.rxliuli.rxeasyexcel.internal.util.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private static Map<Class, Tuple2<Map<?, String>, Map<String, ?>>> cacheMap = new ConcurrentHashMap<>();

    private SelectMapFactory() {
    }

    /**
     * 获取 tuple
     *
     * @param clazz 类型
     * @return 缓存的元组
     */
    private static Tuple2<Map<?, String>, Map<String, ?>> getTuple(Class<? extends ISelectMap<?>> clazz) {
        if (clazz == DefaultSelectMap.class) {
            return Tuple.of(null, null);
        }
        if (!cacheMap.containsKey(clazz)) {
            final ISelectMap<?> instance;
            try {
                instance = clazz.newInstance();
                Map<?, String> map = instance.getMap();
                final AtomicInteger i = new AtomicInteger(0);
                map = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, kv -> kv.getValue() + "-" + i.getAndIncrement()));
                SelectMapFactory.cacheMap.put(clazz, Tuple.of(map, MapUtil.reverse(map)));
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("获取 Map 错误，当前类没有无参的构造函数: {}", clazz);
            }
        }
        return cacheMap.get(clazz);
    }

    /**
     * 根据类型获取到对应的下拉框数据
     *
     * @param clazz 类型
     * @return 数据 Map
     */
    public static Map<?, String> get(Class<? extends ISelectMap<?>> clazz) {
        return getTuple(clazz).getV1();
    }

    /**
     * 得到一个反转的 Map
     *
     * @param clazz 类型
     * @return 反转的数据 Map
     */
    public static Map<String, ?> getReverse(Class<? extends ISelectMap<?>> clazz) {
        return getTuple(clazz).getV2();
    }

    /**
     * 清除指定类的缓存 Map
     *
     * @param clazz 类型
     */
    public static void remove(Class<? extends ISelectMap<?>> clazz) {
        cacheMap.remove(clazz);
    }
}
