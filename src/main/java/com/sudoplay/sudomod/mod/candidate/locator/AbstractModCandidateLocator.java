package com.sudoplay.sudomod.mod.candidate.locator;

import java.nio.file.LinkOption;

/**
 * Created by codetaylor on 2/20/2017.
 */
public abstract class AbstractModCandidateLocator implements
    IModCandidateLocator {

  protected LinkOption[] linkOptions;

  public AbstractModCandidateLocator(boolean followLinks) {

    if (!followLinks) {
      this.linkOptions = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
    }
  }
}
