package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.sort.CyclicGraphException;

import java.util.List;

/**
 * Created by codetaylor on 2/21/2017.
 */
public interface IModContainerSorter {
  List<ModContainer> sort(List<ModContainer> modContainerList, List<ModContainer> store) throws CyclicGraphException;
}
