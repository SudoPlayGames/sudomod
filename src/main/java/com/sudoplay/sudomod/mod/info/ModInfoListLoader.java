package com.sudoplay.sudomod.mod.info;

import com.sudoplay.sudomod.mod.ModLoadException;
import com.sudoplay.sudomod.mod.info.parser.IModInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by sk3lls on 2/18/2017.
 */
public class ModInfoListLoader implements IModInfoListLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ModInfoListLoader.class);

  private IModInfoParser modInfoParser;
  private IModInfoFactory modInfoFactory;

  public ModInfoListLoader(IModInfoParser modInfoParser, IModInfoFactory modInfoFactory) {
    this.modInfoParser = modInfoParser;
    this.modInfoFactory = modInfoFactory;
  }

  @Override
  public List<ModInfo> load(
      List<File> modCandidateList,
      String modInfoFilename,
      List<ModInfo> store
  ) throws ModLoadException {

    LOG.debug("Entering load(modCandidateList=[{}], modInfoFilename=[{}], store=[{}])", modCandidateList,
        modInfoFilename, store);

    for (File modLocation : modCandidateList) {
      File modInfoFile = new File(modLocation, modInfoFilename);

      try {
        String modInfoJsonString = new String(Files.readAllBytes(modInfoFile.toPath()));
        ModInfo modInfo = this.modInfoParser.parseModInfoFile(modInfoJsonString, this.modInfoFactory.create());
        store.add(modInfo);

      } catch (Exception e) {
        LOG.error(String.format("Unable to parse mod info file: %s, skipped loading mod %s", modInfoFile,
            modLocation), e);
      }
    }

    LOG.debug("Leaving load(): {}", store);

    return store;
  }
}
