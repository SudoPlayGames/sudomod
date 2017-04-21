package com.sudoplay.sudoxt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codetaylor on 4/14/2017.
 */
public class MemoryClassLoader extends
    ClassLoader {

  private Map<String, MemoryByteCode> memoryByteCodeMap;

  public MemoryClassLoader() {
    this.memoryByteCodeMap = new HashMap<>();
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    MemoryByteCode memoryByteCode = this.memoryByteCodeMap.get(name);

    if (memoryByteCode == null) {
      memoryByteCode = this.memoryByteCodeMap.get(name.replace(".", "/"));

      if (memoryByteCode == null) {
        throw new ClassNotFoundException(name);
      }

    }

    byte[] bytes = memoryByteCode.getBytes();

    return this.defineClass(name, bytes, 0, bytes.length);

  }

  public void addClass(String name, MemoryByteCode memoryByteCode) {
    this.memoryByteCodeMap.put(name, memoryByteCode);
  }

  public MemoryByteCode getByteCode(String name) {
    return this.memoryByteCodeMap.get(name);
  }

}