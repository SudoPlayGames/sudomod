package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.candidate.ModCandidate;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModContainerListProvider implements
    IModContainerListProvider {

  @Override
  public List<ModContainer> getModContainerList(
      List<ModCandidate> modCandidateList,
      List<ModContainer> store
  ) {

    for (ModCandidate modCandidate : modCandidateList) {
      ModContainer modContainer = new ModContainer(modCandidate.getPath());
      store.add(modContainer);
    }

    return store;
  }

}
