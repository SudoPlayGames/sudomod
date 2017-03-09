package com.sudoplay.sudoxt.container;

import java.nio.file.Path;

/**
 * Created by codetaylor on 3/6/2017.
 */
public class SandboxPathProvider {

  private Path path;

  public SandboxPathProvider(Path path) {
    this.path = path;
  }

  public Path getPath(String path) {
    return this.path.resolve(this.clean(path));
  }

  String clean(String path) {
    path = path.replaceAll("\\\\", "/");
    path = path.replaceAll("/{2,}", "/");
    path = path.replaceAll("[\\.]{2,}", ".");
    path = path.replaceAll("\\./", "");
    path = path.replaceAll("[^a-zA-Z0-9-_/\\.]", "");

    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    return path;
  }
}
