package com.sudoplay.sudoext.meta;

import com.sudoplay.sudoext.versioning.ArtifactVersion;
import com.sudoplay.sudoext.versioning.VersionRange;

import java.util.List;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Meta {

  private String id;
  private String name;
  private String author;
  private ArtifactVersion version;
  private String description;
  private String image;
  private String plugin;
  private VersionRange apiVersionRange;
  private String repo;
  private DependencyContainer dependencyContainer;
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

  public void setPlugin(String plugin) {
    this.plugin = plugin;
  }

  public void setApiVersionRange(VersionRange apiVersionRange) {
    this.apiVersionRange = apiVersionRange;
  }

  public void setRepo(String repo) {
    this.repo = repo;
  }

  public void setDependencyContainer(DependencyContainer dependencyContainer) {
    this.dependencyContainer = dependencyContainer;
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

  public String getPlugin() {
    return this.plugin;
  }

  public VersionRange getApiVersionRange() {
    return this.apiVersionRange;
  }

  public String getRepo() {
    return this.repo;
  }

  public DependencyContainer getDependencyContainer() {
    return this.dependencyContainer;
  }

  public List<String> getJarFileList() {
    return this.jarFileList;
  }

  @Override
  public String toString() {
    return "Meta{" +
        "id='" + this.id + '\'' +
        ", name='" + this.name + '\'' +
        ", author='" + this.author + '\'' +
        ", version=" + this.version +
        ", description='" + this.description + '\'' +
        ", image=" + this.image +
        ", plugin='" + this.plugin + '\'' +
        ", apiVersionRange=" + this.apiVersionRange +
        ", repo='" + this.repo + '\'' +
        ", dependencyContainer=" + this.dependencyContainer +
        ", jarFileList=" + this.jarFileList +
        '}';
  }
}
