package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Quding Ding
 * @since 2018/6/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @ExcelField
  private String username;
  @ExcelField
  private String passwd;


  @Override
  public String toString() {
    return "User{" +
        "username='" + username + '\'' +
        ", passwd='" + passwd + '\'' +
        '}';
  }
}
