package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private PluginFinder pluginFinder;

  public RegisterValidator(PluginFinder pluginFinder) {
    this.pluginFinder = pluginFinder;
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

    for (String value : values) {

      if (!this.pluginFinder.hasPlugin(value, path, jarFileSet)) {
        LOG.error("Failed to locate plugin [{}]", value);
        isValid = false;
      }
    }

    return isValid;
  }
}
