package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.classloader.ISEClassLoader;
import com.sudoplay.sudoext.classloader.IClassLoaderFactory;
import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegate;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class Container {

  private String id;
  private IContainerCacheFactory containerCacheFactory;
  private IClassLoaderFactory classLoaderFactory;
  private PluginInstantiator pluginInstantiator;
  private ICallbackDelegate callbackDelegate;
  private ISEClassLoader classLoader;
  private IContainerCache cache;

  public Container(
      String id,
      IContainerCacheFactory containerCacheFactory,
      PluginInstantiator pluginInstantiator,
      ICallbackDelegate callbackDelegate
  ) {
    this.id = id;
    this.containerCacheFactory = containerCacheFactory;
    this.pluginInstantiator = pluginInstantiator;
    this.callbackDelegate = callbackDelegate;
  }

  public String getId() {
    return this.id;
  }

  public ICallbackDelegate getCallbackDelegate() {
    return this.callbackDelegate;
  }

  /* package */ void setClassLoaderFactory(IClassLoaderFactory classLoaderFactory) {
    this.classLoaderFactory = classLoaderFactory;
  }

  public void reload() {
    this.cache = this.containerCacheFactory.create();
    this.classLoader = this.classLoaderFactory.create();
  }

  public Class<?> loadClassWithoutDependencyCheck(String name) throws ClassNotFoundException {
    return this.classLoader.loadClassWithoutDependencyCheck(name);
  }

  public <P extends Plugin> P get(String resourceString, Class<P> tClass) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {
    return this.get(resourceString, tClass, true);
  }

  public <P extends Plugin> P get(String resourceString, Class<P> pClass, boolean useDependencyCheck) throws
      ClassNotFoundException, IllegalAccessException, InstantiationException {

    Class<P> aClass;

    if (useDependencyCheck) {
      //noinspection unchecked
      aClass = (Class<P>) this.classLoader.loadClass(resourceString);

    } else {
      //noinspection unchecked
      aClass = (Class<P>) this.classLoader.loadClassWithoutDependencyCheck(resourceString);
    }

    Object obj;

    if ((obj = this.cache.get(aClass)) != null) {
      return pClass.cast(obj);
    }

    P newPluginInstance = this.pluginInstantiator.instantiate(aClass);

    this.cache.put(aClass, newPluginInstance);

    return newPluginInstance;
  }

  @Override
  public String toString() {
    return "Container[" + this.id + "]";
  }
}
