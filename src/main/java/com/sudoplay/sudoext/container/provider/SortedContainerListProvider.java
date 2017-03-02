package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.DependencyContainer;
import com.sudoplay.sudoext.sort.DirectedGraph;
import com.sudoplay.sudoext.sort.GraphNode;
import com.sudoplay.sudoext.sort.IDirectedGraphSort;

import java.util.*;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class SortedContainerListProvider implements
    IContainerListProvider {

  private IContainerListProvider containerListProvider;
  private IDirectedGraphSort directedGraphSort;

  public SortedContainerListProvider(
      IContainerListProvider containerListProvider,
      IDirectedGraphSort directedGraphSort
  ) {
    this.containerListProvider = containerListProvider;
    this.directedGraphSort = directedGraphSort;
  }

  @Override
  public List<Container> getContainerList() {

    List<GraphNode> graphNodeList;
    Map<String, GraphNode> sortObjectWrapperMap;
    DirectedGraph<GraphNode> directedGraph;
    GraphNode beforeAll;
    GraphNode before;
    GraphNode afterAll;
    GraphNode after;

    graphNodeList = new ArrayList<>();
    sortObjectWrapperMap = new HashMap<>();
    directedGraph = new DirectedGraph<>();

    beforeAll = new GraphNode();
    before = new GraphNode();
    afterAll = new GraphNode();
    after = new GraphNode();

    directedGraph.addNode(beforeAll);
    directedGraph.addNode(before);
    directedGraph.addNode(afterAll);
    directedGraph.addNode(after);

    directedGraph.addEdge(before, after);
    directedGraph.addEdge(beforeAll, before);
    directedGraph.addEdge(after, afterAll);

    for (Container container : this.containerListProvider.getContainerList()) {
      GraphNode graphNode;

      graphNode = new GraphNode(container);
      graphNodeList.add(graphNode);
      sortObjectWrapperMap.put(container.getMeta().getId(), graphNode);
      directedGraph.addNode(graphNode);
    }

    for (GraphNode graphNode : graphNodeList) {
      Container container;
      DependencyContainer dependencyContainer;
      List<Dependency> dependencyList;
      List<Dependency> dependentList;
      String dependencyId;
      GraphNode mappedGraphNode;

      boolean preDependencyAdded;
      boolean postDependencyAdded;

      container = graphNode.get();
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
          directedGraph.addEdge(graphNode, afterAll);
          directedGraph.addEdge(after, graphNode);
          postDependencyAdded = true;

        } else {
          directedGraph.addEdge(before, graphNode);
          mappedGraphNode = sortObjectWrapperMap.get(dependencyId);

          if (mappedGraphNode != null) {
            directedGraph.addEdge(mappedGraphNode, graphNode);
          }
        }
      }

      for (Dependency dependency : dependentList) {
        postDependencyAdded = true;

        dependencyId = dependency.getId();

        if ("*".equals(dependencyId)) {
          // before all
          directedGraph.addEdge(beforeAll, graphNode);
          directedGraph.addEdge(graphNode, before);
          preDependencyAdded = true;

        } else {
          directedGraph.addEdge(graphNode, after);
          mappedGraphNode = sortObjectWrapperMap.get(dependencyId);

          if (mappedGraphNode != null) {
            directedGraph.addEdge(graphNode, mappedGraphNode);
          }
        }
      }

      if (!preDependencyAdded) {
        directedGraph.addEdge(before, graphNode);
      }

      if (!postDependencyAdded) {
        directedGraph.addEdge(graphNode, after);
      }
    }

    List<Container> result = new ArrayList<>();

    for (GraphNode graphNode : this.directedGraphSort.sort(directedGraph)) {
      Object wrappedObject;

      wrappedObject = graphNode.get();

      if (wrappedObject != null && wrappedObject instanceof Container) {
        result.add((Container) wrappedObject);
      }
    }

    return Collections.unmodifiableList(result);
  }

}
