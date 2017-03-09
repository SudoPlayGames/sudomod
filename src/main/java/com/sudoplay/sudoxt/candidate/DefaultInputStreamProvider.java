package com.sudoplay.sudoxt.candidate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/27/2017.
 */
public class DefaultInputStreamProvider implements
    IInputStreamProvider {

  @Override
  public InputStream getInputStream(Path path) throws IOException {
    return Files.newInputStream(path);
  }
}
