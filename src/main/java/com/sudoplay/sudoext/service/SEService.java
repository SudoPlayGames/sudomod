package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventPlugin;

import java.util.Map;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class SEService {

  private final IFolderLifecycleEventPlugin folderLifecycleEventPlugin;
  private final Map<String, Container> containerMap;

  @SuppressWarnings("WeakerAccess")
  public SEService(
      IFolderLifecycleEventPlugin folderLifecycleEventPlugin,
      Map<String, Container> containerMap
  ) throws SEServiceInitializationException {
    this.folderLifecycleEventPlugin = folderLifecycleEventPlugin;
    this.containerMap = containerMap;
  }

  public void initialize() throws SEServiceInitializationException {
    this.folderLifecycleEventPlugin.initialize();
    this.reloadAllContainers();
  }

  public void dispose() {
    this.folderLifecycleEventPlugin.dispose();
  }

  public void reloadAllContainers() {
    this.containerMap.values().forEach(Container::reload);
  }

  public void reloadContainer(String id) {
    Container container;
    container = this.getContainer(id);
    container.reload();
  }

  public <T extends Plugin> PluginReference<T> getPlugin(String resourceString, Class<T> tClass) {
    return this.getPlugin(this.createResourceLocation(resourceString), tClass);
  }

  private <T extends Plugin> PluginReference<T> getPlugin(ResourceLocation resourceLocation, Class<T> tClass) {
    Container container;

    container = this.getContainer(resourceLocation.getId());
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
