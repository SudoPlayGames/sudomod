package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import com.sudoplay.sudoxt.service.SXResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class OverrideValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(OverrideValidator.class);

  private PluginFinder pluginFinder;

  public OverrideValidator(PluginFinder pluginFinder) {
    this.pluginFinder = pluginFinder;
  }

  @Override
  public boolean isValid(Meta meta, Path path, List<Meta> metaList) {

    // remote doesn't have to exist
    // local must exist

    Set<String> idSet = metaList
        .stream()
        .map(Meta::getId)
        .collect(Collectors.toSet());

    // if remote id exists, map local location to list
    List<SXResourceLocation> localResourceLocationList = meta
        .getOverrideMap()
        .entrySet()
        .stream()
        .filter(entry -> idSet.contains(entry.getKey().getId()))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());

    // check that each local location in list exists
    return isValidLocalResourceLocation(path, localResourceLocationList, meta.getJarFileSet());
  }

  /* package */ boolean isValidLocalResourceLocation(
      Path path,
      List<SXResourceLocation> resourceLocationList,
      Set<String> jarFileSet
  ) {
    boolean isValid = true;

    for (SXResourceLocation location : resourceLocationList) {

      if (!this.pluginFinder.hasPlugin(location.getResource(), path, jarFileSet)) {
        LOG.error("Missing local override [{}]", location);
        isValid = false;
      }
    }

    return isValid;
  }
}
