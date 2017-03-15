package com.sudoplay.sudoxt.service;

import com.sudoplay.sudoxt.container.Container;
import com.sudoplay.sudoxt.folder.IFolderLifecycleEventPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class SXService implements
    IPluginProvider,
    IRegisteredPluginListProvider,
    IPluginLifecycleService {

  private final IFolderLifecycleEventPlugin folderLifecycleEventPlugin;
  private final IPluginPreloader pluginPreLoader;
  private final Map<String, Container> containerMap;

  @SuppressWarnings("WeakerAccess")
  public SXService(
      IFolderLifecycleEventPlugin folderLifecycleEventPlugin,
      IPluginPreloader pluginPreLoader,
      Map<String, Container> containerMap
  ) throws SXServiceInitializationException {
    this.folderLifecycleEventPlugin = folderLifecycleEventPlugin;
    this.pluginPreLoader = pluginPreLoader;
    this.containerMap = containerMap;

    this.folderLifecycleEventPlugin.initialize();
    this.reload();
  }

  @Override
  public void preload(IPreloadMonitor monitor) {
    List<SXResourceLocation> preloadList = new ArrayList<>();

    for (Container container : this.containerMap.values()) {
      String id = container.getId();
      Set<String> preloadSet = container.getPreloadSet();

      preloadList.addAll(
          preloadSet
              .stream()
              .map(resource -> new SXResourceLocation(id, resource))
              .collect(Collectors.toList())
      );
    }

    this.pluginPreLoader.preload(monitor, preloadList, this);
  }

  @Override
  public void reload() {
    this.containerMap.values().forEach(Container::reload);
  }

  @Override
  public void dispose() {
    this.folderLifecycleEventPlugin.dispose();
  }

  @Override
  public <P> List<SXPluginReference<P>> getRegisteredPlugins(String name, Class<P> pClass) {
    return this.containerMap.values()
        .stream()
        .filter(container -> container.hasRegisteredPlugin(name))
        .map(container ->
            new SXPluginReference<>(
                pClass,
                container.getRegisteredPluginResourceString(name),
                container
            ))
        .collect(Collectors.toList());
  }

  @Override
  public <P> SXPluginReference<P> getPlugin(String resourceString, Class<P> tClass) {
    return this.getPlugin(this.createResourceLocation(resourceString), tClass);
  }

  @Override
  public <P> SXPluginReference<P> getPlugin(SXResourceLocation resourceLocation, Class<P> tClass) {
    Container container = this.getContainer(resourceLocation.getId());
    return new SXPluginReference<>(tClass, resourceLocation.getResource(), container);
  }

  private SXResourceLocation createResourceLocation(String resourceString) {
    SXResourceLocation resourceLocation;

    try {
      resourceLocation = new SXResourceLocation(resourceString);

    } catch (SXResourceStringParseException e) {
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
