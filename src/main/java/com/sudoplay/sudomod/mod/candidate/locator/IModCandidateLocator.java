package com.sudoplay.sudomod.mod.candidate.locator;

import com.sudoplay.sudomod.mod.ModLoadException;
import com.sudoplay.sudomod.mod.candidate.ModCandidate;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface IModCandidateLocator {

  List<ModCandidate> locateModCandidates(
      Path modLocation,
      List<ModCandidate> store
  ) throws ModLoadException;

}
