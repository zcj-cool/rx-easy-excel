package com.rxliuli.rxeasyexcel.domain;

import com.rxliuli.rxeasyexcel.domain.convert.IConverter;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;

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
     * 单元格的显示类型
     */
    private final ExcelColumnType type;

    BaseExcelHeader(IConverter<Object> convert, ExcelColumnType type) {
        this.convert = convert;
        this.type = type;
    }

    public ExcelColumnType getType() {
        return type;
    }

    public IConverter<Object> getConvert() {
        return convert;
    }
}
