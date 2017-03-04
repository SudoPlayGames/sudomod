package com.sudoplay.sudoext.meta.validator.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class RegisterValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(RegisterValidator.class);

  private ZipSearch zipSearch;

  public RegisterValidator(
      ZipSearch zipSearch
  ) {
    this.zipSearch = zipSearch;
  }

  @Override
  public boolean isValid(Meta meta, Path path, List<Meta> metaList) {
    return isValid(
        path,
        meta.getRegisteredPluginMap().values(),
        meta.getJarFileSet()
    );
  }

  /* package */ boolean isValid(Path path, Collection<String> values, Set<String> jarFileSet) {

    boolean isValid = true;

    VALIDATION:
    for (String value : values) {

      // check path for .java file
      if (Files.exists(path.resolve(value.replace(".", "/") + ".java"))) {
        continue;
      }

      // check jars for .class file
      for (String jarFile : jarFileSet) {
        Path jarFilePath = path.resolve(jarFile);

        if (!Files.exists(jarFilePath)) {
          continue;
        }

        if (this.zipSearch.findInZip(jarFilePath, value.replace(".", "/") + ".class")) {
          continue VALIDATION; // found it!
        }
      }

      LOG.error("Failed to locate registered plugin [{}]", value);
      isValid = false;
    }

    return isValid;
  }
}
