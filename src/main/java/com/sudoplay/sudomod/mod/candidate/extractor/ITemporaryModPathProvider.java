package com.sudoplay.sudomod.mod.candidate.extractor;

import com.sudoplay.sudomod.mod.candidate.ModCandidateCompressed;

import java.nio.file.Path;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface ITemporaryModPathProvider {
  Path getTemporaryModPath(ModCandidateCompressed modCandidateCompressed);
}
