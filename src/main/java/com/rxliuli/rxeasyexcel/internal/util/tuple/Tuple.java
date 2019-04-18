package com.rxliuli.rxeasyexcel.internal.util.tuple;

/**
 * 基本元组接口
 *
 * @author rxliuli
 */
public interface Tuple {
    /**
     * 生成一个一元元组
     *
     * @param v1   第一个元素
     * @param <V1> 第一个元素的类型
     * @return 一元元组
     */
    public static <V1> Tuple1<V1> of(V1 v1) {
        return new Tuple1<>(v1);
    }

    /**
     * 生成一个二元元组
     *
     * @param v1   第一个元素
     * @param v2   第二个元素
     * @param <V1> 第一个元素的类型
     * @param <V2> 第二个元素的类型
     * @return 二元元组
     */
    public static <V1, V2> Tuple2<V1, V2> of(V1 v1, V2 v2) {
        return new Tuple2<>(v1, v2);
    }

    /**
     * 生成一个三元元组
     * @param v1 第一个元素
     * @param v2 第二个元素
     * @param v3 第三个元素
     * @param <V1> 第一个元素的类型
     * @param <V2> 第二个元素的类型
     * @param <V3> 第三个元素的类型
     * @return 三元元组
     */
    public static <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
        return new Tuple3<>(v1, v2, v3);
    }

    /**
     * 生成一个四元元组
     * @param v1 第一个元素
     * @param v2 第二个元素
     * @param v3 第三个元素
     * @param v4 第四个元素
     * @param <V1> 第一个元素的类型
     * @param <V2> 第二个元素的类型
     * @param <V3> 第三个元素的类型
     * @param <V4> 第四个元素的类型
     * @return 四元元组
     */
    public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> of(V1 v1, V2 v2, V3 v3, V4 v4) {
        return new Tuple4<>(v1, v2, v3, v4);
    }
}
