package com.sudoplay.sudoext.meta.validator.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by codetaylor on 3/6/2017.
 */
public class PreloadValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(PreloadValidator.class);

  private PluginFinder pluginFinder;

  public PreloadValidator(PluginFinder pluginFinder) {
    this.pluginFinder = pluginFinder;
  }

  @Override
  public boolean isValid(Meta meta, Path path, List<Meta> metaList) {
    return isValid(path, meta.getPreloadSet(), meta.getJarFileSet());
  }

  /* package */ boolean isValid(Path path, Collection<String> preloadCollection, Set<String> jarFileSet) {

    boolean isValid = true;

    for (String value : preloadCollection) {

      if (!this.pluginFinder.hasPlugin(value, path, jarFileSet)) {
        LOG.error("Failed to locate plugin [{}]", value);
        isValid = false;
      }
    }

    return isValid;
  }
}
