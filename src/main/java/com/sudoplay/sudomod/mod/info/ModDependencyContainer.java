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
  private List<ModDependency> dependencyList;
  private List<ModDependency> dependentList;

  public ModDependencyContainer() {
    this.required = new HashSet<>();
    this.dependencyList = new ArrayList<>();
    this.dependentList = new ArrayList<>();
  }

  public void addModDependency(ModDependency modDependency, LoadOrder loadOrder) {

    switch (loadOrder) {
      case RequiredBefore:
        this.required.add(modDependency);
        this.dependentList.add(modDependency);
        break;

      case RequiredAfter:
        this.required.add(modDependency);
        this.dependencyList.add(modDependency);
        break;

      case Before:
        this.dependentList.add(modDependency);
        break;

      case After:
        this.dependencyList.add(modDependency);
        break;

      default:
        // should never happen
        throw new IllegalArgumentException(String.format("Unrecognized load order enum: %s", loadOrder));
    }
  }

  public Set<ModDependency> getRequired() {
    return this.required;
  }

  public List<ModDependency> getDependencyList() {
    return this.dependencyList;
  }

  public List<ModDependency> getDependentList() {
    return this.dependentList;
  }

  @Override
  public String toString() {
    return "ModDependencyContainer{" +
        "required=" + this.required +
        ", dependencyList=" + this.dependencyList +
        ", dependentList=" + this.dependentList +
        '}';
  }
}
