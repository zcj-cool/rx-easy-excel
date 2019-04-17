package com.rxliuli.rxeasyexcel.domain;

/**
 * excel 导入时发生的错误
 *
 * @author rxliuli
 */
public class ExcelImportError {
    /**
     * 所在行
     */
    private int row;
    /**
     * 所在列
     */
    private int col;
    /**
     * 字段的名字
     */
    private String field;
    /**
     * 错误的值
     */
    private String val;
    /**
     * 抛出的异常
     */
    private Exception e;
    /**
     * 错误消息，默认为空（该参数必须手动设置）
     */
    private String msg;

    public ExcelImportError() {
    }

    public ExcelImportError(int row, int col, String field, String val, Exception e, String msg) {
        this.row = row;
        this.col = col;
        this.field = field;
        this.val = val;
        this.e = e;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public ExcelImportError setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getRow() {
        return row;
    }

    public ExcelImportError setRow(int row) {
        this.row = row;
        return this;
    }

    public int getCol() {
        return col;
    }

    public ExcelImportError setCol(int col) {
        this.col = col;
        return this;
    }

    public String getField() {
        return field;
    }

    public ExcelImportError setField(String field) {
        this.field = field;
        return this;
    }

    public String getVal() {
        return val;
    }

    public ExcelImportError setVal(String val) {
        this.val = val;
        return this;
    }

    public Exception getE() {
        return e;
    }

    public ExcelImportError setE(Exception e) {
        this.e = e;
        return this;
    }
}
