package com.sudoplay.sudoext.meta.processor;

import com.sudoplay.sudoext.meta.ChainedMetaListProcessor;
import com.sudoplay.sudoext.meta.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class PruneInvalidMetaListProcessor extends
    ChainedMetaListProcessor {

  public PruneInvalidMetaListProcessor(
      ChainedMetaListProcessor metaListProcessor
  ) {
    super(metaListProcessor);
  }

  @Override
  public List<Meta> processLocal(List<Meta> metaList) {

    // prune invalid containers from the list
    return metaList
        .stream()
        .filter(Meta::isValid)
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
