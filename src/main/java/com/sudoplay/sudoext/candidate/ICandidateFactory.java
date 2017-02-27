package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.Candidate;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/25/2017.
 */
public interface ICandidateFactory {

  Candidate create(Path path);
}
