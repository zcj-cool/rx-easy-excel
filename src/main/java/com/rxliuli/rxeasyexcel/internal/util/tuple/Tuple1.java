package com.rxliuli.rxeasyexcel.internal.util.tuple;

/**
 * @author rxliuli
 */
public class Tuple1<V1> implements Tuple {
    private final V1 v1;

    Tuple1(V1 v1) {
        this.v1 = v1;
    }

    public V1 getV1() {
        return v1;
    }
}
