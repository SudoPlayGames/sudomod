package com.sudoplay.sudoext.candidate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by codetaylor on 2/25/2017.
 */
public interface IInputStreamProvider {

  InputStream getInputStream(Path path) throws IOException;
}
