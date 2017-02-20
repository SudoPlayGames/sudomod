package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.validator.IModInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
  public List<ModContainer> validate(List<ModContainer> modContainerList) {
    List<ModContainer> toAdd = new ArrayList<>();

    for (Iterator<ModContainer> it = modContainerList.iterator(); it.hasNext(); ) {
      ModContainer modContainer = it.next();
      Path path = modContainer.getPath();
      ModInfo modInfo = modContainer.getModInfo();
      it.remove();

      if (this.modInfoValidator.isValid(modInfo, path, modContainerList)) {
        toAdd.add(modContainer);

      } else {
        LOG.error("Info file for mod [{}] failed validation, skipping", modInfo.getId());
      }
    }

    modContainerList.addAll(toAdd);
    return modContainerList;
  }

}
