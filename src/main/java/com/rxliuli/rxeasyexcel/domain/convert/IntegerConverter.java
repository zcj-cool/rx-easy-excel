package com.rxliuli.rxeasyexcel.domain.convert;

/**
 * @author rxliuli
 */
public class IntegerConverter implements IConverter<Integer> {
    @Override
    public String to(Integer integer) {
        return Integer.toString(integer);
    }

    @Override
    public Integer from(String s) {
        return Integer.valueOf(s);
    }
}
