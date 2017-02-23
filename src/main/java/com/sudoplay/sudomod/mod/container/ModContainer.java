package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.api.core.ILoggingAPIProviderFactory;
import com.sudoplay.sudomod.mod.classloader.IModClassLoader;
import com.sudoplay.sudomod.mod.classloader.ModClassLoaderFactory;
import com.sudoplay.sudomod.mod.info.ModInfo;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModContainer {

  private Path path;
  private IModContainerCacheFactory modContainerCacheFactory;
  private ILoggingAPIProviderFactory loggingAPIProviderFactory;

  private ModInfo modInfo;
  private ModClassLoaderFactory modClassLoaderFactory;
  private IModClassLoader modClassLoader;
  private IModContainerCache cache;

  public ModContainer(
      Path path,
      IModContainerCacheFactory modContainerCacheFactory,
      ILoggingAPIProviderFactory loggingAPIProviderFactory
  ) {
    this.path = path;
    this.modContainerCacheFactory = modContainerCacheFactory;
    this.loggingAPIProviderFactory = loggingAPIProviderFactory;
  }

  public Path getPath() {
    return this.path;
  }

  public void setModInfo(ModInfo modInfo) {
    this.modInfo = modInfo;
  }

  public ModInfo getModInfo() {
    return this.modInfo;
  }

  public void setModClassLoaderFactory(ModClassLoaderFactory modClassLoaderFactory) {
    this.modClassLoaderFactory = modClassLoaderFactory;
    this.reload();
  }

  public void reload() {
    this.cache = this.modContainerCacheFactory.create();
    this.modClassLoader = this.modClassLoaderFactory.create();

    try {
      this.getModPlugin(ModPlugin.class).initialize(
          this.loggingAPIProviderFactory.create(this.modInfo.getId())
      );

    } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
      throw new ModPluginInitializationException(String.format(
          "Error initializing mod plugin [%s]", this.getModInfo().getModPlugin()
      ), e);
    }
  }

  public IModClassLoader getModClassLoader() {
    return this.modClassLoader;
  }

  public <T extends ModPlugin> T getModPlugin(Class<T> tClass) throws IllegalAccessException,
      InstantiationException, ClassNotFoundException {
    return this.get(this.getModInfo().getModPlugin(), tClass);
  }

  public <T> T get(String resourceString, Class<T> tClass) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException {
    return this.get(resourceString, tClass, true);
  }

  public <T> T get(String resourceString, Class<T> tClass, boolean useDependencyCheck) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {

    Class<T> aClass;

    if (useDependencyCheck) {
      //noinspection unchecked
      aClass = (Class<T>) this.modClassLoader.loadClass(resourceString);

    } else {
      //noinspection unchecked
      aClass = (Class<T>) this.modClassLoader.loadClassWithoutDependencyCheck(resourceString);
    }

    Object obj;

    if ((obj = this.cache.get(aClass)) != null) {
      return tClass.cast(obj);
    }

    T newInstance = aClass.newInstance();

    this.cache.put(aClass, newInstance);
    return newInstance;
  }

  @Override
  public String toString() {
    return "ModContainer[" + (this.modInfo == null ? "?" : this.modInfo.getId()) + "]";
  }
}
