package com.sudoplay.sudoext.candidate.locator;

import java.nio.file.LinkOption;

/**
 * Created by codetaylor on 2/20/2017.
 */
/* package */ abstract class AbstractCandidateLocator implements
    ICandidateLocator {

  /* package */ LinkOption[] linkOptions;

  /* package */ AbstractCandidateLocator(boolean followLinks) {

    if (!followLinks) {
      this.linkOptions = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};

    } else {
      this.linkOptions = new LinkOption[0];
    }
  }
}
