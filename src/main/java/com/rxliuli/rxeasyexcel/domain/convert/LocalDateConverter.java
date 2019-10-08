package com.rxliuli.rxeasyexcel.domain.convert;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author rxliuli
 */
public class LocalDateConverter implements IConverter<LocalDate> {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_FORMAT_EXCEL = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public String to(LocalDate localDateTime) {
        return DATE_FORMAT_EXCEL.format(localDateTime);
    }

    @Override
    public LocalDate from(String s) {
        return Instant.ofEpochMilli(Long.valueOf(s)).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
