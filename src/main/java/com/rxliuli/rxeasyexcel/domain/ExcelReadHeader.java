package com.rxliuli.rxeasyexcel.domain;


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
     * 下拉框的 Map，仅在 {@link super#getType()} 为 {@link ExcelColumnType#SELECT} 时有值
     */
    private final Map<String, ?> selectMap;
    /**
     * 对应的属性字段
     */
    private final Field field;

    private ExcelReadHeader(Field field, IConverter<Object> convert, Map<String, ?> selectMap, ExcelColumnType type) {
        super(convert, type);
        this.field = field;
        this.selectMap = selectMap;
    }

    public static ExcelReadHeader create(Field field, IConverter<Object> convert, Map<String, ?> selectMap, ExcelColumnType type) {
        return new ExcelReadHeader(field, convert, selectMap, type);
    }

    public Map<String, ?> getSelectMap() {
        return selectMap;
    }

    public Field getField() {
        return field;
    }
}
