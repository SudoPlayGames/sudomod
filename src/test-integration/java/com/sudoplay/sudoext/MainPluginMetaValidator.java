package com.sudoplay.sudoext;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class MainPluginMetaValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(IMetaValidator.class);

  @Override
  public boolean isValid(Meta meta, Path path, List<Container> containerList) {
    //return this.validateMainPlugin(path, meta.getPlugin(), meta.getJarFileSet());
    return false;
  }

  private boolean validateMainPlugin(Path path, String pluginString, List<String> jarFileList) {
    boolean found = false;

    if (Files.exists(path.resolve(pluginString.replace(".", "/") + ".java"))) {
      found = true;

    } else {

      for (String jarFileString : jarFileList) {
        path = path.resolve(jarFileString);

        if (Files.exists(path)) {

          try {
            ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(path));
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
              String name = zipEntry.getName();

              if (!zipEntry.isDirectory()
                  && name.equals(pluginString + ".class")) {
                found = true;
                break;
              }
            }

            if (found) {
              break;
            }
          } catch (IOException e) {
            LOG.error("Error scanning jar file [{}] for [{}]", path, pluginString);
          }
        }
      }
    }

    if (!found) {
      LOG.error("Can't find [{}]", pluginString);
    }
    return found;
  }
}
