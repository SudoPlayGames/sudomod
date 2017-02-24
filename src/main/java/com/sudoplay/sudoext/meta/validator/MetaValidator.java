package com.sudoplay.sudoext.meta.validator;

import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.DependencyContainer;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.versioning.ArtifactVersion;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class MetaValidator implements IMetaValidator {

  private static final Logger LOG = LoggerFactory.getLogger(MetaValidator.class);

  private ArtifactVersion apiVersion;

  public MetaValidator(ArtifactVersion apiVersion) {
    this.apiVersion = apiVersion;
  }

  @Override
  public boolean isValid(Meta meta, Path path, List<Container> containerList) {

    boolean isValid;

    isValid = validateId(meta.getId(), containerList);

    if (!validatePlugin(path, meta.getPlugin(), meta.getJarFileList())) {
      isValid = false;
    }

    if (!validateImage(path, meta.getImage())) {
      isValid = false;
    }

    if (!validateApiVersion(meta.getApiVersionRange())) {
      isValid = false;
    }

    if (!validateJarFileList(path, meta.getJarFileList())) {
      isValid = false;
    }

    if (!validateDependsOn(meta.getId(), meta.getDependencyContainer(), containerList)) {
      isValid = false;
    }

    return isValid;
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
      isValid = validateRequiredDependency(id, containerList, dependencyId, versionRange) && isValid;
    }

    for (Dependency dependency : dependencyContainer.getDependencyList()) {
      String dependencyId = dependency.getId();
      VersionRange versionRange = dependency.getVersionRange();
      isValid = validateSoftDependency(id, containerList, dependencyId, versionRange) && isValid;
    }

    for (Dependency dependency : dependencyContainer.getDependentList()) {
      String dependencyId = dependency.getId();
      VersionRange versionRange = dependency.getVersionRange();
      isValid = validateSoftDependency(id, containerList, dependencyId, versionRange) && isValid;
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

  private boolean validatePlugin(Path path, String pluginString, List<String> jarFileList) {
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

  private boolean validateApiVersion(VersionRange apiVersionRange) {
    boolean containsVersion = apiVersionRange.containsVersion(this.apiVersion);

    if (!containsVersion) {
      LOG.error("Current api version [{}] falls outside of api version constraints [{}]", this.apiVersion,
          apiVersionRange);
      return false;
    }

    return true;
  }

  private boolean validateImage(Path path, String image) {
    path = path.resolve(image);

    if (!image.isEmpty() && !Files.exists(path)) {
      LOG.error("Missing image file [{}]", path);
      return false;
    }

    return true;
  }

  private boolean validateId(String id, List<Container> containerList) {

    if (id == null || id.isEmpty()) {
      LOG.error("Meta [id] is invalid: %s; must not be empty or null, valid characters are a-z 0-9 - _", id);
      return false;
    }

    if (id.replaceAll("[a-z0-9-_]", "").length() > 0) {
      LOG.error("Meta [id] is invalid: %s; must not be empty or null, valid characters are a-z 0-9 - _", id);
      return false;
    }

    int idCount = 0;

    for (Container container : containerList) {

      if (id.equals(container.getMeta().getId())) {
        idCount += 1;
      }
    }

    boolean uniqueId = (idCount == 1);

    if (!uniqueId) {
      LOG.error("Duplicate id found [{}], ids must be unique", id);
    }

    return uniqueId;
  }

}
