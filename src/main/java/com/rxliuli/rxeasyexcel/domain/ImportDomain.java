package com.rxliuli.rxeasyexcel.domain;

import java.util.List;
import java.util.Map;

/**
 * 导入的得到的数据
 *
 * @author rxliuli
 */
public class ImportDomain<T> {
    /**
     * 数据列表
     */
    private List<T> data;
    /**
     * 错误列表
     */
    private List<ExcelImportError> errors;

    /**
     * 列信息
     */
    private Map<String, Integer> columnInfoMap;

    public ImportDomain() {
    }

    public ImportDomain(List<T> data, List<ExcelImportError> errors) {
        this.data = data;
        this.errors = errors;
    }

    public ImportDomain(List<T> data, List<ExcelImportError> errors, Map<String, Integer> columnInfoMap) {
        this.data = data;
        this.errors = errors;
        this.columnInfoMap = columnInfoMap;
    }

    public Map<String, Integer> getColumnInfoMap() {
        return columnInfoMap;
    }

    public ImportDomain<T> setColumnInfoMap(Map<String, Integer> columnInfoMap) {
        this.columnInfoMap = columnInfoMap;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public ImportDomain<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public List<ExcelImportError> getErrors() {
        return errors;
    }

    public ImportDomain<T> setErrors(List<ExcelImportError> errors) {
        this.errors = errors;
        return this;
    }
}
