package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.CandidateCompressed;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface ITemporaryPathProvider {
  Path getTemporaryPath(CandidateCompressed candidateCompressed);
}
