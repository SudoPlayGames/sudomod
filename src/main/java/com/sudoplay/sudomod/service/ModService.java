package com.sudoplay.sudomod.service;

import com.sudoplay.sudomod.PluginWrapper;
import com.sudoplay.sudomod.folder.IFolderLifecycleEventPlugin;
import com.sudoplay.sudomod.mod.candidate.IModCandidateListExtractor;
import com.sudoplay.sudomod.mod.candidate.IModCandidateListProvider;
import com.sudoplay.sudomod.mod.candidate.ModCandidate;
import com.sudoplay.sudomod.mod.classloader.ModClassLoaderFactory;
import com.sudoplay.sudomod.mod.container.*;
import com.sudoplay.sudomod.mod.info.IModContainerListInfoLoader;
import com.sudoplay.sudomod.mod.info.ModDependency;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.security.IClassFilter;
import com.sudoplay.sudomod.sort.CyclicGraphException;

import java.util.*;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModService {

  private IFolderLifecycleEventPlugin folderLifecycleEventPlugin;

  private Map<String, ModContainer> modContainerMap;
  private List<ModContainer> modContainerList;
  private ResourceStringParser resourceStringParser;

  public ModService(
      IFolderLifecycleEventPlugin folderLifecycleEventPlugin,
      IModCandidateListProvider modCandidateListProvider,
      IModCandidateListExtractor modCandidateListExtractor,
      IModCandidateListConverter modCandidateListConverter,
      IModContainerListInfoLoader modContainerListInfoLoader,
      IModContainerListValidator modContainerListValidator,
      IModContainerSorter modContainerSorter,
      IClassFilter[] classFilters
  ) throws ModServiceInitializationException {

    this.folderLifecycleEventPlugin = folderLifecycleEventPlugin;

    List<ModCandidate> modCandidateList;
    List<ModContainer> modContainerList;

    this.folderLifecycleEventPlugin.initialize();

    // look in the mod folder and build a list of folders and files that might be valid mods
    modCandidateList = modCandidateListProvider.getModCandidateList(new ArrayList<>());

    // go through each mod candidate and extract compressed files to temporary directory
    // replace compressed candidates with temporary candidates
    modCandidateList = modCandidateListExtractor.extract(modCandidateList, new ArrayList<>());

    // create mod container list from mod candidate list
    modContainerList = modCandidateListConverter.convert(modCandidateList, new ArrayList<>());

    // load each mod's info file
    modContainerList = modContainerListInfoLoader.load(modContainerList, new ArrayList<>());

    // validate each mod's mod-info, remove mods that don't validate
    modContainerList = modContainerListValidator.validate(modContainerList, new ArrayList<>());

    // sort mod containers
    try {
      modContainerList = modContainerSorter.sort(modContainerList, new ArrayList<>());

    } catch (CyclicGraphException e) {
      // exception is thrown when a cyclic dependency in the mod graph is detected
      throw new ModServiceInitializationException("Error initializing mod service", e);
    }

    // initialize mod container collections
    this.modContainerList = new ArrayList<>(modContainerList);
    this.modContainerMap = new HashMap<>();

    for (ModContainer modContainer : modContainerList) {
      // build the container map
      this.modContainerMap.put(
          modContainer.getModInfo().getId(),
          modContainer
      );
    }

    // initialize mod containers
    for (ModContainer modContainer : modContainerList) {
      ModInfo modInfo = modContainer.getModInfo();

      // build the dependency list
      List<ModContainer> modDependencyList = new ArrayList<>();

      for (ModDependency modDependency : modInfo.getModDependencyContainer().getDependencyList()) {
        String modDependencyId = modDependency.getId();
        ModContainer dependency = this.modContainerMap.get(modDependencyId);

        if (dependency != null) {
          modDependencyList.add(dependency);
        }
      }

      // init the mod container's mod class loader factory
      modContainer.setModClassLoaderFactory(
          new ModClassLoaderFactory(
              modContainer.getPath(),
              modInfo.getJarFileList(),
              modDependencyList,
              classFilters
          )
      );

    }

    this.resourceStringParser = new ResourceStringParser();
  }

  public void dispose() {
    this.folderLifecycleEventPlugin.dispose();
  }

  public void reloadAll() {
    this.modContainerList.forEach(ModContainer::reload);
  }

  public void reload(String modId, boolean reloadDependents) {
    ModContainer modContainer;

    modContainer = this.getModContainer(modId);

    if (reloadDependents) {
      this.reloadWithDependents(modContainer, new HashSet<>());

    } else {
      modContainer.reload();
    }
  }

  private void reloadWithDependents(ModContainer modContainer, Set<String> reloaded) {
    ModInfo modInfo;
    List<ModDependency> dependentList;

    modInfo = modContainer.getModInfo();
    dependentList = modInfo.getModDependencyContainer().getDependentList();

    modContainer.reload();
    reloaded.add(modInfo.getId());

    for (ModDependency dependent : dependentList) {

      if (!reloaded.contains(dependent.getId())) {
        this.reloadWithDependents(this.getModContainer(dependent.getId()), reloaded);
      }
    }
  }

  public <T extends ModPlugin> List<PluginWrapper<T>> getModPluginList(
      Class<T> tClass,
      List<PluginWrapper<T>> store
  ) {

    for (ModContainer modContainer : this.modContainerList) {
      String modPlugin = modContainer.getModInfo().getModPlugin();
      store.add(new PluginWrapper<>(tClass, modPlugin, modContainer));
    }
    return store;
  }

  public <T extends ModPlugin> PluginWrapper<T> getModPlugin(String modId, Class<T> tClass) {
    ModContainer modContainer = this.getModContainer(modId);
    return new PluginWrapper<>(tClass, modContainer.getModInfo().getModPlugin(), modContainer);
  }

  public <T> PluginWrapper<T> get(String resourceString, Class<T> tClass) {

    // resource location looks like this:
    // <mod-id>:<path>
    // ie. lss-core:com.sudoplay.lss.mod.core.ModPlugin

    return this.get(this.createResourceLocation(resourceString), tClass);
  }

  private <T> PluginWrapper<T> get(ResourceLocation resourceLocation, Class<T> tClass) {
    ModContainer modContainer;

    // TODO: swap out modId if overridden

    // lookup the container by modId
    modContainer = this.getModContainer(resourceLocation.getModId());

    return new PluginWrapper<>(tClass, resourceLocation.getResourceString(), modContainer);
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

  private ModContainer getModContainer(String modId) {
    ModContainer modContainer;

    modContainer = this.modContainerMap.get(modId);

    if (modContainer == null) {
      throw new IllegalArgumentException(String.format("Unrecognized mod id: %s", modId));
    }
    return modContainer;
  }
}
