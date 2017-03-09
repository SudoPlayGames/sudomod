package com.sudoplay.sudoxt.meta;

import com.sudoplay.sudoxt.candidate.ICandidateListProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class MetaListProvider implements
    IMetaListProvider {

  private ICandidateListProvider candidateListProvider;
  private IMetaListProcessor metaListProcessor;
  private String metaFilename;

  public MetaListProvider(
      ICandidateListProvider candidateListProvider,
      IMetaListProcessor metaListProcessor,
      String metaFilename
  ) {
    this.candidateListProvider = candidateListProvider;
    this.metaListProcessor = metaListProcessor;
    this.metaFilename = metaFilename;
  }

  @Override
  public List<Meta> getMetaList() {
    List<Meta> result;

    result = this.candidateListProvider
        .getCandidateList()
        .stream()
        .map(candidate -> {
          Path parentPath = candidate.getPath();
          Path path = parentPath.resolve(this.metaFilename);
          return new Meta(parentPath, path);
        })
        .collect(Collectors.toCollection(ArrayList::new));

    return this.metaListProcessor.process(result);
  }
}
