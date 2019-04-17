package com.rxliuli.rxeasyexcel.domain;


import com.rxliuli.rxeasyexcel.domain.convert.DefaultConverter;
import com.rxliuli.rxeasyexcel.domain.convert.IConverter;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;

import java.util.Map;

/**
 * excel header的封装
 *
 * @author Quding Ding
 * @since 2018/6/27
 */
public class ExcelWriterHeader extends BaseExcelHeader {
    /**
     * header展示名称
     */
    private final String name;
    /**
     * 表头提示
     */
    private final String prompt;

    private ExcelWriterHeader(String name, IConverter<Object> convert, Map<?, String> selectMap, ExcelColumnType type, String prompt) {
        super(convert, selectMap, type);
        this.name = name;
        this.prompt = prompt;
    }

    public static ExcelWriterHeader create(String name) {
        return new ExcelWriterHeader(name, new DefaultConverter(), null, ExcelColumnType.TEXT, "");
    }

    public static ExcelWriterHeader create(String name, IConverter<Object> convert, Map<?, String> selectMap, ExcelColumnType type, String prompt) {
        return new ExcelWriterHeader(name, convert, selectMap, type, prompt);
    }

    public String getPrompt() {
        return prompt;
    }

    public String getName() {
        return name;
    }
}
