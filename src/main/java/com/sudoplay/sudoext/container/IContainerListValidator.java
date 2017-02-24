package com.sudoplay.sudoext.container;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IContainerListValidator {

  /**
   * Iterates through the provided container list and adds only the containers with a valid meta file to the
   * provided store.
   *
   * @param containerList list to validate
   * @param store         the list to store valid containers in
   * @return the store
   */
  List<Container> validate(List<Container> containerList, List<Container> store);
}
