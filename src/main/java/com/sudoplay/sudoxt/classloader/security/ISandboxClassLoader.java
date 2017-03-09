package com.sudoplay.sudoxt.classloader.security;

import java.nio.file.Path;

/**
 * Used as a marker to identify classes that need to be restricted.
 * <p>
 * Created by codetaylor on 2/24/2017.
 */
public interface ISandboxClassLoader {

  Path getPath();
}
