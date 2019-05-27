package com.rxliuli.rxeasyexcel.internal.util;

import com.rxliuli.rxeasyexcel.ExcelException;
import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.annotation.ExcelIgnore;
import com.rxliuli.rxeasyexcel.domain.ExcelReadHeader;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import com.rxliuli.rxeasyexcel.domain.ExcelWriterHeader;
import com.rxliuli.rxeasyexcel.domain.convert.ConverterFactory;
import com.rxliuli.rxeasyexcel.domain.convert.IConverter;
import com.rxliuli.rxeasyexcel.domain.convert.NotSpecifyConverter;
import com.rxliuli.rxeasyexcel.domain.select.SelectMapFactory;
import com.rxliuli.rxeasyexcel.internal.util.tuple.Tuple;
import com.rxliuli.rxeasyexcel.internal.util.tuple.Tuple3;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 导表过程中对Bean的一些处理
 *
 * @author Quding Ding
 * @since 2018/6/28
 */
public class ExcelBeanHelper {

    private static final String NULL_VAL = "";

    /**
     * bean转Map函数,支持使用自定义注解
     *
     * @param bean 对应的bean
     * @return map中 key 属性名  属性值
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> beanToMap(List<?> bean) {
        if (null == bean || bean.isEmpty()) {
            return Collections.emptyList();
        }
        // 处理已经为map
        Object tempBean = bean.get(0);
        if (tempBean instanceof Map) {
            return (List<Map<String, Object>>) bean;
        }
        // 处理bean
        return bean.stream()
                .map(ExcelBeanHelper::toMap)
                .collect(Collectors.toList());
    }

    /**
     * 通过bean拿到对应的excel header
     *
     * @param bean 实例
     * @param <T>  bean类型,支持LinkedHashMap 与 class
     * @return LinkedHashMap key field name  value ExcelWriterHeader
     */
    @SuppressWarnings("unchecked")
    public static <T> LinkedHashMap<String, ExcelWriterHeader> beanToWriterHeaders(T bean, ExcelWriteContext context) {
        // 为bean情况 获取到所有字段
        return beanToWriterHeaders(bean.getClass(), context);
    }

    /**
     * 通过bean拿到对应的excel header
     *
     * @param clazz class 类型
     * @param <T>   bean类型,支持LinkedHashMap 与 class
     * @return LinkedHashMap key field name  value ExcelWriterHeader
     */
    @SuppressWarnings("unchecked")
    public static <T> LinkedHashMap<String, ExcelWriterHeader> beanToWriterHeaders(Class<?> clazz, ExcelWriteContext context) {
        // 为bean情况 获取到所有字段
        return getSortedFieldStream(clazz, context)
                .map(x -> {
                    final Tuple3<String, ? extends IConverter, ExcelField> triple = castHeaderNameAndConverter(x);
                    Class selectClass = getSelectClass(triple);
                    return Tuple.of(x.getName(), ExcelWriterHeader.create(triple.getV1(), triple.getV2(), SelectMapFactory.get(selectClass), triple.getV3().type(), triple.getV3().prompt()));
                })
                .collect(LinkedHashMap::new, (l, v) -> l.put(v.getV1(), v.getV2()), HashMap::putAll);
    }

    /**
     * bean转为对应的读操作header
     *
     * @param clazz 实体类型
     * @param <T>   试题类型
     * @return 读操作header, key columnName value ExcelReadHeader
     */
    public static <T> Map<String, ExcelReadHeader> beanToReaderHeaders(Class<T> clazz) {
        return getSortedFieldStream(clazz)
                .map(x -> {
                    final Tuple3<String, ? extends IConverter<Object>, ExcelField> tuple3 = castHeaderNameAndConverter(x);
                    Class selectClass = getSelectClass(tuple3);
                    return Tuple.of(tuple3.getV1(), ExcelReadHeader.create(x, tuple3.getV2(), SelectMapFactory.getReverse(selectClass), tuple3.getV3().type()));
                })
                .collect(HashMap::new, (l, v) -> l.put(v.getV1(), v.getV2()), HashMap::putAll);
    }

    /**
     * 获取下拉框获取数据类类型，优先处理字符串类类型
     *
     * @param tuple3
     * @return
     */
    private static Class getSelectClass(Tuple3<String, ? extends IConverter, ExcelField> tuple3) {
        Class aClass = null;
        try {
            aClass = NULL_VAL.equals(tuple3.getV3().selectClassName()) ? tuple3.getV3().select() : Class.forName(tuple3.getV3().selectClassName());
        } catch (ClassNotFoundException e) {
            aClass = tuple3.getV3().select();
        }
        return aClass;
    }

    private static <T> Stream<Field> getSortedFieldStream(Class<T> clazz) {
        return getSortedFieldStream(clazz, null);
    }

    private static <T> Stream<Field> getSortedFieldStream(Class<T> clazz, ExcelWriteContext context) {
        return SuperClassUtil.getAllDeclaredField(clazz).stream()
                // 过滤掉没有使用 ExcelField 注解指定的字段
                .filter(x -> x.getAnnotation(ExcelField.class) != null)
                // 过滤掉指定忽略的字段
                .filter(x -> Objects.isNull(x.getAnnotation(ExcelIgnore.class)))
                //导出过滤逻辑
                .filter(x -> context == null
                        || (context.isTemplateExport() && x.getAnnotation(ExcelField.class).isTemplateField())
                        || (!context.isTemplateExport() && x.getAnnotation(ExcelField.class).isExportField())
                )
                .sorted(Comparator.comparing(x -> x.getAnnotation(ExcelField.class).order()));
    }

    /**
     * 从字段中获取到对应的表头名字与转换器
     *
     * @param field 字段
     * @return 使用 {@link Tuple3} 封装的两个字段
     */
    private static Tuple3<String, ? extends IConverter<Object>, ExcelField> castHeaderNameAndConverter(Field field) {
        field.setAccessible(true);
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        // 如果 convertClass 未指定，则根据字段类型获取对应的默认转换器
        Class<? extends IConverter> convertClass = excelField.converter();
        if (NotSpecifyConverter.class.equals(convertClass)) {
            convertClass = ConverterFactory.get(field.getType());
        }
        final IConverter<Object> convert = ConvertHelper.getConvert(convertClass);
        final String columnName = excelField.columnName();
        final String name = StringUtils.isEmpty(columnName) ? field.getName() : columnName;
        return Tuple.of(name, convert, excelField);
    }

    /**
     * 根据自身值类型自动填入表单对应的类型
     *
     * @param cell  表单格子
     * @param value 值类型
     */
    public static void autoFitCell(Cell cell, Object value) {
        if (null == value) {
            return;
        }
        cell.setCellValue(String.valueOf(value));
    }

    /**
     * 创建一个bean
     *
     * @param clazz 创建bean
     * @param <T>   bean类型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ExcelException(e);
        }
    }

    public static void fieldSetValue(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            // 如果字段值为空则不进行设置
            if (value == null) {
                return;
            }
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new ExcelException(e);
        }
    }

    public static String getColumnValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case STRING:
            case FORMULA:
            case BLANK:
                return cell.getStringCellValue();
            case NUMERIC:
                //判断是否日期
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    return Objects.toString(date.getTime());
                }
                final double value = cell.getNumericCellValue();
                Object res = value;
                if (value == (int) value) {
                    res = (int) value;
                }
                return Objects.toString(res);
            case BOOLEAN:
                return Objects.toString(cell.getBooleanCellValue());
            case _NONE:
            case ERROR:
            default:
                return null;
        }
    }

    /**
     * bean to map
     *
     * @param bean bean
     * @return map key is bean filed name,value is the filed value
     */
    private static <T> Map<String, Object> toMap(T bean) {
        return getSortedFieldStream(bean.getClass())
                .map(x -> {
                    x.setAccessible(true);
                    try {
                        return Tuple.of(x.getName(), x.get(bean));
                    } catch (IllegalAccessException e) {
                        // do nothing
                    }
                    return null;
                })
                .filter(x -> x != null && !Objects.equals(x.getV1(), "this$0"))
                .collect(HashMap::new, (l, v) -> l.put(v.getV1(), v.getV2()), HashMap::putAll);
    }

    public static void autoColumnWidth(Sheet sheet, int columnIndex) {
        sheet.autoSizeColumn(columnIndex);
        sheet.setColumnWidth(columnIndex, sheet.getColumnWidth(columnIndex) * 17 / 10);
    }
}
