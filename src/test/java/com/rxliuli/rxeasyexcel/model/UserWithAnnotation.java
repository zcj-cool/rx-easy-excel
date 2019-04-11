package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.annotation.ExcelIgnore;
import com.rxliuli.rxeasyexcel.domain.convert.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Quding Ding
 * @since 2018/6/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithAnnotation {

  @ExcelField(columnName = "用户名")
  private String username;

  @ExcelField(columnName = "用户密码")
  private String passwd;

  @ExcelIgnore
  private String nickName;

  @ExcelField(columnName = "登录日期", convert = DateConverter.class)
  private Date date;
}
