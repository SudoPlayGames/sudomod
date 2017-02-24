package com.sudoplay.sudoext.candidate;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public abstract class Candidate {

  private Path path;

  public Candidate(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return this.path;
  }
}
