package com.sudoplay.sudoext.meta.validator.element;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Created by codetaylor on 3/6/2017.
 */
public class PluginFinder {

  private ZipSearch zipSearch;

  public PluginFinder(ZipSearch zipSearch) {
    this.zipSearch = zipSearch;
  }

  public boolean hasPlugin(String plugin, Path path, Collection<String> jarFileCollection) {

    // check path for .java file
    if (Files.exists(path.resolve(plugin.replace(".", "/") + ".java"))) {
      return true;
    }

    // check jars for .class file
    String searchValue = plugin.replace(".", "/") + ".class";

    for (String jarFile : jarFileCollection) {
      Path jarFilePath = path.resolve(jarFile);

      if (!Files.exists(jarFilePath)) {
        continue;
      }

      if (this.zipSearch.findInZip(jarFilePath, searchValue)) {
        return true;
      }
    }

    return false;
  }
}
