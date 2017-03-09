package com.sudoplay.sudoxt.candidate;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/25/2017.
 */
public interface IPathValidator {

  boolean isPathValid(Path path) throws IOException;
}
