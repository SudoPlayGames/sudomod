package com.sudoplay.sudomod;

import com.sudoplay.sudomod.folder.IFolderLifecycleEventPlugin;
import com.sudoplay.sudomod.mod.candidate.IModCandidateListExtractor;
import com.sudoplay.sudomod.mod.candidate.IModCandidateListProvider;
import com.sudoplay.sudomod.mod.candidate.ModCandidate;
import com.sudoplay.sudomod.mod.container.IModContainerListProvider;
import com.sudoplay.sudomod.mod.container.IModContainerListValidator;
import com.sudoplay.sudomod.mod.container.ModContainer;
import com.sudoplay.sudomod.mod.info.IModInfoListLoader;
import com.sudoplay.sudomod.mod.info.ModInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class ModService {

  private IFolderLifecycleEventPlugin folderLifecycleEventPlugin;
  private IModCandidateListProvider modCandidateListProvider;
  private IModCandidateListExtractor modCandidateListExtractor;
  private IModContainerListProvider modContainerListProvider;
  private IModInfoListLoader modInfoListLoader;
  private IModContainerListValidator modContainerListValidator;

  public ModService(
      IFolderLifecycleEventPlugin folderLifecycleEventPlugin,
      IModCandidateListProvider modCandidateListProvider,
      IModCandidateListExtractor modCandidateListExtractor,
      IModContainerListProvider modContainerListProvider,
      IModInfoListLoader modInfoListLoader,
      IModContainerListValidator modContainerListValidator
  ) {
    this.folderLifecycleEventPlugin = folderLifecycleEventPlugin;
    this.modCandidateListProvider = modCandidateListProvider;
    this.modCandidateListExtractor = modCandidateListExtractor;
    this.modContainerListProvider = modContainerListProvider;
    this.modInfoListLoader = modInfoListLoader;
    this.modContainerListValidator = modContainerListValidator;
  }

  public void initialize() throws ModServiceException {
    List<ModCandidate> modCandidateList;
    List<ModContainer> modContainerList;

    this.folderLifecycleEventPlugin.initialize();

    // look in the mod folder and build a list of folders and files that might be valid mods
    modCandidateList = this.modCandidateListProvider.getModCandidateList(new ArrayList<>());

    // go through each mod candidate and extract compressed files to temporary directory
    // replace compressed candidates with temporary candidates
    modCandidateList = this.modCandidateListExtractor.extract(modCandidateList);

    // create mod container list from mod candidate list
    modContainerList = this.modContainerListProvider.getModContainerList(modCandidateList, new ArrayList<>());

    // load each mod's info file
    modContainerList = this.modInfoListLoader.load(modContainerList);

    // validate each mod's mod-info, remove mods that don't validate
    modContainerList = this.modContainerListValidator.validate(modContainerList);

    // TODO: sort mod containers

    // TODO: initialize mod containers

    System.out.println(modContainerList);
  }

  public void dispose() {
    this.folderLifecycleEventPlugin.dispose();
  }
}
