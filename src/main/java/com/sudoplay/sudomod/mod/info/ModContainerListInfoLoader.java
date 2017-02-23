package com.sudoplay.sudomod.mod.info;

import com.sudoplay.sudomod.mod.container.ModContainer;
import com.sudoplay.sudomod.mod.info.parser.IModInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModContainerListInfoLoader implements IModContainerListInfoLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ModContainerListInfoLoader.class);

  private IModInfoParser modInfoParser;
  private IModInfoFactory modInfoFactory;
  private String modInfoFilename;

  public ModContainerListInfoLoader(
      IModInfoParser modInfoParser,
      IModInfoFactory modInfoFactory,
      String modInfoFilename
  ) {
    this.modInfoParser = modInfoParser;
    this.modInfoFactory = modInfoFactory;
    this.modInfoFilename = modInfoFilename;
  }

  @Override
  public List<ModContainer> load(
      List<ModContainer> modContainerList,
      ArrayList<ModContainer> store
  ) {
    LOG.debug("Entering load(modContainerList, store)");
    LOG.trace("...modContainerList=[{}]", modContainerList);
    LOG.trace("...store=[{}]", store);

    int count = 0;

    for (ModContainer modContainer : modContainerList) {
      Path modLocation = modContainer.getPath();
      Path modInfoFilePath = modLocation.resolve(this.modInfoFilename);

      try {
        String modInfoJsonString = new String(Files.readAllBytes(modInfoFilePath));
        ModInfo modInfo = this.modInfoParser.parseModInfoFile(modInfoJsonString, this.modInfoFactory.create());
        modContainer.setModInfo(modInfo);
        store.add(modContainer);
        count += 1;

      } catch (Exception e) {
        LOG.error(String.format("Unable to parse mod info file: %s, skipped loading mod %s", modInfoFilePath,
            modLocation), e);
      }
    }

    LOG.info("Loaded [{}] mod info files", count);
    LOG.debug("Leaving load()");
    LOG.trace("...[{}]", store);
    return store;
  }
}
