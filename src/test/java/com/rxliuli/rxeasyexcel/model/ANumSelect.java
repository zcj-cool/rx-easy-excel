package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.domain.select.ISelectMap;
import com.rxliuli.rxeasyexcel.internal.util.LinkedMapBuilder;

import java.util.Map;

public class ANumSelect implements ISelectMap<Integer> {
    @Override
    public Map<Integer, String> getMap() {
        return LinkedMapBuilder.<Integer, String>builder()
                .put(1, "测试1")
                .build();
    }
}
