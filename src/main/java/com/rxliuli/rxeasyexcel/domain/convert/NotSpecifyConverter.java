package com.rxliuli.rxeasyexcel.domain.convert;


import com.rxliuli.rxeasyexcel.ExcelException;

/**
 * 未指定的转换器
 *
 * @author rxliuli
 */
public class NotSpecifyConverter extends DefaultConverter {
  @Override
  public String to(Object o) {
    throw new ExcelException("你不能使用未指定的转换器");
  }

  @Override
  public Object from(String s) {
    throw new ExcelException("你不能使用未指定的转换器");
  }
}
