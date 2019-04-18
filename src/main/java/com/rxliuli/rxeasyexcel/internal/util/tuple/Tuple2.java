package com.rxliuli.rxeasyexcel.internal.util.tuple;

/**
 * @author rxliuli
 */
public class Tuple2<V1, V2> implements Tuple {
    private final V1 v1;
    private final V2 v2;

    Tuple2(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public V1 getV1() {
        return v1;
    }

    public V2 getV2() {
        return v2;
    }
}
