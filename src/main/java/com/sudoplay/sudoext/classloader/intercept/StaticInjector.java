package com.sudoplay.sudoext.classloader.intercept;

/**
 * Created by codetaylor on 3/7/2017.
 */
public class StaticInjector<V> {

  private Class<V> type;
  private String name;
  private IStaticFieldValueProvider<V> valueProvider;

  public StaticInjector(Class<V> type, IStaticFieldValueProvider<V> valueProvider) {
    this(type, "", valueProvider);
  }

  public StaticInjector(Class<V> type, String name, IStaticFieldValueProvider<V> valueProvider) {
    this.type = type;
    this.name = name;
    this.valueProvider = valueProvider;
  }

  public Class<V> getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  /* package */ IStaticFieldValueProvider<V> getValueProvider() {
    return this.valueProvider;
  }
}
