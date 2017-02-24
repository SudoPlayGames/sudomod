package com.sudoplay.sudoext.meta;

import com.sudoplay.sudoext.container.Container;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface IContainerListMetaLoader {

  List<Container> load(
      List<Container> containerList,
      ArrayList<Container> store
  );
}
