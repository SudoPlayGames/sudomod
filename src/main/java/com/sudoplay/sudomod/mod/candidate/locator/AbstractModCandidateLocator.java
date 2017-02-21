package com.sudoplay.sudomod.mod.candidate.locator;

import java.nio.file.LinkOption;

/**
 * Created by codetaylor on 2/20/2017.
 */
/* package */ abstract class AbstractModCandidateLocator implements
    IModCandidateLocator {

  /* package */ LinkOption[] linkOptions;

  /* package */ AbstractModCandidateLocator(boolean followLinks) {

    if (!followLinks) {
      this.linkOptions = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};

    } else {
      this.linkOptions = new LinkOption[0];
    }
  }
}
