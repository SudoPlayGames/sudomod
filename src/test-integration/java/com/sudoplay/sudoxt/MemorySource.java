package com.sudoplay.sudoxt;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by codetaylor on 4/14/2017.
 */
public class MemorySource extends
    SimpleJavaFileObject {

  private String source;

  public MemorySource(String name, String source) {
    super(URI.create("file:///" + name + ".java"), Kind.SOURCE);
    this.source = source;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return this.source;
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public InputStream openInputStream() throws IOException {
    return new ByteArrayInputStream(this.source.getBytes());
  }

  /**
   * Construct a SimpleJavaFileObject of the given kind and with the
   * given URI.
   *
   * @param uri  the URI for this file object
   * @param kind the kind of this file object
   */
  protected MemorySource(URI uri, Kind kind) {
    super(uri, kind);
  }
}
