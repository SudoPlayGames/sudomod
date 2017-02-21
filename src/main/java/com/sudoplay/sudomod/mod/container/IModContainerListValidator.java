package com.sudoplay.sudomod.mod.container;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IModContainerListValidator {

  /**
   * Iterates through the provided mod container list and adds only the containers with a valid info file to the
   * provided store.
   *
   * @param modContainerList list to validate
   * @param store            the list to store valid containers in
   * @return the store
   */
  List<ModContainer> validate(List<ModContainer> modContainerList, List<ModContainer> store);
}
