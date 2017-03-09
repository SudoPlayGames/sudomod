package com.sudoplay.sudoxt.meta.validator.element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipSearch {

  private static final Logger LOG = LoggerFactory.getLogger(ZipSearch.class);

  public boolean findInZip(Path zipFilePath, String searchValue) {

    try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(zipFilePath))) {

      for (ZipEntry zipEntry = zip.getNextEntry(); zipEntry != null; zipEntry = zip.getNextEntry()) {

        if (!zipEntry.isDirectory()
            && zipEntry.getName().equals(searchValue)) {
          return true;
        }
      }

    } catch (IOException e) {
      LOG.error("Error searching [{}] for [{}]", zipFilePath, searchValue, e);
    }

    return false;
  }
}