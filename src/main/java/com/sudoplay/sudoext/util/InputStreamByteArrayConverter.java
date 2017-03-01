package com.sudoplay.sudoext.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class InputStreamByteArrayConverter {

  public byte[] convert(InputStream inputStream) throws IOException {

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
