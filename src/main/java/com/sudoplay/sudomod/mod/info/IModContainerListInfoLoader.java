package com.sudoplay.sudomod.mod.info;

import com.sudoplay.sudomod.mod.container.ModContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface IModContainerListInfoLoader {

  List<ModContainer> load(
      List<ModContainer> modContainerList,
      ArrayList<ModContainer> store
  );
}
