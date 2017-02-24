package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class StaticFieldClassInterceptProcessor implements
    IClassInterceptProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(StaticFieldClassInterceptProcessor.class);

  @SuppressWarnings("WeakerAccess")
  public interface IFieldValueProvider {
    Object getFieldValue(Container container);
  }

  private String fieldName;
  private IFieldValueProvider fieldValueProvider;

  public StaticFieldClassInterceptProcessor(
      String fieldName,
      IFieldValueProvider fieldValueProvider
  ) {
    this.fieldName = fieldName;
    this.fieldValueProvider = fieldValueProvider;
  }

  @Override
  public void process(Class<?> interceptedClass, Container container) {
    Field field;
    Object fieldValue;

    try {
      field = interceptedClass.getDeclaredField(this.fieldName);

    } catch (NoSuchFieldException e) {
      LOG.error(
          "Can't find declared field [{}] in intercepted class [{}]",
          this.fieldName,
          interceptedClass.getName()
      );
      return;
    }

    field.setAccessible(true);

    if (!Modifier.isStatic(field.getModifiers())) {
      LOG.error(
          "Non-static field [{}] in intercepted class [{}] must be static",
          field.getName(),
          interceptedClass.getName()
      );
      return;
    }

    fieldValue = this.fieldValueProvider.getFieldValue(container);

    try {
      field.set(null, fieldValue);

    } catch (IllegalAccessException e) {
      LOG.error(
          "Error while trying to set field [{}] in intercepted class [{}] to value [{}]",
          field.getName(),
          interceptedClass.getName(),
          fieldValue
      );
    }
  }
}
