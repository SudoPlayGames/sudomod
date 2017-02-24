package com.sudoplay.sudoext.classloader;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IClassLoader {

  Class<?> loadClass(String name) throws ClassNotFoundException;

  Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException;
}
