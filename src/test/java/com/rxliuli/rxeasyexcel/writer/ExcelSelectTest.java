package com.rxliuli.rxeasyexcel.writer;

import com.rxliuli.rxeasyexcel.EasyExcel;
import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;
import com.rxliuli.rxeasyexcel.domain.select.ISelectMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author rxliuli
 */
public class ExcelSelectTest {
    private final String currentPath = ExcelSelectTest.class.getClassLoader().getResource(".").getPath();
    private final int count = 5;
    private String fileName = currentPath + "/ExcelSelectTest.xlsx";

    @Test
    void excelExport() {
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

    public static class PersonSelect {
        public static class UsernameMap implements ISelectMap<Integer> {
            @Override
            public Map<Integer, String> getMap() {
                final Map<Integer, String> map = new HashMap<>();
                map.put(1, "保密");
                map.put(2, "男");
                map.put(3, "女");
                return map;
            }
        }
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
        @ExcelField(columnName = "性别", order = 5, type = ExcelColumnType.SELECT, selectClassName = "com.rxliuli.rxeasyexcel.writer.ExcelSelectTest$PersonSelect$UsernameMap")
        private Integer gender;

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

        public LocalDate getLocalDate() {
            return localDate;
        }

        public Person setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
            return this;
        }

        public LocalTime getLocalTime() {
            return localTime;
        }

        public Person setLocalTime(LocalTime localTime) {
            this.localTime = localTime;
            return this;
        }

        public Integer getGender() {
            return gender;
        }

        public Person setGender(Integer gender) {
            this.gender = gender;
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
