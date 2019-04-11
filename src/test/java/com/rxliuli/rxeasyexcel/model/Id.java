package com.rxliuli.rxeasyexcel.model;

import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import lombok.Data;


/**
 * @author Quding Ding
 * @since 2018/8/2
 */
@Data
public class Id {

  @ExcelField(columnName = "id")
  private Long id;

}
