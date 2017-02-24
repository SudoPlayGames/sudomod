package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.container.Container;

import java.util.Map;

/**
 * Created by codetaylor on 2/23/2017.
 */
public interface IClassLoaderFactoryProvider {

  IClassLoaderFactory create(Container container, Map<String, Container> containerMap);
}
