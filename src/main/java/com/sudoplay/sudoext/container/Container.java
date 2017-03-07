package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.api.external.Plugin;
import com.sudoplay.sudoext.classloader.IClassLoaderFactory;
import com.sudoplay.sudoext.classloader.IContainerClassLoader;
import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegate;
import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegateFactory;
import com.sudoplay.sudoext.util.PreCondition;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class Container {

  private final String id;
  private final Path path;
  private final IContainerCacheFactory containerCacheFactory;
  private final ICallbackDelegateFactory callbackDelegateFactory;
  private final PluginInstantiator pluginInstantiator;
  private final Map<String, String> registeredPluginMap;
  private Set<String> preloadSet;

  private IClassLoaderFactory classLoaderFactory;
  private IContainerClassLoader classLoader;
  private IContainerCache cache;

  public Container(
      String id,
      Path path,
      IContainerCacheFactory containerCacheFactory,
      ICallbackDelegateFactory callbackDelegateFactory,
      PluginInstantiator pluginInstantiator,
      Map<String, String> registeredPluginMap,
      Set<String> preloadSet
  ) {
    this.id = id;
    this.path = path;
    this.containerCacheFactory = containerCacheFactory;
    this.callbackDelegateFactory = callbackDelegateFactory;
    this.pluginInstantiator = pluginInstantiator;
    this.registeredPluginMap = registeredPluginMap;
    this.preloadSet = preloadSet;
  }

  public String getId() {
    return this.id;
  }

  public Path getPath() {
    return this.path;
  }

  public Set<String> getPreloadSet() {
    return Collections.unmodifiableSet(this.preloadSet);
  }

  /* package */ void setClassLoaderFactory(IClassLoaderFactory classLoaderFactory) {
    this.classLoaderFactory = classLoaderFactory;
  }

  public ICallbackDelegate getCallbackDelegate() {
    return this.callbackDelegateFactory.create(this.classLoader);
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

    if ((obj = this.cache.get(resourceString)) != null) {
      return pClass.cast(obj);
    }

    P newPluginInstance = this.pluginInstantiator.instantiate(aClass);

    this.cache.put(resourceString, newPluginInstance);

    return newPluginInstance;
  }

  @Override
  public String toString() {
    return "Container[" + this.id + "]";
  }
}
