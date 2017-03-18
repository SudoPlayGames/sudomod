package com.sudoplay.sudoxt.container;

import com.sudoplay.sudoxt.classloader.IClassLoaderFactory;
import com.sudoplay.sudoxt.classloader.IContainerClassLoader;
import com.sudoplay.sudoxt.classloader.asm.callback.ICallbackDelegate;
import com.sudoplay.sudoxt.classloader.asm.callback.ICallbackDelegateFactory;
import com.sudoplay.sudoxt.util.PreCondition;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.sudoplay.sudoxt.classloader.SXClassLoader.*;

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
  private ContainerOverrideProvider containerOverrideProvider;

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

  /* package */ void setContainerOverrideProvider(ContainerOverrideProvider containerOverrideProvider) {
    this.containerOverrideProvider = containerOverrideProvider;
  }

  public ICallbackDelegate getCallbackDelegate() {
    return this.callbackDelegateFactory.create(this.classLoader);
  }

  public void reload() {
    this.cache = this.containerCacheFactory.create();
    this.classLoader = this.classLoaderFactory.create();
  }

  public Class<?> loadClass(String name, int flags) throws ClassNotFoundException {
    return this.classLoader.loadClass(name, flags);
  }

  public boolean hasRegisteredPlugin(String name) {
    return this.registeredPluginMap.containsKey(name);
  }

  public String getRegisteredPluginResourceString(String name) {
    return this.registeredPluginMap.get(name);
  }

  public <P> P getPlugin(String resourceString, Class<P> pClass) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {
    return this.getPlugin(
        PreCondition.notNull(resourceString),
        PreCondition.notNull(pClass),
        true
    );
  }

  private <P> P getPlugin(String resourceString, Class<P> pClass, boolean useDependencyCheck) throws
      ClassNotFoundException, IllegalAccessException, InstantiationException {

    P plugin = this.containerOverrideProvider.getPlugin(resourceString, pClass);

    if (plugin == null) {
      Class<P> aClass;

      if (useDependencyCheck) {
        //noinspection unchecked
        aClass = (Class<P>) this.classLoader.loadClass(resourceString, (PARENT | JAR | SOURCE | DEPENDENCY));

      } else {
        //noinspection unchecked
        aClass = (Class<P>) this.classLoader.loadClass(resourceString, (PARENT | JAR | SOURCE));
      }

      Object obj;

      if ((obj = this.cache.get(resourceString)) != null) {
        return pClass.cast(obj);
      }

      plugin = this.pluginInstantiator.instantiate(aClass);

      this.cache.put(resourceString, plugin);
    }

    return plugin;
  }

  @Override
  public String toString() {
    return "Container[" + this.id + "]";
  }
}
