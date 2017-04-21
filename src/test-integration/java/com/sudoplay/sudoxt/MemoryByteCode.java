package com.sudoplay.sudoxt;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by codetaylor on 4/14/2017.
 */
public class MemoryByteCode extends
    SimpleJavaFileObject {

  private ByteArrayOutputStream byteArrayOutputStream;

  public MemoryByteCode(String name) {
    this(URI.create("byte:///" + name + ".class"), Kind.CLASS);
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    throw new IllegalStateException();
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    return (this.byteArrayOutputStream = new ByteArrayOutputStream());
  }

  @Override
  public InputStream openInputStream() throws IOException {
    throw new IllegalStateException();
  }

  public byte[] getBytes() {
    return this.byteArrayOutputStream.toByteArray();
  }

  /**
   * Construct a SimpleJavaFileObject of the given kind and with the
   * given URI.
   *
   * @param uri  the URI for this file object
   * @param kind the kind of this file object
   */
  protected MemoryByteCode(URI uri, Kind kind) {
    super(uri, kind);
  }

}