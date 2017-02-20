package com.sudoplay.sudomod.mod.info.validator;

import com.sudoplay.sudomod.mod.container.ModContainer;
import com.sudoplay.sudomod.mod.info.ModInfo;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IModInfoValidator {
  boolean isValid(ModInfo modInfo, Path modPath, List<ModContainer> modContainerList);
}
