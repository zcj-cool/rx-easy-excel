package com.rxliuli.rxeasyexcel.domain;


import com.rxliuli.rxeasyexcel.domain.convert.DefaultConverter;
import com.rxliuli.rxeasyexcel.domain.convert.IConverter;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Quding Ding
 * @since 2018/6/29
 */
public class ExcelReadHeader extends BaseExcelHeader {

    /**
     * 对应的属性字段
     */
    private Field field;

    private ExcelReadHeader(Field field, IConverter<Object> convert, Map<?, String> selectMap, ExcelColumnType type) {
        super(convert, selectMap, type);
        this.field = field;
    }

    public static ExcelReadHeader create(Field field, IConverter<Object> convert, Map<?, String> selectMap, ExcelColumnType type) {
        return new ExcelReadHeader(field, convert, selectMap, type);
    }

    public static ExcelReadHeader create(Field field) {
        return new ExcelReadHeader(field, new DefaultConverter(), null, ExcelColumnType.TEXT);
    }

    public Field getField() {
        return field;
    }
}
