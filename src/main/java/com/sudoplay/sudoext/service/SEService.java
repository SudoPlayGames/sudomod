package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.Plugin;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventPlugin;
import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.Meta;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  }

  public void dispose() {
    this.folderLifecycleEventPlugin.dispose();
  }

  public void reloadAllContainers() {
    this.containerMap.values().forEach(Container::reload);
  }

  public void reloadContainer(String id, boolean reloadDependents) {
    Container container;

    container = this.getContainer(id);

    if (reloadDependents) {
      this.reloadContainerWithDependents(container, new HashSet<>());

    } else {
      container.reload();
    }
  }

  private void reloadContainerWithDependents(Container container, Set<String> reloaded) {
    Meta meta;
    List<Dependency> dependentList;

    meta = container.getMeta();
    dependentList = meta.getDependencyContainer().getDependentList();

    container.reload();
    reloaded.add(meta.getId());

    for (Dependency dependent : dependentList) {

      if (!reloaded.contains(dependent.getId())) {
        this.reloadContainerWithDependents(this.getContainer(dependent.getId()), reloaded);
      }
    }
  }

  public <M extends Meta> M getMeta(String containerId, Class<M> metaClass) {
    return metaClass.cast(this.getContainer(containerId).getMeta());
  }

  public <T extends Plugin> PluginReference<T> getPlugin(String resourceString, Class<T> tClass) {
    return this.getPlugin(this.createResourceLocation(resourceString), tClass);
  }

  private <T extends Plugin> PluginReference<T> getPlugin(ResourceLocation resourceLocation, Class<T> tClass) {
    Container container;

    // TODO: swap out id if overridden

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
