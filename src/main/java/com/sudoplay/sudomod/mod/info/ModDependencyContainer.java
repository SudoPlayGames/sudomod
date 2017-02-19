package com.sudoplay.sudomod.mod.info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModDependencyContainer {

  private Set<ModDependency> required;
  private List<ModDependency> before;
  private List<ModDependency> after;

  public ModDependencyContainer() {
    this.required = new HashSet<>();
    this.before = new ArrayList<>();
    this.after = new ArrayList<>();
  }

  public void addModDependency(ModDependency modDependency, LoadOrder loadOrder) {

    switch (loadOrder) {
      case RequiredBefore:
      case RequiredAfter:
        this.required.add(modDependency);
        break;

      case Before:
        this.before.add(modDependency);
        break;

      case After:
        this.after.add(modDependency);
        break;

      default:
        // should never happen
        throw new IllegalArgumentException(String.format("Unrecognized load order enum: %s", loadOrder));
    }
  }

  public Set<ModDependency> getRequired() {
    return this.required;
  }

  public List<ModDependency> getBefore() {
    return this.before;
  }

  public List<ModDependency> getAfter() {
    return this.after;
  }

  @Override
  public String toString() {
    return "ModDependencyContainer{" +
        "required=" + this.required +
        ", before=" + this.before +
        ", after=" + this.after +
        '}';
  }
}
