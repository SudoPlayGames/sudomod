package com.sudoplay.sudoext.classloader.asm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by codetaylor on 2/28/2017.
 */
public class NoOpByteCodeTransformer implements
    IByteCodeTransformer {

  @Override
  public byte[] transform(InputStream inputStream) throws IOException {

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    int read;
    byte[] buffer = new byte[4096];

    while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
      out.write(buffer, 0, read);
    }

    out.flush();

    return out.toByteArray();
  }
}
