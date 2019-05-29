package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;

import java.time.LocalDateTime;

public class Car {
    @ExcelField(columnName = "车名111111111111111111111", order = 1)
    private String name;

    @ExcelField(columnName = "ID", order = 1, isTemplateField = false)
    private String id;

    @ExcelField(columnName = "颜色", order = 1)
    private String color;

    @ExcelField(columnName = "vo值", order = 1, isExportField = false)
    private String voVal;

    @ExcelField(columnName = "处置—过滤原因", order = 2)
    private String reason;
    /**
     * 处置-日志记录，
     * 是否对中标的网络数据进行记录：
     * 0——不记录
     * 1——记录
     */
    @ExcelField(columnName = "处置-日志记录", order = 3)
    private Integer log;
    /**
     * 日志上传
     * 是否对中标的网络数据的日志记录进行上报：
     * 0——不上传
     * 1——上传
     */
    @ExcelField(columnName = "日志上传", order = 4)
    private Integer report;
    /**
     * 生效时间， 指令的生效时间，采用 yyyy-MM-dd HH:mm:ss 格式
     */
    @ExcelField(columnName = "生效时间", order = 5)
    private LocalDateTime effectTime;
    /**
     * 过期时间， 指令的失效时间，采用 yyyy-MM-dd HH:mm:ss 格式
     */
    @ExcelField(columnName = "过期时间", order = 5)
    private LocalDateTime expiredTime;

    /**
     * 指令源, 1: SMMS，2：ISMS, 如果指令源为 SMMS的时候，这条只能不允许在后台修改和删除
     */
    @ExcelField(columnName = "指令源", order = 6)
    private Integer insSource;



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

    public LocalDateTime getEffectTime() {
        return effectTime;
    }

    public Car setEffectTime(LocalDateTime effectTime) {
        this.effectTime = effectTime;
        return this;
    }
}
