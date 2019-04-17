package com.rxliuli.rxeasyexcel.write;

import com.rxliuli.rxeasyexcel.ExcelException;
import com.rxliuli.rxeasyexcel.domain.ExcelImportError;
import com.rxliuli.rxeasyexcel.domain.ExcelType;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import com.rxliuli.rxeasyexcel.domain.ExcelWriterHeader;
import com.rxliuli.rxeasyexcel.internal.util.Assert;
import com.rxliuli.rxeasyexcel.internal.util.ExcelBeanHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Quding Ding
 * @since 2018/6/28
 */
public class DefaultExcelWriter implements ExcelWriter {

    private static Logger logger = LoggerFactory.getLogger(DefaultExcelWriter.class);
    private final ExcelType excelType;
    /**
     * 工作簿
     */
    private Workbook workbook;
    /**
     * 输出目标
     */
    private OutputStream outputStream;

    public DefaultExcelWriter(ExcelType excelType, OutputStream outputStream) {
        Assert.notNull(excelType, "excelType can't be null");
        Assert.notNull(outputStream, "outputStream can't be null");
        this.excelType = excelType;
        this.outputStream = outputStream;
    }

    /**
     * 把数据写入该表中
     *
     * @return 该实例
     */
    @Override
    public DefaultExcelWriter export(ExcelWriteContext context) {
        createWorkbookIfNull(context);

        Sheet sheet = StringUtils.isEmpty(context.getSheetName())
                ? workbook.createSheet()
                : workbook.createSheet(context.getSheetName());

        // custom 处理
        context.getCreateSheetHook().accept(sheet, context);

        int startRow = context.getStartRow();

        // 写表头
        Row headerRow = sheet.createRow(startRow++);
        int[] tempCol = {0};
        LinkedHashMap<String, ExcelWriterHeader> headers = context.getHeaders();
        headers.forEach((k, v) -> {
            Cell cell = headerRow.createCell(tempCol[0]++);
            cell.setCellValue(v.getName());
        });

        // 写数据
        for (Map<String, Object> rowData : context.getDatasource()) {
            Row row = sheet.createRow(startRow++);
            tempCol[0] = 0;
            headers.forEach((k, v) -> {
                Cell cell = row.createCell(tempCol[0]++);
                Object value = rowData.get(k);
                ExcelBeanHelper.autoFitCell(cell, value == null ? null : v.getConvert().to(value));
            });
        }

        // 绘图对象
        final Drawing<?> drawing = sheet.createDrawingPatriarch();

        // 写错误数据
        final List<ExcelImportError> errors = context.getErrors();
        errors.forEach(error -> createCell(sheet, drawing, error));
        return this;
    }

    /**
     * 创建一个单元格
     *
     * @param sheet   当前片区
     * @param drawing 绘图对象
     * @param error   错误信息
     */
    private void createCell(Sheet sheet, Drawing<?> drawing, ExcelImportError error) {
        final Cell cell = sheet.getRow(error.getRow()).getCell(error.getCol());
        final CellStyle style = this.createErrorCellStyle();
        cell.setCellValue(error.getVal());
        cell.setCellStyle(style);
        final Comment comment = createComment(drawing, error);
        cell.setCellComment(comment);
    }

    /**
     * 获取一个单元格批注对象
     *
     * @param drawing 绘图对象
     * @param error   错误信息
     * @return 批注
     */
    public Comment createComment(Drawing<?> drawing, ExcelImportError error) {
        final Comment comment = drawing.createCellComment(this.workbook.getCreationHelper().createClientAnchor());
        comment.setString(this.excelType == ExcelType.XLSX ? new XSSFRichTextString(error.getMsg()) : new HSSFRichTextString(error.getMsg()));
        return comment;
    }

    /**
     * 获取错误样式
     */
    private CellStyle createErrorCellStyle() {
        final CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED1.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillBackgroundColor(IndexedColors.RED1.getIndex());
        style.setFillPattern(FillPatternType.BIG_SPOTS);
        return style;
    }

    /**
     * 创建工作本
     *
     * @param context 一张sheet上下文
     */
    private void createWorkbookIfNull(ExcelWriteContext context) {
        // 不存在则创建目录
        if (null == workbook) {
            workbook = excelType.workbook(context.getDatasource().size());
        }
    }


    @Override
    public void write() {
        try {
            this.workbook.write(outputStream);
        } catch (IOException e) {
            throw new ExcelException(e);
        } finally {
            // 释放资源
            try {
                this.workbook.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                logger.error("write fail", e);
            }
        }
    }
}
