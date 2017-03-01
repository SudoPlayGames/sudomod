package com.sudoplay.sudoext.classloader.asm;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by codetaylor on 2/28/2017.
 */
public interface IByteCodeTransformer {

  byte[] transform(InputStream inputStream) throws IOException;
}
