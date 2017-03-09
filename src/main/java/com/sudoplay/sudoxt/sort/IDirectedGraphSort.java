package com.sudoplay.sudoxt.sort;

import java.util.List;

/**
 * Created by codetaylor on 3/2/2017.
 */
public interface IDirectedGraphSort<T> {

  List<T> sort(DirectedGraph<T> g) throws CyclicGraphException;
}
