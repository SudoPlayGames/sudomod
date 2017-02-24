package com.sudoplay.sudoext.meta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class DependencyContainer {

  private Set<Dependency> required;
  private List<Dependency> dependencyList;
  private List<Dependency> dependentList;

  public DependencyContainer() {
    this.required = new HashSet<>();
    this.dependencyList = new ArrayList<>();
    this.dependentList = new ArrayList<>();
  }

  public void addDependency(Dependency dependency, LoadOrder loadOrder) {

    switch (loadOrder) {
      case RequiredBefore:
        this.required.add(dependency);
        this.dependentList.add(dependency);
        break;

      case RequiredAfter:
        this.required.add(dependency);
        this.dependencyList.add(dependency);
        break;

      case Before:
        this.dependentList.add(dependency);
        break;

      case After:
        this.dependencyList.add(dependency);
        break;

      default:
        // should never happen
        throw new IllegalArgumentException(String.format("Unrecognized load order enum: %s", loadOrder));
    }
  }

  public Set<Dependency> getRequired() {
    return this.required;
  }

  public List<Dependency> getDependencyList() {
    return this.dependencyList;
  }

  public List<Dependency> getDependentList() {
    return this.dependentList;
  }

  @Override
  public String toString() {
    return "DependencyContainer{" +
        "required=" + this.required +
        ", dependencyList=" + this.dependencyList +
        ", dependentList=" + this.dependentList +
        '}';
  }
}
