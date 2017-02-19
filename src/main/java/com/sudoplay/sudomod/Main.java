package com.sudoplay.sudomod;

import com.sudoplay.sudomod.config.Config;
import com.sudoplay.sudomod.config.ConfigBuilder;
import com.sudoplay.sudomod.mod.IModCandidateLocator;
import com.sudoplay.sudomod.mod.ModCandidateLocator;
import com.sudoplay.sudomod.mod.ModLoadException;
import com.sudoplay.sudomod.mod.info.DefaultModInfoFactory;
import com.sudoplay.sudomod.mod.info.IModInfoListLoader;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.mod.info.ModInfoListLoader;
import com.sudoplay.sudomod.mod.info.parser.IModInfoElementParser;
import com.sudoplay.sudomod.mod.info.parser.ModInfoParser;
import com.sudoplay.sudomod.mod.info.parser.element.*;
import com.sudoplay.sudomod.spi.IFolderValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sk3lls on 2/18/2017.
 */
public class Main {

  public static void main(String... args) throws ModLoadException {

    File modsLocation = new File("mods");
    File modsDataLocation = new File("mod-data");

    modsLocation.mkdirs();
    modsDataLocation.mkdirs();

    Config config = new ConfigBuilder()
        .setModLocation(modsLocation)
        .setModDataLocation(modsDataLocation)
        .setModInfoFilename("mod-info.json")
        .setDefaultApiVersionString("[0,)")
        .getConfig();

    {
      IFolderValidator folderValidator;
      IModCandidateLocator modCandidateLocator;
      IModInfoListLoader modInfoListLoader;

      folderValidator = new FolderValidator();
      modCandidateLocator = new ModCandidateLocator();
      modInfoListLoader = new ModInfoListLoader(
          new ModInfoParser(
              new IModInfoElementParser[]{
                  new ApiVersionRangeParser(config.getDefaultApiVersionString()),
                  new AuthorParser(),
                  new DependsOnParser(),
                  new DescriptionParser(),
                  new IdParser(),
                  new ImageParser(),
                  new JarParser(),
                  new ModPluginParser(),
                  new NameParser(),
                  new RepoParser(),
                  new VersionParser()
              }
          ),
          new DefaultModInfoFactory()
      );

      if (!folderValidator.isValidFolder(config.getModLocation())
          || !folderValidator.isValidFolder(config.getModDataLocation())) {
        return;
      }

      String modInfoFilename = config.getModInfoFilename();

      List<File> files = modCandidateLocator.locateModCandidates(modsLocation, modInfoFilename);
      List<ModInfo> modInfoList = modInfoListLoader.load(files, modInfoFilename, new ArrayList<>());

      // TODO: mod info validation

      // TODO: create mod containers

      // TODO: sort mod containers

      // TODO: initialize mod containers

      System.out.println(modInfoList);
    }
  }
}
