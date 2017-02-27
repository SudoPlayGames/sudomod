package com.sudoplay.sudoext.candidate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/25/2017.
 */
public interface IPathListProvider {

  List<Path> getPathList() throws IOException;
}
