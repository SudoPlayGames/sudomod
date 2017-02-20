package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.info.ModInfo;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModContainer {

  private Path path;
  private ModInfo modInfo;

  public ModContainer(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return this.path;
  }

  public void setModInfo(ModInfo modInfo) {
    this.modInfo = modInfo;
  }

  public ModInfo getModInfo() {
    return this.modInfo;
  }
}
