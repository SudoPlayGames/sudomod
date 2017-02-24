package com.sudoplay.sudoext.candidate.locator;

import com.sudoplay.sudoext.candidate.Candidate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface ICandidateLocator {

  List<Candidate> locateCandidates(
      Path location,
      List<Candidate> store
  ) throws IOException;

}
