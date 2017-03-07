package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.transform.NoOpByteCodeTransformer;
import com.sudoplay.sudoext.util.InputStreamByteArrayConverter;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class JarClassLoaderTest {

  private class PassThruClassLoader extends
      ClassLoader implements
      IContainerClassLoader {

    public PassThruClassLoader(ClassLoader parent) {
      super(parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      throw new ClassNotFoundException();
    }

    @Override
    public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
      throw new ClassNotFoundException();
    }
  }

  @Test
  public void loadClassWithoutDependencyCheckShouldDelegateToParentWhenParentIsContainerClassLoader() throws Exception {

    PassThruClassLoader containerClassLoader = spy(new PassThruClassLoader(null));

    JarClassLoader jarClassLoader = new JarClassLoader(null, new URL[0], containerClassLoader, null, null);

    try {
      jarClassLoader.loadClassWithoutDependencyCheck("MissingClass");
      Assert.fail("Should throw ClassNotFoundException");

    } catch (ClassNotFoundException e) {
      // expected
    }

    verify(containerClassLoader, times(1)).loadClassWithoutDependencyCheck("MissingClass");
  }

  @Test
  public void findClassShouldInvokeByteCodeTransformer() throws Exception {

    NoOpByteCodeTransformer byteCodeTransformer = mock(NoOpByteCodeTransformer.class);
    when(byteCodeTransformer.transform(any())).thenThrow(ClassNotFoundException.class);

    InputStreamByteArrayConverter inputStreamByteArrayConverter = new InputStreamByteArrayConverter();

    JarClassLoader jarClassLoader = new JarClassLoader(null, new URL[]{Paths.get("src/test/resources").toUri().toURL()},
        null, byteCodeTransformer, inputStreamByteArrayConverter);

    try {
      jarClassLoader.loadClass("SimpleTestClass2");

    } catch (ClassNotFoundException e) {
      // expected
    }

    verify(byteCodeTransformer, times(1)).transform(any());
  }
}