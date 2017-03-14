package com.sudoplay.sudoxt.classloader;

import org.codehaus.janino.Descriptor;
import org.codehaus.janino.IClass;
import org.codehaus.janino.IClassLoader;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import static com.sudoplay.sudoxt.classloader.SXClassLoader.DEPENDENCY;
import static com.sudoplay.sudoxt.classloader.SXClassLoader.JAR;

/**
 * Created by codetaylor on 3/10/2017.
 */
public class SXIClassLoader extends
    IClassLoader {

  private final IContainerClassLoader classLoader;

  public SXIClassLoader(IContainerClassLoader classLoader) {
    super(null);
    this.classLoader = classLoader;
    super.postConstruct();
  }

  @Override
  protected IClass findIClass(String descriptor) throws ClassNotFoundException {
    Class<?> clazz;

    try {
      clazz = this.classLoader.loadClass(Descriptor.toClassName(descriptor), (JAR | DEPENDENCY));

    } catch (ClassNotFoundException e) {

      if (e.getException() == null) {
        return null;

      } else {
        throw e;
      }
    }

    IClass result;

    try {
      Class<?> c = Class.forName("org.codehaus.janino.ReflectionIClass");
      Constructor<?> declaredConstructor = c.getDeclaredConstructor(Class.class, IClassLoader.class);

      AccessController.doPrivileged((PrivilegedExceptionAction<Void>) () -> {
        declaredConstructor.setAccessible(true);
        return null;
      });

      result = (IClass) declaredConstructor.newInstance(clazz, this);


    } catch (Exception e) {
      throw new ClassNotFoundException(descriptor, e);
    }

    this.defineIClass(result);
    return result;
  }
}
