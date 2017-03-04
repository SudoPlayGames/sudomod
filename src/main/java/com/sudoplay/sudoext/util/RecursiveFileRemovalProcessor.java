package com.sudoplay.sudoext.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class RecursiveFileRemovalProcessor {

  public void deleteRecursively(Path path) throws IOException {

    if (!Files.exists(path)) {
      return;
    }

    Files.walkFileTree(
        path,
        EnumSet.noneOf(FileVisitOption.class), // prevents following symlinks
        Integer.MAX_VALUE, // max depth
        new SimpleFileVisitor<Path>() {

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
        }
    );
  }
}
