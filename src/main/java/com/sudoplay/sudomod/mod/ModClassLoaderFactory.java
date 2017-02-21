package com.sudoplay.sudomod.mod;

import com.sudoplay.sudomod.mod.ModClassLoader;
import org.codehaus.janino.JavaSourceClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by codetaylor on 2/21/2017.
 */
public class ModClassLoaderFactory {

  private final URL[] urls;
  private File[] sourcePath;

  public ModClassLoaderFactory(Path path, List<String> jarFileList) {
    int size = jarFileList.size();
    this.urls = new URL[size];

    for (int i = 0; i < size; i++) {

      try {
        this.urls[i] = path.resolve(jarFileList.get(i)).toUri().toURL();

      } catch (MalformedURLException e) {
        // this should be prevented by the URL conversion check in the validation phase
      }
    }

    this.sourcePath = new File[]{path.toFile()};
  }

  public ModClassLoader create() {
    return new ModClassLoader(
        this.urls,
        new JavaSourceClassLoader(
            null,
            this.sourcePath,
            null
        ),
        getClass().getClassLoader()
    );
  }

}
