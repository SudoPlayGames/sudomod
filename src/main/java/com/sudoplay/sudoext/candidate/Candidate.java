package com.sudoplay.sudoext.candidate;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class Candidate {

  public enum Type {
    Folder, Compressed
  }

  private Path path;
  private Type type;

  public Candidate(Path path, Type type) {
    this.path = path;
    this.type = type;
  }

  public Path getPath() {
    return this.path;
  }

  public Type getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Candidate candidate = (Candidate) o;

    if (!path.equals(candidate.path)) return false;
    return type == candidate.type;
  }

  @Override
  public int hashCode() {
    int result = path.hashCode();
    result = 31 * result + type.hashCode();
    return result;
  }
}
