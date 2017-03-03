package com.sudoplay.sudoext.meta.validator.element;

import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import com.sudoplay.sudoext.versioning.ArtifactVersion;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class DependsOnValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(DependsOnValidator.class);

  @Override
  public boolean isValid(Meta meta, Path path, List<Meta> metaList) {

    Map<String, Meta> metaMap = new LinkedHashMap<>();

    for (Meta m : metaList) {
      metaMap.put(m.getId(), m);
    }

    boolean isValid;
    String id = meta.getId();

    isValid = this.validateRequired(id, meta.getRequiredDependencySet(), metaMap);
    isValid = this.validateSoft(id, meta.getLoadAfterDependencySet(), metaMap) && isValid;
    isValid = this.validateSoft(id, meta.getLoadBeforeDependencySet(), metaMap) && isValid;

    return isValid;
  }

  /* package */ boolean validateRequired(
      String id,
      Collection<Dependency> dependencyCollection,
      Map<String, Meta> metaMap
  ) {
    boolean isValid = true;

    for (Dependency dependency : dependencyCollection) {
      String dependencyId = dependency.getId();
      VersionRange versionRange = dependency.getVersionRange();
      Meta meta = metaMap.get(dependencyId);

      if (meta != null) {
        ArtifactVersion version = meta.getVersion();

        if (!versionRange.containsVersion(version)) {
          isValid = false;
          LOG.error(
              "[{}] requires version {} of [{}], found incompatible version [{}]",
              id, versionRange, meta.getId(), version
          );
        }

      } else {
        isValid = false;
        LOG.error("[{}] is missing a required dependency, [{}]", id, dependencyId);
      }
    }

    return isValid;
  }

  /* package */ boolean validateSoft(
      String id,
      Collection<Dependency> dependencyCollection,
      Map<String, Meta> metaMap
  ) {
    boolean isValid = true;

    for (Dependency dependency : dependencyCollection) {
      String dependencyId = dependency.getId();
      VersionRange versionRange = dependency.getVersionRange();
      Meta meta = metaMap.get(dependencyId);

      if (meta != null) {
        ArtifactVersion version = meta.getVersion();

        if (!versionRange.containsVersion(version)) {
          isValid = false;
          LOG.error(
              "[{}] requires version {} of [{}], found incompatible version [{}]",
              id, versionRange, meta.getId(), version
          );
        }
      }
    }

    return isValid;
  }
}
