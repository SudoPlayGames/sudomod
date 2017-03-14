package com.sudoplay.sudoxt.candidate;

import com.sudoplay.sudoxt.util.CloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class FileExtensionPathListProvider implements
    IPathListProvider {

  private static final Logger LOG = LoggerFactory.getLogger(FileExtensionPathListProvider.class);

  private Path[] paths;
  private String compressedFileExtension;

  public FileExtensionPathListProvider(Path[] paths, String compressedFileExtension) {
    this.paths = paths;
    this.compressedFileExtension = compressedFileExtension;
  }

  @Override
  public List<Path> getPathList() throws IOException {
    DirectoryStream<Path> directoryStream;
    List<Path> result;

    directoryStream = null;
    result = new ArrayList<>();

    for (Path path : this.paths) {

      try {
        // find all compressed files
        PathMatcher matcher = FileSystems.getDefault()
            .getPathMatcher("glob:**." + this.compressedFileExtension);

        directoryStream = Files.newDirectoryStream(
            path,
            entry -> Files.isRegularFile(entry) && matcher.matches(entry)
        );

        for (Path p : directoryStream) {
          result.add(p);
        }

      } catch (IOException e) {
        LOG.error("Error getting file extension [{}] path list for [{}]", this.compressedFileExtension, this.paths);

      } finally {
        CloseUtil.close(directoryStream, LOG);
      }
    }

    return result;
  }
}
