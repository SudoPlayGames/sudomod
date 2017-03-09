package com.sudoplay.sudoxt.candidate;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class Candidate {

  private Path path;

  public Candidate(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return this.path;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Candidate candidate = (Candidate) o;

    return path.equals(candidate.path);

  }

  @Override
  public int hashCode() {
    return path.hashCode();
  }
}
