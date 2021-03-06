package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.domain.convert.DateConverter;
import com.rxliuli.rxeasyexcel.domain.convert.LocalDateConverter;
import com.rxliuli.rxeasyexcel.domain.convert.LocalDateTimeConverter;
import com.rxliuli.rxeasyexcel.domain.convert.LocalTimeConverter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author rxliuli
 */
public class Person {
    @ExcelField(columnName = "姓名")
    private String username;
    @ExcelField(columnName = "生日", converter = LocalDateTimeConverter.class)
    private LocalDateTime birthday;
    @ExcelField(columnName = "日期", converter = DateConverter.class)
    private Date date;
    @ExcelField(columnName = "本地日期", converter = LocalDateConverter.class)
    private LocalDate localDate;
    @ExcelField(columnName = "本地时间", converter = LocalTimeConverter.class)
    private LocalTime localTime;

    public Person() {
    }

    public Person(String username, LocalDateTime birthday, Date date, LocalDate localDate, LocalTime localTime) {
        this.username = username;
        this.birthday = birthday;
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

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public Person setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("birthday", birthday)
                .append("date", date)
                .toString();
    }
}
