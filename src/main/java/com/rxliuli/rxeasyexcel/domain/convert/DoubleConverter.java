package com.rxliuli.rxeasyexcel.domain.convert;

/**
 * @author rxliuli
 */
public class DoubleConverter implements IConverter<Double> {
  @Override
  public String to(Double aDouble) {
    return Double.toString(aDouble);
  }

  @Override
  public Double from(String s) {
    return Double.valueOf(s);
  }
}
