package com.sudoplay.sudoxt.candidate;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/25/2017.
 */
public interface ICandidateFactory {

  Candidate create(Path path);
}
