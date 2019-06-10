package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.domain.select.ExcelColumnType;

public class SelectTest {
    @ExcelField(type = ExcelColumnType.SELECT, select = ANumSelect.class)
    private Integer selectVal;

    public Integer getSelectVal() {
        return selectVal;
    }

    public SelectTest setSelectVal(Integer selectVal) {
        this.selectVal = selectVal;
        return this;
    }
}
