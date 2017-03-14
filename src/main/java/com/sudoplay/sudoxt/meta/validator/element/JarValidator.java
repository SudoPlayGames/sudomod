package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class JarValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(JarValidator.class);

  @Override
  public boolean isValid(Meta meta, Path path, List<Meta> metaList) {
    return this.isValidSet(path, meta.getJarFileSet());
  }

  /* package */ boolean isValidSet(Path path, Set<String> jarFileSet) {

    boolean isValid = true;

    for (String jarFileString : jarFileSet) {
      Path jarPath = path.resolve(jarFileString);

      if (!Files.exists(jarPath)) {
        LOG.error("Missing declared jar file [{}]", jarPath);
        isValid = false;
      }

      if (isValid) {

        try {
          //noinspection ResultOfMethodCallIgnored
          jarPath.toUri().toURL();

        } catch (MalformedURLException e) {
          LOG.error("Error converting path [{}] to URL", jarPath, e);
          isValid = false;
        }
      }
    }
    return isValid;
  }
}
