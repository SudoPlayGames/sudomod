package com.sudoplay.sudomod.service;

import com.sudoplay.sudomod.folder.IFolderLifecycleEventPlugin;
import com.sudoplay.sudomod.mod.ModClassLoaderFactory;
import com.sudoplay.sudomod.mod.candidate.IModCandidateListExtractor;
import com.sudoplay.sudomod.mod.candidate.IModCandidateListProvider;
import com.sudoplay.sudomod.mod.candidate.ModCandidate;
import com.sudoplay.sudomod.mod.container.IModContainerListProvider;
import com.sudoplay.sudomod.mod.container.IModContainerListValidator;
import com.sudoplay.sudomod.mod.container.IModContainerSorter;
import com.sudoplay.sudomod.mod.container.ModContainer;
import com.sudoplay.sudomod.mod.info.IModContainerListInfoLoader;
import com.sudoplay.sudomod.mod.info.ModDependency;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.sort.CyclicGraphException;

import java.util.*;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModService {

  private IFolderLifecycleEventPlugin folderLifecycleEventPlugin;

  private Map<String, ModContainer> modContainerMap;
  private List<ModContainer> modContainerList;

  public ModService(
      IFolderLifecycleEventPlugin folderLifecycleEventPlugin,
      IModCandidateListProvider modCandidateListProvider,
      IModCandidateListExtractor modCandidateListExtractor,
      IModContainerListProvider modContainerListProvider,
      IModContainerListInfoLoader modContainerListInfoLoader,
      IModContainerListValidator modContainerListValidator,
      IModContainerSorter modContainerSorter
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
    modContainerList = modContainerListProvider.getModContainerList(modCandidateList, new ArrayList<>());

    // load each mod's info file
    modContainerList = modContainerListInfoLoader.load(modContainerList);

    // validate each mod's mod-info, remove mods that don't validate
    modContainerList = modContainerListValidator.validate(modContainerList, new ArrayList<>());

    // sort mod containers
    try {
      modContainerList = modContainerSorter.sort(modContainerList, new ArrayList<>());

    } catch (CyclicGraphException e) {
      // exception is thrown when a cyclic dependency in the mod graph is detected
      throw new ModServiceInitializationException("Error initializing mod service", e);
    }

    // initialize mod containers
    this.modContainerList = new ArrayList<>(modContainerList);
    this.modContainerMap = new HashMap<>();

    for (ModContainer modContainer : modContainerList) {

      modContainer.setModClassLoaderFactory(
          new ModClassLoaderFactory(
              modContainer.getPath(),
              modContainer.getModInfo().getJarFileList()
          )
      );

      this.modContainerMap.put(
          modContainer.getModInfo().getId(),
          modContainer
      );
    }

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

  public List<ResourceLocation> getModPluginResourceLocationList(List<ResourceLocation> store) {

    for (ModContainer modContainer : this.modContainerList) {
      String id = modContainer.getModInfo().getId();
      String modPlugin = modContainer.getModInfo().getModPlugin();
      ResourceLocation resourceLocation = new ResourceLocation(id, modPlugin);

      store.add(resourceLocation);
    }
    return store;
  }

  public <T> T get(String resourceString) throws IllegalAccessException, InstantiationException,
      ClassNotFoundException {

    // resource location looks like this:
    // <mod-id>:<path>
    // ie. lss-core:com.sudoplay.lss.mod.core.ModPlugin

    return get(this.createResourceLocation(resourceString));
  }

  private <T> T get(ResourceLocation resourceLocation) throws ClassNotFoundException, IllegalAccessException,
      InstantiationException {

    ModContainer modContainer;

    // TODO: swap out modId if overridden

    // lookup the container by modId
    modContainer = this.getModContainer(resourceLocation.getModId());

    return modContainer.get(resourceLocation.getResourceString());
  }

  private ResourceLocation createResourceLocation(String resourceString) {
    ResourceLocation resourceLocation;

    try {
      // TODO: add resource string parser to constructor
      resourceLocation = new ResourceStringParser().parse(resourceString);

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
