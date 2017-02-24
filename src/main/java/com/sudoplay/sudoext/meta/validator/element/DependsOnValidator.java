package com.sudoplay.sudoext.meta.validator.element;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.DependencyContainer;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import com.sudoplay.sudoext.versioning.ArtifactVersion;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class DependsOnValidator implements
    IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(DependsOnValidator.class);

  @Override
  public boolean isValid(Meta meta, Path path, List<Container> containerList) {
    return this.validateDependsOn(meta.getId(), meta.getDependencyContainer(), containerList);
  }

  private boolean validateDependsOn(
      String id,
      DependencyContainer dependencyContainer,
      List<Container> containerList
  ) {
    boolean isValid = true;

    for (Dependency dependency : dependencyContainer.getRequired()) {
      String dependencyId = dependency.getId();
      VersionRange versionRange = dependency.getVersionRange();
      isValid = this.validateRequiredDependency(id, containerList, dependencyId, versionRange) && isValid;
    }

    for (Dependency dependency : dependencyContainer.getDependencyList()) {
      String dependencyId = dependency.getId();
      VersionRange versionRange = dependency.getVersionRange();
      isValid = this.validateSoftDependency(id, containerList, dependencyId, versionRange) && isValid;
    }

    for (Dependency dependency : dependencyContainer.getDependentList()) {
      String dependencyId = dependency.getId();
      VersionRange versionRange = dependency.getVersionRange();
      isValid = this.validateSoftDependency(id, containerList, dependencyId, versionRange) && isValid;
    }

    return isValid;
  }

  private boolean validateRequiredDependency(
      String id,
      List<Container> containerList,
      String dependencyId,
      VersionRange versionRange
  ) {

    for (Container container : containerList) {
      String candidateId = container.getMeta().getId();

      if (dependencyId.equals(candidateId)) {
        ArtifactVersion candidateVersion = container.getMeta().getVersion();

        if (versionRange.containsVersion(candidateVersion)) {
          return true;

        } else {
          LOG.error("[{}] requires a version within {} of [{}], found incompatible version [{}]",
              id, versionRange, dependencyId, candidateVersion
          );
          return false;
        }
      }
    }

    LOG.error("[{}] is missing a required dependency, [{}]", id, dependencyId);
    return false;
  }

  private boolean validateSoftDependency(
      String id,
      List<Container> containerList,
      String dependencyId,
      VersionRange versionRange
  ) {

    for (Container container : containerList) {
      String candidateId = container.getMeta().getId();

      if (dependencyId.equals(candidateId)) {
        ArtifactVersion candidateVersion = container.getMeta().getVersion();

        if (versionRange.containsVersion(candidateVersion)) {
          return true;

        } else {
          LOG.error("[{}] requires a version within {} of [{}], found incompatible version [{}]",
              id, versionRange, dependencyId, candidateVersion
          );
          return false;
        }
      }
    }

    return true;
  }
}
