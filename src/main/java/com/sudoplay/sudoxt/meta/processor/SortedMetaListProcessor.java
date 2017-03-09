package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.ChainedMetaListProcessor;
import com.sudoplay.sudoxt.meta.Dependency;
import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.sort.DirectedGraph;
import com.sudoplay.sudoxt.sort.GraphNode;
import com.sudoplay.sudoxt.sort.IDirectedGraphSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class SortedMetaListProcessor extends
    ChainedMetaListProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(SortedMetaListProcessor.class);

  private IDirectedGraphSort<GraphNode<Meta>> directedGraphSort;

  public SortedMetaListProcessor(
      ChainedMetaListProcessor metaListProcessor,
      IDirectedGraphSort<GraphNode<Meta>> directedGraphSort
  ) {
    super(metaListProcessor);
    this.directedGraphSort = directedGraphSort;
  }

  @Override
  public List<Meta> processLocal(List<Meta> metaList) {
    LOG.debug("Entering processLocal(metaList)");
    LOG.trace("...metaList=[{}]", metaList);

    List<GraphNode<Meta>> graphNodeList;
    Map<String, GraphNode<Meta>> sortObjectWrapperMap;
    DirectedGraph<GraphNode<Meta>> directedGraph;
    GraphNode<Meta> beforeAll;
    GraphNode<Meta> before;
    GraphNode<Meta> afterAll;
    GraphNode<Meta> after;

    graphNodeList = new ArrayList<>();
    sortObjectWrapperMap = new HashMap<>();
    directedGraph = new DirectedGraph<>();

    beforeAll = new GraphNode<>();
    before = new GraphNode<>();
    afterAll = new GraphNode<>();
    after = new GraphNode<>();

    directedGraph.addNode(beforeAll);
    directedGraph.addNode(before);
    directedGraph.addNode(afterAll);
    directedGraph.addNode(after);

    directedGraph.addEdge(before, after);
    directedGraph.addEdge(beforeAll, before);
    directedGraph.addEdge(after, afterAll);

    for (Meta meta : metaList) {
      GraphNode<Meta> graphNode;

      graphNode = new GraphNode<>(meta);
      graphNodeList.add(graphNode);
      sortObjectWrapperMap.put(meta.getId(), graphNode);
      directedGraph.addNode(graphNode);
    }

    for (GraphNode<Meta> graphNode : graphNodeList) {
      Set<Dependency> loadAfterSet;
      Set<Dependency> loadBeforeSet;
      String dependencyId;
      GraphNode<Meta> mappedGraphNode;

      boolean preDependencyAdded;
      boolean postDependencyAdded;

      Meta meta = graphNode.get();
      loadAfterSet = meta.getLoadAfterDependencySet();
      loadBeforeSet = meta.getLoadBeforeDependencySet();

      preDependencyAdded = false;
      postDependencyAdded = false;

      for (Dependency dependency : loadAfterSet) {

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

      for (Dependency dependency : loadBeforeSet) {
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

    List<Meta> result = new ArrayList<>();

    for (GraphNode<Meta> graphNode : this.directedGraphSort.sort(directedGraph)) {
      Object wrappedObject;

      wrappedObject = graphNode.get();

      if (wrappedObject != null) {
        result.add((Meta) wrappedObject);
      }
    }

    LOG.info("Sorted [{}] meta files", result.size());
    LOG.debug("Leaving processLocal()");
    LOG.trace("...[{}]", metaList);
    return result;
  }

}
