package com.sudoplay.sudoext.container.provider;

import com.sudoplay.sudoext.candidate.ICandidateListProvider;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.container.IContainerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class CandidateContainerListProvider implements
    IContainerListProvider {

  private ICandidateListProvider candidateListProvider;
  private IContainerFactory containerFactory;

  public CandidateContainerListProvider(
      ICandidateListProvider candidateListProvider,
      IContainerFactory containerFactory
  ) {
    this.candidateListProvider = candidateListProvider;
    this.containerFactory = containerFactory;
  }

  @Override
  public List<Container> getContainerList() {
    List<Container> result;

    result = this.candidateListProvider
        .getCandidateList()
        .stream()
        .map(candidate -> this.containerFactory.create(candidate.getPath()))
        .collect(Collectors.toCollection(ArrayList::new));

    return Collections.unmodifiableList(result);
  }
}
