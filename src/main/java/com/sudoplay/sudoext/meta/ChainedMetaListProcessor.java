package com.sudoplay.sudoext.meta;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by codetaylor on 3/3/2017.
 */
public abstract class ChainedMetaListProcessor implements
    IMetaListProcessor {

  private ChainedMetaListProcessor metaListProcessor;

  public ChainedMetaListProcessor(
      @Nullable ChainedMetaListProcessor metaListProcessor
  ) {
    this.metaListProcessor = metaListProcessor;
  }

  @Override
  public List<Meta> process(List<Meta> metaList) {

    if (this.metaListProcessor != null) {
      return this.processLocal(this.metaListProcessor.process(metaList));

    } else {
      return this.processLocal(metaList);
    }
  }

  protected abstract List<Meta> processLocal(List<Meta> metaList);
}
