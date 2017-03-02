package com.sudoplay.sudoext.meta;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class DefaultStringLoader implements
    IStringLoader {

  private Charset charset;

  public DefaultStringLoader(Charset charset) {
    this.charset = charset;
  }

  @Override
  public String load(Path path) throws IOException {
    return new String(Files.readAllBytes(path), this.charset);
  }
}
