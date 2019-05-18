package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;

public class Car {
    @ExcelField(columnName = "车名111111111111111111111", order = 1)
    private String name;

    @ExcelField(columnName = "ID", order = 1, isTemplateField = false)
    private String id;

    @ExcelField(columnName = "颜色", order = 1)
    private String color;

    @ExcelField(columnName = "vo值", order = 1, isExportField = false)
    private String voVal;

    public String getName() {
        return name;
    }

    public Car setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public Car setId(String id) {
        this.id = id;
        return this;
    }

    public String getColor() {
        return color;
    }

    public Car setColor(String color) {
        this.color = color;
        return this;
    }

    public String getVoVal() {
        return voVal;
    }

    public Car setVoVal(String voVal) {
        this.voVal = voVal;
        return this;
    }
}
