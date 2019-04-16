package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;

/**
 * @author Quding Ding
 * @since 2018/8/2
 */
public class Id {

    @ExcelField(columnName = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public Id setId(Long id) {
        this.id = id;
        return this;
    }
}
