package com.rxliuli.rxeasyexcel.writer;

import com.rxliuli.rxeasyexcel.EasyExcel;
import com.rxliuli.rxeasyexcel.domain.ExcelReadContext;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import com.rxliuli.rxeasyexcel.domain.ImportDomain;
import com.rxliuli.rxeasyexcel.model.Car;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

class ExcelWriteTest {
    private final String currentPath = ExcelSelectTest.class.getClassLoader().getResource(".").getPath();

    @Test
    void excelTemplateTest() {
        EasyExcel
                .export(currentPath + "/ExcelWriteTemplateTest.xlsx")
                .export(ExcelWriteContext.builder(true)
                        .headers(Car.class)
                        .datasource(Collections.emptyList())
                        .build()
                ).write();
    }

    @Test
    void excelDataTest() {
        EasyExcel
                .export(currentPath + "/ExcelWriteDataTest.xlsx")
                .export(ExcelWriteContext.builder(false)
                        .headers(Car.class)
                        .datasource(Collections.emptyList())
                        .build()
                ).write();
    }

    @Test
    void excelLocalDateTimeTest() {
        EasyExcel
                .export(currentPath + "/ExcelWriteDataTest.xlsx")
                .export(ExcelWriteContext.builder(false)
                        .headers(Car.class)
                        .datasource(Lists.newArrayList(
                                new Car().setEffectTime(LocalDateTime.now())
                                        .setName("23654")
                        ))
                        .build()
                ).write();
    }

    @Test
    void importTest() {
        ImportDomain<Car> resolve = EasyExcel.read(currentPath + "/ExcelWriteDataTest.xlsx")
                .resolve(ExcelReadContext.<Car>builder()
                        .clazz(Car.class)
                        .build());
        System.out.println(resolve);
    }

}
