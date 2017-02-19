package com.sudoplay.sudomod.mod.info;

import com.sudoplay.sudomod.versioning.VersionRange;

/**
 * Created by sk3lls on 2/18/2017.
 */
public class ModDependency {

  private String id;
  private LoadOrder loadOrder;
  private VersionRange versionRange;

  public ModDependency(String id, LoadOrder loadOrder) {
    this(id, loadOrder, null);
  }

  public ModDependency(String id, LoadOrder loadOrder, VersionRange versionRange) {
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

    ModDependency that = (ModDependency) o;

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
    return "ModDependency{" + this.loadOrder.getKey() + ":" + this.id + (this.versionRange == null ? "" : "@" + this
        .versionRange) + "}";
  }
}
