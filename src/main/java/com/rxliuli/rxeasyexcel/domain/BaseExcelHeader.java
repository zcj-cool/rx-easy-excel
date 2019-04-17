package com.rxliuli.rxeasyexcel.domain;

import com.rxliuli.rxeasyexcel.domain.convert.IConverter;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;

import java.util.Map;

/**
 * 基本的 excel 头
 *
 * @author rxliuli
 */
public abstract class BaseExcelHeader {
    /**
     * 对应转换器
     */
    private final IConverter<Object> convert;
    /**
     * 下拉框的 Map，仅在 {@link #type} 为 {@link ExcelColumnType#SELECT} 时有值
     */
    private final Map<?, String> selectMap;
    /**
     * 单元格的显示类型
     */
    private final ExcelColumnType type;

    BaseExcelHeader(IConverter<Object> convert, Map<?, String> selectMap, ExcelColumnType type) {
        this.convert = convert;
        this.selectMap = selectMap;
        this.type = type;
    }

    public Map<?, String> getSelectMap() {
        return selectMap;
    }

    public ExcelColumnType getType() {
        return type;
    }

    public IConverter<Object> getConvert() {
        return convert;
    }
}
