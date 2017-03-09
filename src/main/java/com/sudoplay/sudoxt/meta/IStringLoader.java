package com.sudoplay.sudoxt.meta;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by codetaylor on 3/1/2017.
 */
public interface IStringLoader {

  String load(Path path) throws IOException;
}
