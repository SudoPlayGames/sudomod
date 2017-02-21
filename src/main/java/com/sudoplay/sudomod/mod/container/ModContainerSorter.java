package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.info.ModDependency;
import com.sudoplay.sudomod.mod.info.ModDependencyContainer;
import com.sudoplay.sudomod.sort.DirectedGraph;
import com.sudoplay.sudomod.sort.CyclicGraphException;
import com.sudoplay.sudomod.sort.TopologicalSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ModContainerSorter implements
    IModContainerSorter {

  @Override
  public List<ModContainer> sort(List<ModContainer> modContainerList, List<ModContainer> store) throws CyclicGraphException {

    List<SortObjectWrapper> sortObjectWrapperList;
    Map<String, SortObjectWrapper> sortObjectWrapperMap;
    DirectedGraph<SortObjectWrapper> directedGraph;
    SortObjectWrapper beforeAll;
    SortObjectWrapper before;
    SortObjectWrapper afterAll;
    SortObjectWrapper after;

    sortObjectWrapperList = new ArrayList<>();
    sortObjectWrapperMap = new HashMap<>();
    directedGraph = new DirectedGraph<>();

    beforeAll = new SortObjectWrapper();
    before = new SortObjectWrapper();
    afterAll = new SortObjectWrapper();
    after = new SortObjectWrapper();

    directedGraph.addNode(beforeAll);
    directedGraph.addNode(before);
    directedGraph.addNode(afterAll);
    directedGraph.addNode(after);

    directedGraph.addEdge(before, after);
    directedGraph.addEdge(beforeAll, before);
    directedGraph.addEdge(after, afterAll);

    for (ModContainer modContainer : modContainerList) {
      SortObjectWrapper sortObjectWrapper;

      sortObjectWrapper = new SortObjectWrapper(modContainer);
      sortObjectWrapperList.add(sortObjectWrapper);
      sortObjectWrapperMap.put(modContainer.getModInfo().getId(), sortObjectWrapper);
      directedGraph.addNode(sortObjectWrapper);
    }

    for (SortObjectWrapper sortObjectWrapper : sortObjectWrapperList) {
      ModContainer modContainer;
      ModDependencyContainer modDependencyContainer;
      List<ModDependency> dependencyList;
      List<ModDependency> dependentList;
      String dependencyModId;
      SortObjectWrapper mappedSortObjectWrapper;

      boolean preDependencyAdded;
      boolean postDependencyAdded;

      modContainer = sortObjectWrapper.getWrappedObject();
      modDependencyContainer = modContainer.getModInfo().getModDependencyContainer();
      dependencyList = modDependencyContainer.getDependencyList();
      dependentList = modDependencyContainer.getDependentList();

      preDependencyAdded = false;
      postDependencyAdded = false;

      for (ModDependency modDependency : dependencyList) {

        preDependencyAdded = true;
        dependencyModId = modDependency.getId();

        if ("*".equals(dependencyModId)) {
          // after all
          directedGraph.addEdge(sortObjectWrapper, afterAll);
          directedGraph.addEdge(after, sortObjectWrapper);
          postDependencyAdded = true;

        } else {
          directedGraph.addEdge(before, sortObjectWrapper);
          mappedSortObjectWrapper = sortObjectWrapperMap.get(dependencyModId);

          if (mappedSortObjectWrapper != null) {
            directedGraph.addEdge(mappedSortObjectWrapper, sortObjectWrapper);
          }
        }
      }

      for (ModDependency modDependency : dependentList) {
        postDependencyAdded = true;

        dependencyModId = modDependency.getId();

        if ("*".equals(dependencyModId)) {
          // before all
          directedGraph.addEdge(beforeAll, sortObjectWrapper);
          directedGraph.addEdge(sortObjectWrapper, before);
          preDependencyAdded = true;

        } else {
          directedGraph.addEdge(sortObjectWrapper, after);
          mappedSortObjectWrapper = sortObjectWrapperMap.get(dependencyModId);

          if (mappedSortObjectWrapper != null) {
            directedGraph.addEdge(sortObjectWrapper, mappedSortObjectWrapper);
          }
        }
      }

      if (!preDependencyAdded) {
        directedGraph.addEdge(before, sortObjectWrapper);
      }

      if (!postDependencyAdded) {
        directedGraph.addEdge(sortObjectWrapper, after);
      }
    }

    for (SortObjectWrapper sortObjectWrapper : TopologicalSort.sort(directedGraph)) {
      Object wrappedObject;

      wrappedObject = sortObjectWrapper.getWrappedObject();

      if (wrappedObject != null && wrappedObject instanceof ModContainer) {
        store.add((ModContainer) wrappedObject);
      }
    }

    return store;
  }

}
