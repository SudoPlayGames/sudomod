package com.sudoplay.sudoext.sort;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class TopologicalSortTest {

  @Test
  public void sortShouldReturnSortedList() throws Exception {

    DirectedGraph<String> directedGraph = new DirectedGraph<>();

    directedGraph.addNode("node-a");
    directedGraph.addNode("node-b");
    directedGraph.addNode("node-c");
    directedGraph.addNode("node-d");

    directedGraph.addEdge("node-a", "node-b");
    directedGraph.addEdge("node-a", "node-c");
    directedGraph.addEdge("node-c", "node-d");
    directedGraph.addEdge("node-b", "node-c");

    TopologicalSort topologicalSort = new TopologicalSort();
    List<String> list = topologicalSort.sort(directedGraph);

    Assert.assertEquals("node-a", list.get(0));
    Assert.assertEquals("node-b", list.get(1));
    Assert.assertEquals("node-c", list.get(2));
    Assert.assertEquals("node-d", list.get(3));
  }

  @Test(expected = CyclicGraphException.class)
  public void sortShouldThrowWhenCycleDetected() throws Exception {

    DirectedGraph<String> directedGraph = new DirectedGraph<>();

    directedGraph.addNode("node-a");
    directedGraph.addNode("node-b");

    directedGraph.addEdge("node-a", "node-b");
    directedGraph.addEdge("node-b", "node-a");

    TopologicalSort topologicalSort = new TopologicalSort();
    topologicalSort.sort(directedGraph);
  }

}