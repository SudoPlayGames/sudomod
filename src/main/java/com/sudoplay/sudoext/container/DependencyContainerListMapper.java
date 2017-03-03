package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.meta.Dependency;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DependencyContainerListMapper {

  @NotNull List<Container> getDependencyContainerList(
      Map<String, Container> containerMap,
      List<Dependency> dependencyList
  ) {
    List<Container> dependencyContainerList;
    Container container;

    dependencyContainerList = new ArrayList<>();

    // build the dependency container list
    for (Dependency dependency : dependencyList) {
      container = containerMap.get(dependency.getId());

      if (container != null) {
        dependencyContainerList.add(container);
      }
    }
    return dependencyContainerList;
  }
}