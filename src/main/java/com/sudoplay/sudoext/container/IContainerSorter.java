package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.sort.CyclicGraphException;

import java.util.List;

/**
 * Created by codetaylor on 2/21/2017.
 */
public interface IContainerSorter {

  List<Container> sort(
      List<Container> containerList,
      List<Container> store
  ) throws CyclicGraphException;
}
