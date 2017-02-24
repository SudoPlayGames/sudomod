package com.sudoplay.sudoext.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by codetaylor on 2/20/2017.
 */
public class FileUtils {

  public static void deleteRecursively(Path path) throws IOException {

    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  public static void close(InputStream inputStream) {

    if (inputStream == null) {
      return;
    }

    try {
      inputStream.close();

    } catch (IOException e) {
      //
    }
  }

  private FileUtils() {
    //
  }
}
