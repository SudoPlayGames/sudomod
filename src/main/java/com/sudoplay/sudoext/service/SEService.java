package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class SEService {

  private final IFolderLifecycleEventPlugin folderLifecycleEventPlugin;
  private final PluginPreLoader pluginPreLoader;
  private final Map<String, Container> containerMap;

  @SuppressWarnings("WeakerAccess")
  public SEService(
      IFolderLifecycleEventPlugin folderLifecycleEventPlugin,
      PluginPreLoader pluginPreLoader,
      Map<String, Container> containerMap
  ) throws SEServiceInitializationException {
    this.folderLifecycleEventPlugin = folderLifecycleEventPlugin;
    this.pluginPreLoader = pluginPreLoader;
    this.containerMap = containerMap;
  }

  /* package */ void initializeFolders() throws SEServiceInitializationException {
    this.folderLifecycleEventPlugin.initialize();
  }

  public void disposeFolders() {
    this.folderLifecycleEventPlugin.dispose();
  }

  public void preload(IPreloadMonitor monitor) {
    List<ResourceLocation> preloadList = new ArrayList<>();

    for (Container container : this.containerMap.values()) {
      String id = container.getId();
      Set<String> preloadSet = container.getPreloadSet();

      preloadList.addAll(
          preloadSet
              .stream()
              .map(resource -> new ResourceLocation(id, resource))
              .collect(Collectors.toList())
      );
    }

    this.pluginPreLoader.preload(monitor, preloadList, this);
  }

  public void reload() {
    this.containerMap.values().forEach(Container::reload);
  }

  public <P extends Plugin> List<PluginReference<P>> getRegisteredPlugins(String name, Class<P> pClass) {
    return this.containerMap.values()
        .stream()
        .filter(container -> container.hasRegisteredPlugin(name))
        .map(container ->
            new PluginReference<>(
                pClass,
                container.getRegisteredPluginResourceString(name),
                container
            ))
        .collect(Collectors.toList());
  }

  public <P extends Plugin> PluginReference<P> getPlugin(String resourceString, Class<P> tClass) {
    return this.getPlugin(this.createResourceLocation(resourceString), tClass);
  }

  public <P extends Plugin> PluginReference<P> getPlugin(ResourceLocation resourceLocation, Class<P> tClass) {
    Container container = this.getContainer(resourceLocation.getId());
    return new PluginReference<>(tClass, resourceLocation.getResource(), container);
  }

  private ResourceLocation createResourceLocation(String resourceString) {
    ResourceLocation resourceLocation;

    try {
      resourceLocation = new ResourceLocation(resourceString);

    } catch (ResourceStringParseException e) {
      throw new IllegalArgumentException(String.format("Invalid resource string: %s", resourceString));
    }
    return resourceLocation;
  }

  private Container getContainer(String containerId) {
    Container container;

    container = this.containerMap.get(containerId);

    if (container == null) {
      throw new IllegalArgumentException(String.format("Unrecognized containerId: %s", containerId));
    }
    return container;
  }
}
