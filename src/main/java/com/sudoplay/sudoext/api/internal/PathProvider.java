package com.sudoplay.sudoext.api.internal;

import com.sudoplay.sudoext.util.PathUtil;

import java.nio.file.Path;

/**
 * Created by codetaylor on 3/6/2017.
 */
public class PathProvider {

  private Path path;

  public PathProvider(Path path) {
    this.path = path;
  }

  public Path getPath(String path) {
    return this.path.resolve(PathUtil.clean(path));
  }
}
