package com.sudoplay.sudomod.mod.info;

import com.sudoplay.sudomod.mod.container.ModContainer;
import com.sudoplay.sudomod.mod.info.parser.IModInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModInfoListLoader implements IModInfoListLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ModInfoListLoader.class);

  private IModInfoParser modInfoParser;
  private IModInfoFactory modInfoFactory;
  private String modInfoFilename;

  public ModInfoListLoader(IModInfoParser modInfoParser, IModInfoFactory modInfoFactory, String modInfoFilename) {
    this.modInfoParser = modInfoParser;
    this.modInfoFactory = modInfoFactory;
    this.modInfoFilename = modInfoFilename;
  }

  @Override
  public List<ModContainer> load(
      List<ModContainer> modContainerList
  ) {

    LOG.debug("Entering load(modCandidateList=[{}])", modContainerList);

    for (ModContainer modContainer : modContainerList) {
      Path modLocation = modContainer.getPath();
      Path modInfoFilePath = modLocation.resolve(this.modInfoFilename);

      try {
        String modInfoJsonString = new String(Files.readAllBytes(modInfoFilePath));
        ModInfo modInfo = this.modInfoParser.parseModInfoFile(modInfoJsonString, this.modInfoFactory.create());
        modContainer.setModInfo(modInfo);

      } catch (Exception e) {
        LOG.error(String.format("Unable to parse mod info file: %s, skipped loading mod %s", modInfoFilePath,
            modLocation), e);
      }
    }

    LOG.debug("Leaving load(): {}", modContainerList);

    return modContainerList;
  }
}
