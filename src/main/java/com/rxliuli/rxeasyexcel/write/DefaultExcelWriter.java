package com.rxliuli.rxeasyexcel.write;

import com.rxliuli.rxeasyexcel.ExcelException;
import com.rxliuli.rxeasyexcel.domain.ExcelImportError;
import com.rxliuli.rxeasyexcel.domain.ExcelType;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import com.rxliuli.rxeasyexcel.domain.ExcelWriterHeader;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;
import com.rxliuli.rxeasyexcel.internal.util.Assert;
import com.rxliuli.rxeasyexcel.internal.util.ExcelBeanHelper;
import com.rxliuli.rxeasyexcel.internal.util.tuple.Tuple;
import com.rxliuli.rxeasyexcel.internal.util.tuple.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
        final AtomicInteger tempCol = new AtomicInteger(0);
        LinkedHashMap<String, ExcelWriterHeader> headers = context.getHeaders();
        // 下拉框数据
        final LinkedList<Tuple2<String[], Integer>> selectTupleList = new LinkedList<>();
        // 绘图对象
        final Drawing<?> drawing = sheet.createDrawingPatriarch();
        CellStyle headerStyle = getHeaderStyle();
        headers.forEach((k, v) -> {
            Cell cell = headerRow.createCell(tempCol.getAndIncrement());
            cell.setCellValue(v.getName());
            // 批注
            final String prompt = v.getPrompt();
            if (StringUtils.isNotEmpty(prompt)) {
                final Comment comment = createComment(sheet, drawing, prompt, cell.getRowIndex(), cell.getColumnIndex());
                cell.setCellComment(comment);
            }
            // 下拉框数据记录
            if (v.getType() == ExcelColumnType.SELECT) {
                selectTupleList.push(Tuple.of(v.getSelectMap().values().toArray(new String[]{}), cell.getColumnIndex()));
            }
            //设置宽度自适应
            int columnIndex = cell.getColumnIndex();
            ExcelBeanHelper.autoColumnWidth(sheet, columnIndex);
            //设置表头样式
            cell.setCellStyle(headerStyle);
        });

        // 下拉框设置
        selectTupleList.forEach(tuple3 -> {
            final DataValidationHelper helper = sheet.getDataValidationHelper();

            // 约束值
            final DataValidationConstraint constraint = helper.createExplicitListConstraint(tuple3.getV1());
            constraint.setExplicitListValues(tuple3.getV1());
            // 作用范围
            final Integer col = tuple3.getV2();
            final CellRangeAddressList regions = new CellRangeAddressList(1, 50000, col, col);
            final DataValidation validation = helper.createValidation(constraint, regions);

            sheet.addValidationData(validation);
        });

        // 写数据
        for (Map<String, Object> rowData : context.getDatasource()) {
            Row row = sheet.createRow(startRow++);
            tempCol.set(0);
            headers.forEach((k, v) -> {
                Cell cell = row.createCell(tempCol.getAndIncrement());
                Object value = rowData.get(k);
                final String text;
                switch (v.getType()) {
                    case TEXT:
                        text = value == null ? null : v.getConvert().to(value);
                        break;
                    case SELECT:
                        text = value == null ? null : v.getSelectMap().getOrDefault(value, null);
                        break;
                    default:
                        text = null;
                }
                ExcelBeanHelper.autoFitCell(cell, text);
            });
        }

        // 写错误数据
        final List<ExcelImportError> errors = context.getErrors();
        errors.forEach(error -> {
            final Cell cell = sheet.getRow(error.getRow()).getCell(error.getCol());
            final CellStyle style = this.createErrorCellStyle();
            cell.setCellValue(error.getVal());
            cell.setCellStyle(style);
            // 错误批注
            if (StringUtils.isNotEmpty(error.getMsg())) {
                final Comment comment = createComment(sheet, drawing, error.getMsg(), cell.getRowIndex(), cell.getColumnIndex());
                cell.setCellComment(comment);
            }
        });
        return this;
    }

    /**
     * 获取表头样式
     */
    private CellStyle getHeaderStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 获取一个单元格批注对象
     * @param sheet
     * @param drawing 绘图对象
     * @param prompt  提示信息
     * @return 批注
     */
    private Comment createComment(Sheet sheet, Drawing<?> drawing, String prompt, int row, int col) {
        //计算内容使批注自适应
        //获取列的宽度
        int colWidth = sheet.getColumnWidth(col);
        int length = colWidth * 2;
        //行数
        int rowNum = prompt.length() / length + 1;
        final Comment comment = drawing.createCellComment(this.excelType == ExcelType.XLSX
                ? new XSSFClientAnchor(0, 0, 0, 0, col + 3, row + 3, col + 5, row + 8 + rowNum)
                : new HSSFClientAnchor(0, 0, 0, 0, (short) (col + 3), row + 3, (short) (col + 5), row + 8 + rowNum)
        );
        comment.setString(this.excelType == ExcelType.XLSX ? new XSSFRichTextString(prompt) : new HSSFRichTextString(prompt));
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
