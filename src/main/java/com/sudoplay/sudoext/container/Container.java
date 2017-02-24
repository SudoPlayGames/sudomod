package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.classloader.IClassLoader;
import com.sudoplay.sudoext.classloader.IClassLoaderFactory;
import com.sudoplay.sudoext.meta.Meta;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class Container {

  private Path path;
  private IContainerCacheFactory containerCacheFactory;

  private Meta meta;
  private IClassLoaderFactory classLoaderFactory;
  private IClassLoader classLoader;
  private IContainerCache cache;

  public Container(
      Path path,
      IContainerCacheFactory containerCacheFactory
  ) {
    this.path = path;
    this.containerCacheFactory = containerCacheFactory;
  }

  public Path getPath() {
    return this.path;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }

  public Meta getMeta() {
    return this.meta;
  }

  public void setClassLoaderFactory(IClassLoaderFactory classLoaderFactory) {
    this.classLoaderFactory = classLoaderFactory;
    this.reload();
  }

  public void reload() {
    this.cache = this.containerCacheFactory.create();
    this.classLoader = this.classLoaderFactory.create(this);
  }

  public IClassLoader getClassLoader() {
    return this.classLoader;
  }

  public <T extends Plugin> T get(String resourceString, Class<T> tClass) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {
    return this.get(resourceString, tClass, true);
  }

  public <T extends Plugin> T get(String resourceString, Class<T> tClass, boolean useDependencyCheck) throws
      ClassNotFoundException, IllegalAccessException, InstantiationException {

    Class<T> aClass;

    if (useDependencyCheck) {
      //noinspection unchecked
      aClass = (Class<T>) this.classLoader.loadClass(resourceString);

    } else {
      //noinspection unchecked
      aClass = (Class<T>) this.classLoader.loadClassWithoutDependencyCheck(resourceString);
    }

    Object obj;

    if ((obj = this.cache.get(aClass)) != null) {
      return tClass.cast(obj);
    }

    T newPluginInstance = aClass.newInstance();

    this.cache.put(aClass, newPluginInstance);

    return newPluginInstance;
  }

  @Override
  public String toString() {
    return "Container[" + (this.meta == null ? "?" : this.meta.getId()) + "]";
  }
}
