package com.sudoplay.sudoxt.classloader;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IContainerClassLoader {

  Class<?> loadClass(String name, int flags) throws ClassNotFoundException;

}
