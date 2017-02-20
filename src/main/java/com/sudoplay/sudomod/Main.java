package com.sudoplay.sudomod;

import com.sudoplay.sudomod.config.Config;
import com.sudoplay.sudomod.config.ConfigBuilder;

import java.nio.file.Paths;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Main {

  public static void main(String... args) throws ModServiceException {

    Config config = new ConfigBuilder()
        .setFollowLinks(false)
        .setCompressedModFileExtension(".lsm")
        .setModLocation(Paths.get("mods"))
        .setModDataLocation(Paths.get("mod-data"))
        .setModTempLocation(Paths.get("mods-temp"))
        .setModInfoFilename("mod-info.json")
        .setApiVersion("1.0")
        .setDefaultModInfoApiVersionString("[0,)")
        .getConfig();

    ModService modService = ModServiceFactory.create(config);
    ModServiceLocator.registerModService("default", modService);

    ModServiceLocator.locate("default").initialize();
  }
}
