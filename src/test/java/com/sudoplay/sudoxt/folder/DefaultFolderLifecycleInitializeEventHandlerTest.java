package com.sudoplay.sudoxt.folder;

import com.sudoplay.sudoxt.service.SXServiceInitializationException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class DefaultFolderLifecycleInitializeEventHandlerTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldCreateFolderOnInitialize() {

    try {
      File file = this.folder.newFolder("test");
      Path path = Paths.get(file.toURI()).resolve("child-folder");

      DefaultFolderLifecycleInitializeEventHandler handler;

      handler = new DefaultFolderLifecycleInitializeEventHandler(path);

      handler.onInitialize();

      Assert.assertTrue(Files.exists(path));

    } catch (IOException | SXServiceInitializationException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}
