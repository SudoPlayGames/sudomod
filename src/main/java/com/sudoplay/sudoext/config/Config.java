package com.sudoplay.sudoext.config;

import com.sudoplay.sudoext.classloader.intercept.ClassIntercept;
import com.sudoplay.sudoext.container.DefaultContainerSorter;
import com.sudoplay.sudoext.container.IContainerCacheFactory;
import com.sudoplay.sudoext.container.IContainerSorter;
import com.sudoplay.sudoext.container.LRUContainerCacheFactory;
import com.sudoplay.sudoext.meta.DefaultMetaFactory;
import com.sudoplay.sudoext.meta.IMetaFactory;
import com.sudoplay.sudoext.security.IClassFilter;
import com.sudoplay.sudoext.versioning.ArtifactVersion;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration context.
 * <p>
 * Created by codetaylor on 2/18/2017.
 */
public class Config {

  /* package */ Path dataLocation;
  /* package */ Path location;
  /* package */ String metaFilename;
  /* package */ String defaultMetaApiVersionString;
  /* package */ boolean followLinks;
  /* package */ Path tempLocation;
  /* package */ String compressedFileExtension;
  /* package */ ArtifactVersion apiVersion;
  /* package */ List<IClassFilter> classFilterList;
  /* package */ IContainerCacheFactory containerCacheFactory;
  /* package */ List<ClassIntercept> classInterceptList;
  /* package */ IContainerSorter containerSorter;
  /* package */ IMetaFactory metaFactory;

  /* package */ Config() {
    this.defaultMetaApiVersionString = "[0,)";
    this.classFilterList = new ArrayList<>();
    this.containerCacheFactory = new LRUContainerCacheFactory(64);
    this.classInterceptList = new ArrayList<>();
    this.containerSorter = new DefaultContainerSorter();
    this.metaFactory = new DefaultMetaFactory();
  }

  public Path getDataLocation() {
    return this.dataLocation;
  }

  public Path getLocation() {
    return this.location;
  }

  public String getMetaFilename() {
    return this.metaFilename;
  }

  public String getDefaultMetaApiVersionString() {
    return this.defaultMetaApiVersionString;
  }

  public boolean isFollowLinks() {
    return this.followLinks;
  }

  public Path getTempLocation() {
    return this.tempLocation;
  }

  public String getCompressedFileExtension() {
    return this.compressedFileExtension;
  }

  public IClassFilter[] getClassFilters() {
    return this.classFilterList.toArray(new IClassFilter[this.classFilterList.size()]);
  }

  public ArtifactVersion getApiVersion() {
    return this.apiVersion;
  }

  public IContainerCacheFactory getContainerCacheFactory() {
    return this.containerCacheFactory;
  }

  public ClassIntercept[] getClassIntercepts() {
    return this.classInterceptList.toArray(new ClassIntercept[this.classInterceptList.size()]);
  }

  public IContainerSorter getContainerSorter() {
    return this.containerSorter;
  }

  public IMetaFactory getMetaFactory() {
    return this.metaFactory;
  }
}
