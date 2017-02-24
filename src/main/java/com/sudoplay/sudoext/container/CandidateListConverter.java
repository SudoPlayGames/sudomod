package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.candidate.Candidate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateListConverter implements
    ICandidateListConverter {

  private IContainerFactory containerFactory;

  public CandidateListConverter(IContainerFactory containerFactory) {
    this.containerFactory = containerFactory;
  }

  @Override
  public List<Container> convert(
      List<Candidate> candidateList,
      List<Container> store
  ) {
    store.addAll(
        candidateList
            .stream()
            .map(candidate -> this.containerFactory.create(candidate.getPath()))
            .collect(Collectors.toList())
    );
    return store;
  }

}
