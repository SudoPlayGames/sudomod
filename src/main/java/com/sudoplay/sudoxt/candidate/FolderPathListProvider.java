package com.sudoplay.sudoxt.candidate;

import com.sudoplay.sudoxt.util.CloseUtil;
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

  private Path[] paths;

  public FolderPathListProvider(
      Path[] paths
  ) {
    this.paths = paths;
  }

  @Override
  public List<Path> getPathList() throws IOException {
    DirectoryStream<Path> directoryStream;
    List<Path> result;

    directoryStream = null;
    result = new ArrayList<>();

    for (Path path : this.paths) {

      try {
        directoryStream = Files.newDirectoryStream(
            path,
            entry -> Files.isDirectory(entry)
        );

        for (Path p : directoryStream) {
          result.add(p);
        }

      } catch (IOException e) {
        LOG.error("Error getting file path list for [{}]", this.paths, e);

      } finally {
        CloseUtil.close(directoryStream, LOG);
      }
    }

    return result;
  }
}
