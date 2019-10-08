package com.rxliuli.rxeasyexcel.read;

import com.rxliuli.rxeasyexcel.EasyExcel;
import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.domain.ExcelImportError;
import com.rxliuli.rxeasyexcel.domain.ExcelReadContext;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import com.rxliuli.rxeasyexcel.domain.ImportDomain;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;
import com.rxliuli.rxeasyexcel.model.ANumSelect;
import com.rxliuli.rxeasyexcel.writer.DateFieldTest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rxliuli
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ErrorImportTest {
    private final String currentPath = DateFieldTest.class.getClassLoader().getResource(".").getPath();
    private final int count = 5;
    private String fileName = currentPath + "/ErrorImportTest.xlsx";

    static String join(Collection<?> collection) {
        return collection.stream()
                .map(ToStringBuilder::reflectionToString)
                .collect(Collectors.joining("\n"));
    }

    @Test
    @Order(1)
    void exportDateList() {
        final List<Person> users = mockUser(count);
        EasyExcel.export(fileName)
                .export(ExcelWriteContext.builder()
                        .datasource(users)
                        .sheetName("user")
                        .build())
                .write();
    }

    @Test
    @Order(2)
    void importDateList() {
        ImportDomain<Person> result = new ImportDomain<>();
        try (InputStream is = new FileInputStream(currentPath + "ErrorImportTestExcel.xlsx");
             final ExcelReader reader = EasyExcel.read(is)) {
            result = reader.resolve(ExcelReadContext.<Person>builder()
                    .clazz(Person.class)
                    .build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        final List<Person> data = result.getData();
        System.out.println(join(data));
        List<ExcelImportError> errorList = result.getErrors();
        System.out.println(join(errorList));
        assertThat(data)
                .hasSize(count);
        assertThat(errorList)
                .hasSize(3);

        EasyExcel.export(fileName)
                .export(ExcelWriteContext.builder()
                        .datasource(data)
                        .errors(errorList)
                        .build()
                )
                .write();
    }

    @Test
    public void exportSelectFile() {
        EasyExcel.export(currentPath + "SelectExportTest.xlsx")
                .export(ExcelWriteContext.builder()
                        .datasource(Collections.emptyList())
                        .headers(Info.class)
                        .build()
                ).write();
    }

    @Test
    public void importSelectFile() {
        ImportDomain<Info> resolve = EasyExcel.read(currentPath + "SelectExportTest.xlsx")
                .resolve(ExcelReadContext.<Info>builder()
                        .clazz(Info.class)
                        .build());
        System.out.println(resolve);
    }

    @Test
    public void localTimeTest() {
        LocalTime now = LocalTime.now();
        System.out.println(now);
    }


    private List<Person> mockUser(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new Person("姓名 " + i, new Date(), LocalDate.now(), LocalTime.now()))
                .collect(Collectors.toList());
    }

    public static class Person {
        @ExcelField(columnName = "姓名", order = 1)
        private String username;
        @ExcelField(columnName = "日期", order = 2)
        private Date date;
        @ExcelField(columnName = "本地日期", order = 3, errMsg = "本地日期错误，请务必输入正确的日期。例如 2018-12-11")
        private LocalDate localDate;
        @ExcelField(columnName = "本地时间", order = 4)
        private LocalTime localTime;
        @ExcelField(columnName = "测试下拉框", order = 5, type = ExcelColumnType.SELECT, select = ANumSelect.class)
        private Integer ANum;

        public Person() {
        }

        public Person(String username, Date date, LocalDate localDate, LocalTime localTime) {
            this.username = username;
            this.date = date;
            this.localDate = localDate;
            this.localTime = localTime;
        }

        public Date getDate() {
            return date;
        }

        public Person setDate(Date date) {
            this.date = date;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public Person setUsername(String username) {
            this.username = username;
            return this;
        }

        public Integer getANum() {
            return ANum;
        }

        public Person setANum(Integer ANum) {
            this.ANum = ANum;
            return this;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("username", username)
                    .append("date", date)
                    .append("localDate", localDate)
                    .append("localTime", localTime)
                    .toString();
        }
    }

    public static class Info {
        @ExcelField(columnName = "测试下拉框", order = 5, type = ExcelColumnType.SELECT, select = ANumSelect.class)
        private Integer ANum;

        //        @ExcelField(columnName = "日期", order = 5)
        private LocalDateTime date;

        //@ExcelField(columnName = "时间", order = 5)
        private LocalTime time;

        @ExcelField(columnName = "名称")
        private String name;

        //        @ExcelField(columnName = "年龄", maxLength = 3)
        private Integer year;

        @ExcelField(columnName = "手机")
        private String num;

        @ExcelField(columnName = "类型")
        private Integer type;

        @ExcelField(columnName = "idcID")
        private String idcId;


        public Info() {
        }

        public String getIdcId() {
            return idcId;
        }

        public Info setIdcId(String idcId) {
            this.idcId = idcId;
            return this;
        }

        public Integer getType() {
            return type;
        }

        public Info setType(Integer type) {
            this.type = type;
            return this;
        }

        public String getNum() {
            return num;
        }

        public Info setNum(String num) {
            this.num = num;
            return this;
        }

        public Integer getYear() {
            return year;
        }

        public Info setYear(Integer year) {
            this.year = year;
            return this;
        }

        public String getName() {
            return name;
        }

        public Info setName(String name) {
            this.name = name;
            return this;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public Info setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public LocalTime getTime() {
            return time;
        }

        public Info setTime(LocalTime time) {
            this.time = time;
            return this;
        }

        public Info(Integer ANum) {
            this.ANum = ANum;
        }

        public Integer getANum() {
            return ANum;
        }

        public Info setANum(Integer ANum) {
            this.ANum = ANum;
            return this;
        }
    }
}
