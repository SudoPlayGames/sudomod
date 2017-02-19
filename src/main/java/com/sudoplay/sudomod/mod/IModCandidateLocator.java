package com.sudoplay.sudomod.mod;

import java.io.File;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public interface IModCandidateLocator {

  List<File> locateModCandidates(File modLocation, String modInfoFilename);

}
