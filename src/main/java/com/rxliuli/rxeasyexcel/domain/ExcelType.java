package com.rxliuli.rxeasyexcel.domain;


import com.rxliuli.rxeasyexcel.internal.restrain.WorkbookCreate;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Quding Ding
 * @since 2018/2/6
 */
public enum ExcelType implements WorkbookCreate {
    /**
     * xls
     */
    XLS {
        @Override
        public Workbook workbook(int mayRowCount) {
            return new HSSFWorkbook();
        }
    },
    /**
     * xlsx
     */
    XLSX {
        @Override
        public Workbook workbook(int mayRowCount) {
            Workbook workbook = new XSSFWorkbook();
            if (mayRowCount > 3000) {
                workbook = new SXSSFWorkbook((XSSFWorkbook) workbook, 100);
            }
            return workbook;
        }
    };

}
