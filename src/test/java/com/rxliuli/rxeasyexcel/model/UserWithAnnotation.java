package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.annotation.ExcelIgnore;
import com.rxliuli.rxeasyexcel.domain.convert.DateConverter;

import java.util.Date;

/**
 * @author Quding Ding
 * @since 2018/6/29
 */
public class UserWithAnnotation {

    @ExcelField(columnName = "用户名")
    private String username;

    @ExcelField(columnName = "用户密码")
    private String passwd;

    @ExcelIgnore
    private String nickName;

    @ExcelField(columnName = "登录日期", convert = DateConverter.class)
    private Date date;

    public UserWithAnnotation() {
    }

    public UserWithAnnotation(String username, String passwd, String nickName, Date date) {
        this.username = username;
        this.passwd = passwd;
        this.nickName = nickName;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public UserWithAnnotation setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPasswd() {
        return passwd;
    }

    public UserWithAnnotation setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public UserWithAnnotation setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public UserWithAnnotation setDate(Date date) {
        this.date = date;
        return this;
    }
}
