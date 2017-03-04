package com.sudoplay.sudoext.classloader.intercept;

import com.sudoplay.sudoext.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 2/23/2017.
 */
public class DefaultClassInterceptor implements
    IClassInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultClassInterceptor.class);

  private Container container;
  private Set<String> classNameSet;
  private Map<String, IClassInterceptProcessor> processorMap;

  public DefaultClassInterceptor(Container container, ClassIntercept[] classIntercepts) {
    this.container = container;
    this.classNameSet = new HashSet<>();
    this.processorMap = new HashMap<>();

    for (ClassIntercept classIntercept : classIntercepts) {
      Class<?> interceptClass = classIntercept.getInterceptClass();
      this.classNameSet.add(interceptClass.getName());
      this.processorMap.put(interceptClass.getName(), classIntercept.getClassInterceptProcessor());
    }
  }

  @Override
  public boolean canIntercept(String name) {
    return this.classNameSet.contains(name);
  }

  @Override
  public void intercept(Class<?> aClass) {
    IClassInterceptProcessor processor = this.processorMap.get(aClass.getName());

    if (processor == null) {
      LOG.error("No class intercept processor registered for class [{}]", aClass.getName());
      return;
    }

    processor.process(aClass, this.container);
  }
}
