package com.sudoplay.sudomod.mod.info;

import com.sudoplay.sudomod.versioning.ArtifactVersion;
import com.sudoplay.sudomod.versioning.VersionRange;

import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class ModInfo {

  private String id;
  private String name;
  private String author;
  private ArtifactVersion version;
  private String description;
  private String image;
  private String modPlugin;
  private VersionRange apiVersionRange;
  private String repo;
  private ModDependencyContainer modDependencyContainer;
  private List<String> jarFileList;

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setVersion(ArtifactVersion version) {
    this.version = version;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setModPlugin(String modPlugin) {
    this.modPlugin = modPlugin;
  }

  public void setApiVersionRange(VersionRange apiVersionRange) {
    this.apiVersionRange = apiVersionRange;
  }

  public void setRepo(String repo) {
    this.repo = repo;
  }

  public void setModDependencyContainer(ModDependencyContainer modDependencyContainer) {
    this.modDependencyContainer = modDependencyContainer;
  }

  public void setJarFileList(List<String> jarFileList) {
    this.jarFileList = jarFileList;
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getAuthor() {
    return this.author;
  }

  public ArtifactVersion getVersion() {
    return this.version;
  }

  public String getDescription() {
    return this.description;
  }

  public String getImage() {
    return this.image;
  }

  public String getModPlugin() {
    return this.modPlugin;
  }

  public VersionRange getApiVersionRange() {
    return this.apiVersionRange;
  }

  public String getRepo() {
    return this.repo;
  }

  public ModDependencyContainer getModDependencyContainer() {
    return this.modDependencyContainer;
  }

  public List<String> getJarFileList() {
    return this.jarFileList;
  }

  @Override
  public String toString() {
    return "ModInfo{" +
        "id='" + this.id + '\'' +
        ", name='" + this.name + '\'' +
        ", author='" + this.author + '\'' +
        ", version=" + this.version +
        ", description='" + this.description + '\'' +
        ", image=" + this.image +
        ", modPlugin='" + this.modPlugin + '\'' +
        ", apiVersionRange=" + this.apiVersionRange +
        ", repo='" + this.repo + '\'' +
        ", modDependencyContainer=" + this.modDependencyContainer +
        ", jarFileList=" + this.jarFileList +
        '}';
  }
}
