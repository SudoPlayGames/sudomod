package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.info.ModDependency;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.validator.IModInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModContainerListValidator implements
    IModContainerListValidator {

  private static final Logger LOG = LoggerFactory.getLogger(ModContainerListValidator.class);

  private IModInfoValidator modInfoValidator;

  public ModContainerListValidator(IModInfoValidator modInfoValidator) {
    this.modInfoValidator = modInfoValidator;
  }

  @Override
  public List<ModContainer> validate(List<ModContainer> modContainerList, List<ModContainer> store) {
    LOG.debug("Entering validate(modContainerList, store)");
    LOG.trace("...modContainerList=[{}]", modContainerList);
    LOG.trace("...store=[{}]", store);

    Map<String, ModContainer> validatedModContainerMap = new HashMap<>();
    List<ModContainer> invalidModContainers = new ArrayList<>();

    for (ModContainer modContainer : modContainerList) {
      Path path = modContainer.getPath();
      ModInfo modInfo = modContainer.getModInfo();

      if (this.modInfoValidator.isValid(modInfo, path, modContainerList)) {
        validatedModContainerMap.put(modInfo.getId(), modContainer);
        LOG.debug("Validated mod info file for mod [{}]", modInfo.getId());

      } else {
        invalidModContainers.add(modContainer);
        LOG.error("Info file for mod [{}] failed validation, can't load mod", modInfo.getId());
      }
    }

    // remove all mods that are dependent on an invalid mod
    for (ModContainer modContainer : invalidModContainers) {
      this.removeDependents(modContainer, validatedModContainerMap);
    }

    store.addAll(validatedModContainerMap.values());

    LOG.info("Validated mod info files for [{}] mods", validatedModContainerMap.size());
    LOG.debug("Leaving validate()");
    LOG.trace("...[{}]", store);
    return store;
  }

  private void removeDependents(ModContainer modContainer, Map<String, ModContainer> map) {
    ModInfo modInfo = modContainer.getModInfo();
    String id = modInfo.getId();
    List<ModDependency> dependentList = modInfo.getModDependencyContainer().getDependentList();

    for (ModDependency dependency : dependentList) {
      ModContainer dependentModContainer;
      String dependencyId = dependency.getId();

      if ((dependentModContainer = map.remove(dependencyId)) != null) {
        LOG.error("Dependent mod [{}] prevented from loading by invalid dependency mod [{}]", dependencyId, id);
        removeDependents(dependentModContainer, map);
      }
    }
  }

}
