package com.sudoplay.sudoxt.classloader.intercept;

import com.sudoplay.sudoxt.container.Container;

/**
 * Created by codetaylor on 3/7/2017.
 */
public interface IStaticFieldValueProvider<V> {

  V getValue(Container container);
}
