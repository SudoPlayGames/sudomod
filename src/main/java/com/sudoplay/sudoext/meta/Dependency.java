package com.sudoplay.sudoext.meta;

import com.sudoplay.sudoext.versioning.VersionRange;

/**
 * Created by codetaylor on 2/18/2017.
 */
public class Dependency {

  private String id;
  private LoadOrder loadOrder;
  private VersionRange versionRange;

  public Dependency(String id, LoadOrder loadOrder) {
    this(id, loadOrder, null);
  }

  public Dependency(String id, LoadOrder loadOrder, VersionRange versionRange) {
    this.id = id;
    this.loadOrder = loadOrder;
    this.versionRange = versionRange;
  }

  public boolean isAll() {
    return "*".equals(this.id);
  }

  public String getId() {
    return this.id;
  }

  public LoadOrder getLoadOrder() {
    return this.loadOrder;
  }

  public boolean isUnbounded() {
    return this.versionRange == null;
  }

  public VersionRange getVersionRange() {
    return this.versionRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Dependency that = (Dependency) o;

    if (!id.equals(that.id)) return false;
    if (loadOrder != that.loadOrder) return false;
    return versionRange != null ? versionRange.equals(that.versionRange) : that.versionRange == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + loadOrder.hashCode();
    result = 31 * result + (versionRange != null ? versionRange.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Dependency{" + this.loadOrder.getKey() + ":" + this.id + (this.versionRange == null ? "" : "@" + this
        .versionRange) + "}";
  }
}
