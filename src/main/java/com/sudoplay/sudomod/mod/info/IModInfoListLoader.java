package com.sudoplay.sudomod.mod.info;

import com.sudoplay.sudomod.mod.ModLoadException;

import java.io.File;
import java.util.List;

/**
 * Created by sk3lls on 2/18/2017.
 */
public interface IModInfoListLoader {
  List<ModInfo> load(List<File> modCandidateList, String modInfoFilename, List<ModInfo> store) throws ModLoadException;
}
