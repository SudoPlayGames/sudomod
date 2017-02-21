package com.sudoplay.sudomod.mod.candidate;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IModCandidateListExtractor {

  List<ModCandidate> extract(List<ModCandidate> modCandidateList, List<ModCandidate> store);
}
