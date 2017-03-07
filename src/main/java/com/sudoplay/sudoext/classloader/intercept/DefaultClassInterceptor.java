package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class DefaultClassInterceptor implements
    IClassInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultClassInterceptor.class);

  private Container container;
  private Map<Class<?>, Map<String, IStaticFieldValueProvider<?>>> valueProviderMap;

  public DefaultClassInterceptor(
      Container container,
      StaticInjector<?>[] staticInjectors
  ) {
    this.container = container;
    this.valueProviderMap = new HashMap<>();
    this.registerInjectors(staticInjectors);
  }

  private void registerInjectors(StaticInjector<?>[] staticInjectors) {
    Class<?> injectorType;
    String injectorName;
    Map<String, IStaticFieldValueProvider<?>> providerMap;

    for (StaticInjector<?> staticInjector : staticInjectors) {
      injectorType = staticInjector.getType();
      injectorName = staticInjector.getName();

      providerMap = this.valueProviderMap
          .get(injectorType);

      if (providerMap == null) {
        providerMap = new HashMap<>();
        this.valueProviderMap.put(injectorType, providerMap);
      }

      if (providerMap.containsKey(injectorName)) {
        throw new IllegalArgumentException(String.format(
            "Attempt to register duplicate static injector for type [%s] named [%s]",
            injectorType,
            injectorName
        ));
      }

      providerMap.put(injectorName, staticInjector.getValueProvider());
    }
  }

  @Override
  public void intercept(Class<?> aClass) {

    Field[] declaredFields = aClass.getDeclaredFields();

    for (Field field : declaredFields) {
      InjectStaticField declaredAnnotation = field.getDeclaredAnnotation(InjectStaticField.class);

      if (declaredAnnotation != null) {

        if (!Modifier.isStatic(field.getModifiers())) {
          LOG.error(
              "Non-static field [{}] in class [{}] marked for static injection",
              field.getName(),
              aClass.getName()
          );
          continue;
        }

        Class<?> fieldType = field.getType();
        String injectedName = declaredAnnotation.value();

        Map<String, IStaticFieldValueProvider<?>> providerMap = this.valueProviderMap.get(fieldType);

        if (providerMap == null) {
          LOG.error(
              "No static injector registered for type [{}]",
              fieldType.getName()
          );
          continue;
        }

        IStaticFieldValueProvider<?> valueProvider = providerMap.get(injectedName);

        if (valueProvider == null) {
          LOG.error(
              "No static injector registered for type [{}] with name [{}]",
              fieldType.getName(),
              injectedName
          );
          continue;
        }

        Object value = valueProvider.getValue(this.container);

        try {
          field.setAccessible(true);
          field.set(null, value);
          field.setAccessible(false);

        } catch (IllegalAccessException e) {
          LOG.error(
              "Error while trying to set field [{}] in class [{}] to value [{}]",
              field.getName(),
              aClass.getName(),
              value,
              e
          );
        }
      }
    }
  }
}
