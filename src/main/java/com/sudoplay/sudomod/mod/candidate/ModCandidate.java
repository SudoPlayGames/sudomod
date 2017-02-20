package com.sudoplay.sudomod.mod.candidate;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public abstract class ModCandidate {

  private Path path;

  public ModCandidate(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return this.path;
  }
}
