package com.sudoplay.sudomod.mod.container;

import com.sudoplay.sudomod.mod.candidate.ModCandidate;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface IModContainerListProvider {
  List<ModContainer> getModContainerList(
      List<ModCandidate> modCandidateList,
      List<ModContainer> store
  );
}
