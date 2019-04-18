package com.rxliuli.rxeasyexcel.writer;

import com.rxliuli.rxeasyexcel.EasyExcel;
import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author rxliuli
 */
class CustomPromptTest {
    private final String currentPath = CustomPromptTest.class.getClassLoader().getResource(".").getPath();
    private final int count = 5;
    private String fileName = currentPath + "/CustomPromptTest.xlsx";

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

    private List<Person> mockUser(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new Person("姓名 " + i, new Date(), LocalDate.now(), LocalTime.now()))
                .collect(Collectors.toList());
    }

    public static class Person {
        @ExcelField(columnName = "姓名", order = 1, prompt = "请输入真实姓名")
        private String username;
        @ExcelField(columnName = "日期", order = 2, prompt = "请输入一个正确的日期，格式为 yyyy-MM-dd。例如 2018-12-11")
        private Date date;
        @ExcelField(columnName = "本地日期", order = 3, errMsg = "本地日期错误，请务必输入正确的日期。例如 2018-12-11")
        private LocalDate localDate;
        @ExcelField(columnName = "本地时间", order = 4)
        private LocalTime localTime;

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
}
