package com.sudoplay.sudoext.classloader;

import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.asm.transform.NoOpByteCodeTransformer;
import org.codehaus.janino.ClassLoaderIClassLoader;
import org.codehaus.janino.JavaSourceIClassLoader;
import org.codehaus.janino.util.resource.PathResourceFinder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class SourceClassLoaderTest {

  private class PassThruClassLoader extends
      ClassLoader implements
      IContainerClassLoader {

    public PassThruClassLoader(ClassLoader parent) {
      super(parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      return this.getParent().loadClass(name);
    }

    @Override
    public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
      throw new ClassNotFoundException();
    }
  }

  @Test
  public void loadClassWithoutDependencyCheckShouldDelegateToParentWhenParentIsContainerClassLoader() throws
      ClassNotFoundException {

    PassThruClassLoader containerClassLoader = spy(new PassThruClassLoader(this.getClass().getClassLoader()));

    SourceClassLoader sourceClassLoader = new SourceClassLoader(
        null,
        containerClassLoader,
        new JavaSourceIClassLoader(
            new PathResourceFinder(new File[]{Paths.get("src/test/resources").toFile()}),
            null,
            new ClassLoaderIClassLoader(containerClassLoader)
        ),
        new NoOpByteCodeTransformer()
    );

    try {
      sourceClassLoader.loadClassWithoutDependencyCheck("MissingClass");
      Assert.fail("Should throw ClassNotFoundException");

    } catch (ClassNotFoundException e) {
      // expected
    }

    verify(containerClassLoader, times(1)).loadClassWithoutDependencyCheck(anyString());
  }

  @Test
  public void findClassShouldInvokeByteCodeTransformer() throws Exception {

    IByteCodeTransformer byteCodeTransformer = mock(NoOpByteCodeTransformer.class);
    when(byteCodeTransformer.transform(any())).thenCallRealMethod();

    SourceClassLoader sourceClassLoader = new SourceClassLoader(
        null,
        this.getClass().getClassLoader(),
        new JavaSourceIClassLoader(
            new PathResourceFinder(new File[]{Paths.get("src/test/resources").toFile()}),
            null,
            new ClassLoaderIClassLoader(this.getClass().getClassLoader())
        ),
        byteCodeTransformer
    );

    sourceClassLoader.loadClass("SimpleTestClass");

    verify(byteCodeTransformer, times(1)).transform(any());
  }

}