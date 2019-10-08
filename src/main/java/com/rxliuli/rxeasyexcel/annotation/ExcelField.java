package com.rxliuli.rxeasyexcel.annotation;


import com.rxliuli.rxeasyexcel.domain.convert.IConverter;
import com.rxliuli.rxeasyexcel.domain.convert.NotSpecifyConverter;
import com.rxliuli.rxeasyexcel.domain.customize.CustomizeMap;
import com.rxliuli.rxeasyexcel.domain.customize.DefaultCustomizeMap;
import com.rxliuli.rxeasyexcel.domain.select.DefaultSelectMap;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;
import com.rxliuli.rxeasyexcel.domain.select.ISelectMap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Quding Ding
 * @since 2018/5/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {
    /**
     * excel header name
     */
    String columnName() default "";

    /**
     * 写入时所采取的转换器
     */
    Class<? extends IConverter> converter() default NotSpecifyConverter.class;

    /**
     * 表格的列排序，默认全部为 0（不排序）
     */
    int order() default 0;

    /**
     * 当前列的类型，默认为普通文本
     */
    ExcelColumnType type() default ExcelColumnType.TEXT;

    /**
     * 下拉框对应的数据提供类，默认为 {@link null}
     * 注意，如果提供了这个，则 converter 将会失效
     */
    Class<? extends ISelectMap<?>> select() default DefaultSelectMap.class;

    /**
     * 下拉框对应的数据提供类，默认为空字符串，为了解决循环依赖而生
     * @return
     */
    String selectClassName() default "";

    /**
     * 表头提示
     */
    String prompt() default "";

    /**
     * 错误消息，默认为 ""
     */
    String errMsg() default "";

    /**
     * 是否导出字段
     *
     * @return
     */
    boolean isExportField() default true;

    /**
     * 是否模板字段
     */
    boolean isTemplateField() default true;

    /**
     * 验证
     * 值最大长度
     */
    int maxLength() default 500;

    /**
     * 自定义属性
     */
    Class<? extends CustomizeMap<?, ?>> customize() default DefaultCustomizeMap.class;

}
