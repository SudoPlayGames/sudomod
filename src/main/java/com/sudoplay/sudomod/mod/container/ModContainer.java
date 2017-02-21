package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.ModClassLoader;
import com.sudoplay.sudomod.mod.ModClassLoaderFactory;
import com.sudoplay.sudomod.mod.info.ModInfo;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModContainer {

  private Path path;
  private ModInfo modInfo;
  private ModClassLoaderFactory modClassLoaderFactory;
  private ModClassLoader modClassLoader;
  private Map<Class<?>, Object> classMap;

  public ModContainer(Path path) {
    this.path = path;
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
    this.modClassLoader = this.modClassLoaderFactory.create();
    this.classMap = new HashMap<>();
  }

  public <T> T get(String resourceString) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException {
    //noinspection unchecked
    Class<?> aClass = this.modClassLoader.loadClass(resourceString);

    if (this.classMap.containsKey(aClass)) {
      //noinspection unchecked
      return (T) this.classMap.get(aClass);
    }

    Object newInstance = aClass.newInstance();
    this.classMap.put(aClass, newInstance);
    //noinspection unchecked
    return (T) newInstance;
  }
}
