package com.sudoplay.sudoext.candidate.locator;

import com.sudoplay.sudoext.util.CloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class FolderPathListProvider implements
    IPathListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(FolderPathListProvider.class);

  private Path path;

  public FolderPathListProvider(
      Path path
  ) {
    this.path = path;
  }

  @Override
  public List<Path> getPathList() throws IOException {
    DirectoryStream<Path> directoryStream;
    List<Path> result;

    directoryStream = null;
    result = new ArrayList<>();

    try {
      directoryStream = Files.newDirectoryStream(
          this.path,
          entry -> Files.isDirectory(entry)
      );

      for (Path path : directoryStream) {
        result.add(path);
      }

    } catch (IOException e) {
      LOG.error("Error getting compressed file path list for [{}]", this.path, e);

    } finally {
      CloseUtil.close(directoryStream, LOG);
    }

    return result;
  }
}
