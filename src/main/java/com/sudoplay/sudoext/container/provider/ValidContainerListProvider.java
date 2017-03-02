package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.container.Container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class ValidContainerListProvider implements
    IContainerListProvider {

  private IContainerListProvider containerListProvider;

  public ValidContainerListProvider(IContainerListProvider containerListProvider) {
    this.containerListProvider = containerListProvider;
  }

  @Override
  public List<Container> getContainerList() {

    // prune invalid containers from the list
    ArrayList<Container> list = this.containerListProvider
        .getContainerList()
        .stream()
        .filter(Container::isValid)
        .collect(Collectors.toCollection(ArrayList::new));

    return Collections.unmodifiableList(list);
  }
}
