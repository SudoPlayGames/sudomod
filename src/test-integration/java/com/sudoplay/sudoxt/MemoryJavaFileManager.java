package com.sudoplay.sudoxt;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 4/14/2017.
 */
public class MemoryJavaFileManager extends
    ForwardingJavaFileManager {

  private Map<String, MemoryByteCode> memoryByteCodeMap;

  /**
   * Creates a new instance of ForwardingJavaFileManager.
   *
   * @param fileManager delegate to this file manager
   */
  protected MemoryJavaFileManager(JavaFileManager fileManager) {
    super(fileManager);
    this.memoryByteCodeMap = new HashMap<>();
  }

  @Override
  public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
    MemoryByteCode memoryByteCode = new MemoryByteCode(className);
    this.memoryByteCodeMap.put(className, memoryByteCode);
    return memoryByteCode;
  }

  @Override
  public ClassLoader getClassLoader(Location location) {
    return null;
  }

  public MemoryByteCode getByteCode(String name) {
    return this.memoryByteCodeMap.get(name);
  }

}
