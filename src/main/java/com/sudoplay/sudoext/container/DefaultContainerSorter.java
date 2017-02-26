package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.DependencyContainer;
import com.sudoplay.sudoext.sort.DirectedGraph;
import com.sudoplay.sudoext.sort.CyclicGraphException;
import com.sudoplay.sudoext.sort.SortObjectWrapper;
import com.sudoplay.sudoext.sort.TopologicalSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class DefaultContainerSorter implements
    IContainerSorter {

  @Override
  public List<Container> sort(List<Container> containerList, List<Container> store) throws CyclicGraphException {

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

    for (Container container : containerList) {
      SortObjectWrapper sortObjectWrapper;

      sortObjectWrapper = new SortObjectWrapper(container);
      sortObjectWrapperList.add(sortObjectWrapper);
      sortObjectWrapperMap.put(container.getMeta().getId(), sortObjectWrapper);
      directedGraph.addNode(sortObjectWrapper);
    }

    for (SortObjectWrapper sortObjectWrapper : sortObjectWrapperList) {
      Container container;
      DependencyContainer dependencyContainer;
      List<Dependency> dependencyList;
      List<Dependency> dependentList;
      String dependencyId;
      SortObjectWrapper mappedSortObjectWrapper;

      boolean preDependencyAdded;
      boolean postDependencyAdded;

      container = sortObjectWrapper.getWrappedObject();
      dependencyContainer = container.getMeta().getDependencyContainer();
      dependencyList = dependencyContainer.getDependencyList();
      dependentList = dependencyContainer.getDependentList();

      preDependencyAdded = false;
      postDependencyAdded = false;

      for (Dependency dependency : dependencyList) {

        preDependencyAdded = true;
        dependencyId = dependency.getId();

        if ("*".equals(dependencyId)) {
          // after all
          directedGraph.addEdge(sortObjectWrapper, afterAll);
          directedGraph.addEdge(after, sortObjectWrapper);
          postDependencyAdded = true;

        } else {
          directedGraph.addEdge(before, sortObjectWrapper);
          mappedSortObjectWrapper = sortObjectWrapperMap.get(dependencyId);

          if (mappedSortObjectWrapper != null) {
            directedGraph.addEdge(mappedSortObjectWrapper, sortObjectWrapper);
          }
        }
      }

      for (Dependency dependency : dependentList) {
        postDependencyAdded = true;

        dependencyId = dependency.getId();

        if ("*".equals(dependencyId)) {
          // before all
          directedGraph.addEdge(beforeAll, sortObjectWrapper);
          directedGraph.addEdge(sortObjectWrapper, before);
          preDependencyAdded = true;

        } else {
          directedGraph.addEdge(sortObjectWrapper, after);
          mappedSortObjectWrapper = sortObjectWrapperMap.get(dependencyId);

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

      if (wrappedObject != null && wrappedObject instanceof Container) {
        store.add((Container) wrappedObject);
      }
    }

    return store;
  }

}
