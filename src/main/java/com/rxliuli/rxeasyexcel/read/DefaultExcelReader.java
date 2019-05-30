package com.rxliuli.rxeasyexcel.read;

import com.rxliuli.rxeasyexcel.ExcelException;
import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.domain.ExcelImportError;
import com.rxliuli.rxeasyexcel.domain.ExcelReadContext;
import com.rxliuli.rxeasyexcel.domain.ExcelReadHeader;
import com.rxliuli.rxeasyexcel.domain.ImportDomain;
import com.rxliuli.rxeasyexcel.internal.util.ExcelBeanHelper;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Quding Ding
 * @since 2018/6/29
 */
public class DefaultExcelReader implements ExcelReader {


    private Workbook workbook;

    private InputStream inputStream;


    public DefaultExcelReader(InputStream inputStream) {
        try {
            this.workbook = WorkbookFactory.create(inputStream);
            this.inputStream = inputStream;
        } catch (IOException | InvalidFormatException e) {
            throw new ExcelException(e);
        }

    }

    @Override
    public <T> ImportDomain<T> resolve(ExcelReadContext<T> context) {
        Sheet sheet = this.workbook.getSheetAt(context.getSheetIndex());

        // 读取之前的操作
        context.getReadSheetHook().accept(sheet, context);

        // 解析header
        int startRow = context.getHeaderStart();
        Row header = sheet.getRow(startRow++);
        List<String> realHeaders = getHeaders(header);

        int lastRowNum = sheet.getLastRowNum();
        int totalCount = lastRowNum - header.getRowNum();
        List<T> resultContainer = new ArrayList<>(totalCount);
        Map<String, ExcelReadHeader> configHeaders = context.getHeaders();
        final LinkedList<ExcelImportError> errorList = new LinkedList<>();
        final LinkedMap<String, Integer> columnInfoMap = new LinkedMap<>();

        //获取列信息
        //添加列信息，只添加一次
        final AtomicInteger colAddNum = new AtomicInteger(0);
        header.cellIterator().forEachRemaining(x -> {
            final int columnIndex = x.getColumnIndex();
            final ExcelReadHeader tempHeader = configHeaders.get(realHeaders.get(columnIndex));
            final Field field = tempHeader.getField();
            columnInfoMap.put(field.getName(), columnIndex);
            colAddNum.incrementAndGet();
        });

        // 依次解析每一行
        for (; startRow <= lastRowNum; startRow++) {
            Row row = sheet.getRow(startRow);
            T instance = ExcelBeanHelper.newInstance(context.getClazz());
            final int i = startRow;
            row.cellIterator().forEachRemaining(x -> {
                final int columnIndex = x.getColumnIndex();
                final ExcelReadHeader tempHeader = configHeaders.get(realHeaders.get(columnIndex));
                final String columnValue = ExcelBeanHelper.getColumnValue(x);
                // 如果字段值为空字符串则直接跳过
                if (null == tempHeader || StringUtils.isEmpty(columnValue)) {
                    return;
                }
                Object value = null;
                final Field field = tempHeader.getField();
                final ExcelField excelField = field.getAnnotation(ExcelField.class);
                try {
                    switch (tempHeader.getType()) {
                        case TEXT:
                            value = tempHeader.getConvert().from(columnValue);
                            int maxLength = excelField.maxLength();
                            if (Objects.toString(value).length() > maxLength) {
                                errorList.add(new ExcelImportError(i, columnIndex, field.getName(), columnValue, null, "你输入的值超过" + maxLength + "字符，请重新输入"));
                            }
                            break;
                        case SELECT:
                            value = tempHeader.getSelectMap().getOrDefault(columnValue, null);
                            if (value == null || (value instanceof String && StringUtils.isEmpty((String) value))) {
                                errorList.add(new ExcelImportError(i, columnIndex, field.getName(), columnValue, null, "请选择下拉框的值"));
                            }
                            break;
                        default:
                            value = null;
                    }
                } catch (NumberFormatException e) {
                    final String msg = excelField.errMsg();
                    errorList.add(new ExcelImportError(i, columnIndex, field.getName(), columnValue, e, StringUtils.isEmpty(msg) ? "请输入正确的数字" : msg));

                } catch (DateTimeParseException e) {
                    final String msg = excelField.errMsg();
                    errorList.add(new ExcelImportError(i, columnIndex, field.getName(), columnValue, e, StringUtils.isEmpty(msg) ? "请输入正确的日期" : msg));
                } catch (Exception e) {
                    // 如果解析错误则记录下来
                    final String msg = excelField.errMsg();
                    errorList.add(new ExcelImportError(i, columnIndex, field.getName(), columnValue, e, StringUtils.isEmpty(msg) ? null : msg));
                }
                ExcelBeanHelper.fieldSetValue(field, instance, value);
            });
            resultContainer.add(instance);
        }
        return new ImportDomain<>(resultContainer, errorList, columnInfoMap);
    }


    @Override
    public void close() {
        try {
            inputStream.close();
            workbook.close();
        } catch (IOException e) {
            throw new ExcelException(e);
        }
    }


    /**
     * 获取到表头
     *
     * @param header header表头
     * @return 表头, 有序
     */
    private List<String> getHeaders(Row header) {
        List<String> headers = new ArrayList<>();
        header.cellIterator().forEachRemaining(x -> headers.add(x.getStringCellValue()));
        return headers;
    }

}
