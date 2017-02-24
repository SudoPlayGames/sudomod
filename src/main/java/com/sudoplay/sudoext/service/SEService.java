package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.candidate.Candidate;
import com.sudoplay.sudoext.candidate.ICandidateListExtractor;
import com.sudoplay.sudoext.candidate.ICandidateListProvider;
import com.sudoplay.sudoext.classloader.IClassLoaderFactoryProvider;
import com.sudoplay.sudoext.container.*;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventPlugin;
import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.IContainerListMetaLoader;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.sort.CyclicGraphException;

import java.util.*;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class SEService {

  private IFolderLifecycleEventPlugin folderLifecycleEventPlugin;

  private Map<String, Container> containerMap;
  private List<Container> containerList;
  private ResourceStringParser resourceStringParser;

  public SEService(
      IFolderLifecycleEventPlugin folderLifecycleEventPlugin,
      ICandidateListProvider candidateListProvider,
      ICandidateListExtractor candidateListExtractor,
      ICandidateListConverter candidateListConverter,
      IContainerListMetaLoader containerListMetaLoader,
      IContainerListValidator containerListValidator,
      IContainerSorter containerSorter,
      IClassLoaderFactoryProvider classLoaderFactoryProvider
  ) throws SEServiceInitializationException {

    this.folderLifecycleEventPlugin = folderLifecycleEventPlugin;

    List<Candidate> candidateList;
    List<Container> containerList;

    this.folderLifecycleEventPlugin.initialize();

    // look in the folder and build a list of folders and files that might be valid
    candidateList = candidateListProvider.getCandidateList(new ArrayList<>());

    // go through each candidate and extract compressed files to temporary directory
    // replace compressed candidates with temporary candidates
    candidateList = candidateListExtractor.extract(candidateList, new ArrayList<>());

    // create container list from the candidate list
    containerList = candidateListConverter.convert(candidateList, new ArrayList<>());

    // load each meta file
    containerList = containerListMetaLoader.load(containerList, new ArrayList<>());

    // validate each meta file, remove containers that don't validate
    containerList = containerListValidator.validate(containerList, new ArrayList<>());

    // sort containers
    try {
      containerList = containerSorter.sort(containerList, new ArrayList<>());

    } catch (CyclicGraphException e) {
      // exception is thrown when a cyclic dependency in the graph is detected
      throw new SEServiceInitializationException("Error initializing service", e);
    }

    // initialize container collections
    this.containerList = new ArrayList<>(containerList);
    this.containerMap = new HashMap<>();

    for (Container container : containerList) {
      // build the container map
      this.containerMap.put(
          container.getMeta().getId(),
          container
      );
    }

    // initialize containers
    for (Container container : containerList) {

      // init the container's class loader factory
      container.setClassLoaderFactory(
          classLoaderFactoryProvider.create(
              container,
              this.containerMap
          )
      );
    }

    this.resourceStringParser = new ResourceStringParser();
  }

  public void dispose() {
    this.folderLifecycleEventPlugin.dispose();
  }

  public void reloadAll() {
    this.containerList.forEach(Container::reload);
  }

  public void reload(String id, boolean reloadDependents) {
    Container container;

    container = this.getContainer(id);

    if (reloadDependents) {
      this.reloadWithDependents(container, new HashSet<>());

    } else {
      container.reload();
    }
  }

  private void reloadWithDependents(Container container, Set<String> reloaded) {
    Meta meta;
    List<Dependency> dependentList;

    meta = container.getMeta();
    dependentList = meta.getDependencyContainer().getDependentList();

    container.reload();
    reloaded.add(meta.getId());

    for (Dependency dependent : dependentList) {

      if (!reloaded.contains(dependent.getId())) {
        this.reloadWithDependents(this.getContainer(dependent.getId()), reloaded);
      }
    }
  }

  public <T extends Plugin> List<PluginReference<T>> getPluginList(
      Class<T> tClass,
      List<PluginReference<T>> store
  ) {

    for (Container container : this.containerList) {
      String plugin = container.getMeta().getPlugin();
      store.add(new PluginReference<>(tClass, plugin, container));
    }
    return store;
  }

  public <T extends Plugin> PluginReference<T> getPlugin(String id, Class<T> tClass) {
    Container container = this.getContainer(id);
    return new PluginReference<>(tClass, container.getMeta().getPlugin(), container);
  }

  public <T extends Plugin> PluginReference<T> get(String resourceString, Class<T> tClass) {

    // resource location looks like this:
    // <id>:<path>
    // ie. lss-core:com.sudoplay.lss.core.SomePlugin

    return this.get(this.createResourceLocation(resourceString), tClass);
  }

  private <T extends Plugin> PluginReference<T> get(ResourceLocation resourceLocation, Class<T> tClass) {
    Container container;

    // TODO: swap out id if overridden

    // lookup the container by id
    container = this.getContainer(resourceLocation.getId());

    return new PluginReference<>(tClass, resourceLocation.getResourceString(), container);
  }

  private ResourceLocation createResourceLocation(String resourceString) {
    ResourceLocation resourceLocation;

    try {
      resourceLocation = this.resourceStringParser.parse(resourceString);

    } catch (ResourceStringParseException e) {
      throw new IllegalArgumentException(String.format("Invalid resource string: %s", resourceString));
    }
    return resourceLocation;
  }

  private Container getContainer(String id) {
    Container container;

    container = this.containerMap.get(id);

    if (container == null) {
      throw new IllegalArgumentException(String.format("Unrecognized id: %s", id));
    }
    return container;
  }
}
