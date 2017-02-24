package com.sudoplay.sudoext.meta;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class DefaultMetaFactory implements
    IMetaFactory {

  @Override
  public Meta create() {
    return new Meta();
  }
}
