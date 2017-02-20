package com.sudoplay.sudomod.mod.candidate;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IModCandidateListProvider {

  List<ModCandidate> getModCandidateList(
      List<ModCandidate> store
  );
}
