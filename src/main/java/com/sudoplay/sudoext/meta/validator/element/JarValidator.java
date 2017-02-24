package com.sudoplay.sudoext.meta.validator.element;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class JarValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(JarValidator.class);

  @Override
  public boolean isValid(Meta meta, Path path, List<Container> containerList) {
    return this.validateJarFileList(path, meta.getJarFileList());
  }

  private boolean validateJarFileList(Path path, List<String> jarFileList) {

    boolean isValid = true;

    for (String jarFileString : jarFileList) {
      path = path.resolve(jarFileString);

      if (!Files.exists(path)) {
        LOG.error("Missing declared jar file [{}]", path);
        isValid = false;
      }

      if (isValid) {

        try {
          //noinspection ResultOfMethodCallIgnored
          path.toUri().toURL();

        } catch (MalformedURLException e) {
          LOG.error("Error converting path [{}] to URL", path, e);
          isValid = false;
        }
      }
    }
    return isValid;
  }
}
