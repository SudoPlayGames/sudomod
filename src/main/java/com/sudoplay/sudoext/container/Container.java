package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.classloader.IClassLoaderFactory;
import com.sudoplay.sudoext.classloader.IContainerClassLoader;
import com.sudoplay.sudoext.util.PreCondition;

import java.util.Map;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class Container {

  private final String id;
  private final IContainerCacheFactory containerCacheFactory;
  private final PluginInstantiator pluginInstantiator;
  private final Map<String, String> registeredPluginMap;

  private IClassLoaderFactory classLoaderFactory;
  private IContainerClassLoader classLoader;
  private IContainerCache cache;

  public Container(
      String id,
      IContainerCacheFactory containerCacheFactory,
      PluginInstantiator pluginInstantiator,
      Map<String, String> registeredPluginMap
  ) {
    this.id = id;
    this.containerCacheFactory = containerCacheFactory;
    this.pluginInstantiator = pluginInstantiator;
    this.registeredPluginMap = registeredPluginMap;
  }

  public String getId() {
    return this.id;
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

  public boolean hasRegisteredPlugin(String name) {
    return this.registeredPluginMap.containsKey(name);
  }

  public String getRegisteredPluginResourceString(String name) {
    return this.registeredPluginMap.get(name);
  }

  public <P extends Plugin> P getRegisteredPlugin(String name, Class<P> pClass) throws IllegalAccessException,
      InstantiationException, ClassNotFoundException {
    return this.getPlugin(
        PreCondition.notNull(this.registeredPluginMap.get(name)),
        PreCondition.notNull(pClass)
    );
  }

  public <P extends Plugin> P getPlugin(String resourceString, Class<P> pClass) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {
    return this.getPlugin(
        PreCondition.notNull(resourceString),
        PreCondition.notNull(pClass),
        true
    );
  }

  private <P extends Plugin> P getPlugin(String resourceString, Class<P> pClass, boolean useDependencyCheck) throws
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
