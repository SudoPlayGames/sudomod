package com.sudoplay.sudoxt.classloader;

import java.io.File;

/**
 * Created by codetaylor on 4/13/2017.
 */
public interface ICompiler {

  void onClassLoaderInit(SXClassLoader classLoader, File[] sourcePath);

  byte[] compile(String name) throws ClassNotFoundException;

}
