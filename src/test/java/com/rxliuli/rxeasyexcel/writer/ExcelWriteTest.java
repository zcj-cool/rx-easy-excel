package com.rxliuli.rxeasyexcel.writer;

import com.rxliuli.rxeasyexcel.EasyExcel;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import com.rxliuli.rxeasyexcel.model.Car;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class ExcelWriteTest {
    private final String currentPath = ExcelSelectTest.class.getClassLoader().getResource(".").getPath();
    private String fileName = currentPath + "/ExcelTemplateTest.xlsx";

    @Test
    public void excelTemplateTest() {
        EasyExcel
                .export(fileName)
                .export(ExcelWriteContext.builder(true)
                        .headers(Car.class)
                        .datasource(Collections.emptyList())
                        .build()
                ).write();
    }
}
