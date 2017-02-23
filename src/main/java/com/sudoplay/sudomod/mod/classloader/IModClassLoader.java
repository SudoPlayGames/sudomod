package com.sudoplay.sudomod.mod.classloader;

/**
 * Created by codetaylor on 2/22/2017.
 */
public interface IModClassLoader {

  Class<?> loadClass(String name) throws ClassNotFoundException;

  Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException;
}
