package com.sudoplay.sudomod.mod.info.validator;

import com.sudoplay.sudomod.mod.container.ModContainer;
import com.sudoplay.sudomod.mod.info.ModDependency;
import com.sudoplay.sudomod.mod.info.ModDependencyContainer;
import com.sudoplay.sudomod.mod.info.ModInfo;
import com.sudoplay.sudomod.versioning.ArtifactVersion;
import com.sudoplay.sudomod.versioning.VersionRange;
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
public class ModInfoValidator implements IModInfoValidator {

  private static final Logger LOG = LoggerFactory.getLogger(ModInfoValidator.class);

  private ArtifactVersion apiVersion;

  public ModInfoValidator(ArtifactVersion apiVersion) {
    this.apiVersion = apiVersion;
  }

  @Override
  public boolean isValid(ModInfo modInfo, Path modPath, List<ModContainer> modContainerList) {

    boolean isValid;

    isValid = validateId(modInfo.getId(), modContainerList);

    if (!validateModPlugin(modPath, modInfo.getModPlugin(), modInfo.getJarFileList())) {
      isValid = false;
    }

    if (!validateImage(modPath, modInfo.getImage())) {
      isValid = false;
    }

    if (!validateApiVersion(modInfo.getApiVersionRange())) {
      isValid = false;
    }

    if (!validateJarFileList(modPath, modInfo.getJarFileList())) {
      isValid = false;
    }

    if (!validateModDependsOn(modInfo.getId(), modInfo.getModDependencyContainer(), modContainerList)) {
      isValid = false;
    }

    return isValid;
  }

  private boolean validateModDependsOn(
      String modId,
      ModDependencyContainer modDependencyContainer,
      List<ModContainer> modContainerList
  ) {
    boolean isValid = true;

    for (ModDependency modDependency : modDependencyContainer.getRequired()) {
      String id = modDependency.getId();
      VersionRange versionRange = modDependency.getVersionRange();
      isValid = validateRequiredDependency(modId, modContainerList, id, versionRange) && isValid;
    }

    for (ModDependency modDependency : modDependencyContainer.getDependencyList()) {
      String id = modDependency.getId();
      VersionRange versionRange = modDependency.getVersionRange();
      isValid = validateSoftDependency(modId, modContainerList, id, versionRange) && isValid;
    }

    for (ModDependency modDependency : modDependencyContainer.getDependentList()) {
      String id = modDependency.getId();
      VersionRange versionRange = modDependency.getVersionRange();
      isValid = validateSoftDependency(modId, modContainerList, id, versionRange) && isValid;
    }

    return isValid;
  }

  private boolean validateRequiredDependency(
      String modId,
      List<ModContainer> modContainerList,
      String id,
      VersionRange versionRange
  ) {

    for (ModContainer modContainer : modContainerList) {
      String candidateId = modContainer.getModInfo().getId();

      if (id.equals(candidateId)) {
        ArtifactVersion candidateVersion = modContainer.getModInfo().getVersion();

        if (versionRange.containsVersion(candidateVersion)) {
          return true;

        } else {
          LOG.error("Mod [{}] requires a version within {} of mod [{}], found incompatible version [{}]",
              modId, versionRange, id, candidateVersion
          );
          return false;
        }
      }
    }

    LOG.error("Mod [{}] is missing a required dependency, mod [{}]", modId, id);
    return false;
  }

  private boolean validateSoftDependency(
      String modId,
      List<ModContainer> modContainerList,
      String id,
      VersionRange versionRange
  ) {

    for (ModContainer modContainer : modContainerList) {
      String candidateId = modContainer.getModInfo().getId();

      if (id.equals(candidateId)) {
        ArtifactVersion candidateVersion = modContainer.getModInfo().getVersion();

        if (versionRange.containsVersion(candidateVersion)) {
          return true;

        } else {
          LOG.error("Mod [{}] requires a version within {} of mod [{}], found incompatible version [{}]",
              modId, versionRange, id, candidateVersion
          );
          return false;
        }
      }
    }

    return true;
  }

  private boolean validateJarFileList(Path modPath, List<String> jarFileList) {

    boolean isValid = true;

    for (String jarFileString : jarFileList) {
      Path path = modPath.resolve(jarFileString);

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

  private boolean validateModPlugin(Path modPath, String modPlugin, List<String> jarFileList) {
    boolean found = false;

    if (Files.exists(modPath.resolve(modPlugin.replace(".", "/") + ".java"))) {
      found = true;

    } else {

      for (String jarFileString : jarFileList) {
        Path path = modPath.resolve(jarFileString);

        if (Files.exists(path)) {

          try {
            ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(path));
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
              String name = zipEntry.getName();

              if (!zipEntry.isDirectory()
                  && name.equals(modPlugin + ".class")) {
                found = true;
                break;
              }
            }

            if (found) {
              break;
            }
          } catch (IOException e) {
            LOG.error("Error scanning jar file [{}] for mod plugin [{}]", path, modPlugin);
          }
        }
      }
    }

    if (!found) {
      LOG.error("Can't find mod plugin [{}]", modPlugin);
    }
    return found;
  }

  private boolean validateApiVersion(VersionRange apiVersionRange) {
    boolean containsVersion = apiVersionRange.containsVersion(this.apiVersion);

    if (!containsVersion) {
      LOG.error("Current api version [{}] falls outside of mod's api version constraints [{}]", this.apiVersion,
          apiVersionRange);
      return false;
    }

    return true;
  }

  private boolean validateImage(Path modPath, String image) {
    Path path = modPath.resolve(image);

    if (!image.isEmpty() && !Files.exists(path)) {
      LOG.error("Missing image file [{}]", path);
      return false;
    }

    return true;
  }

  private boolean validateId(String id, List<ModContainer> modContainerList) {

    if (id == null || id.isEmpty()) {
      LOG.error("Mod info [id] is invalid: %s; must not be empty or null, valid characters are a-z 0-9 - _", id);
      return false;
    }

    if (id.replaceAll("[a-z0-9-_]", "").length() > 0) {
      LOG.error("Mod info [id] is invalid: %s; must not be empty or null, valid characters are a-z 0-9 - _", id);
      return false;
    }

    int modIdCount = 0;

    for (ModContainer modContainer : modContainerList) {

      if (id.equals(modContainer.getModInfo().getId())) {
        modIdCount += 1;
      }
    }

    boolean uniqueModId = modIdCount == 1;

    if (!uniqueModId) {
      LOG.error("Duplicate mod id found [{}], mod id's must be unique", id);
    }

    return uniqueModId;
  }

}
