package com.sudoplay.sudoext.meta;

import java.util.List;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class CachedMetaListProvider implements
    IMetaListProvider {

  private IMetaListProvider metaListProvider;
  private List<Meta> metaList;

  public CachedMetaListProvider(IMetaListProvider metaListProvider) {
    this.metaListProvider = metaListProvider;
  }

  @Override
  public List<Meta> getMetaList() {

    if (this.metaList == null) {
      this.metaList = this.metaListProvider.getMetaList();
    }

    return this.metaList;
  }
}
