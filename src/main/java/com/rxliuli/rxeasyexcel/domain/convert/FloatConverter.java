package com.rxliuli.rxeasyexcel.domain.convert;

/**
 * @author rxliuli
 */
public class FloatConverter implements IConverter<Float> {
  @Override
  public String to(Float aFloat) {
    return Float.toString(aFloat);
  }

  @Override
  public Float from(String s) {
    return Float.valueOf(s);
  }
}
