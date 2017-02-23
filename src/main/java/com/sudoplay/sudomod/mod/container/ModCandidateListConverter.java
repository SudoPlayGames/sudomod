package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.candidate.ModCandidate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModCandidateListConverter implements
    IModCandidateListConverter {

  private IModContainerFactory modContainerFactory;

  public ModCandidateListConverter(IModContainerFactory modContainerFactory) {
    this.modContainerFactory = modContainerFactory;
  }

  @Override
  public List<ModContainer> convert(
      List<ModCandidate> modCandidateList,
      List<ModContainer> store
  ) {
    store.addAll(
        modCandidateList
            .stream()
            .map(modCandidate -> this.modContainerFactory.create(modCandidate.getPath()))
            .collect(Collectors.toList())
    );
    return store;
  }

}
