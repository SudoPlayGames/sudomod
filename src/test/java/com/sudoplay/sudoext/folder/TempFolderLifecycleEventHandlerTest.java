package com.sudoplay.sudoext.folder;

import com.sudoplay.sudoext.service.SEServiceInitializationException;
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
public class TempFolderLifecycleEventHandlerTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldCreateAndRemoveFolderAndContents() {

    try {
      File file = this.folder.newFolder("test");
      Path path = Paths.get(file.toURI()).resolve("child-folder");

      TempFolderLifecycleEventHandler handler = new TempFolderLifecycleEventHandler(path);

      handler.onInitialize();

      // should create file
      Assert.assertTrue(Files.exists(path));

      Files.newOutputStream(path.resolve("test-file-0.txt")).close();
      Files.newOutputStream(path.resolve("test-file-1.txt")).close();
      Files.newOutputStream(path.resolve("test-file-2.txt")).close();

      Path nestedPath = path.resolve("folder-a/folder-b");
      Files.createDirectories(nestedPath);
      Files.newOutputStream(nestedPath.resolve("test-file-3.txt")).close();
      Files.newOutputStream(nestedPath.resolve("test-file-4.txt")).close();
      Files.newOutputStream(nestedPath.resolve("test-file-5.txt")).close();

      handler.onDispose();

      // should clean up recursively
      Assert.assertFalse(Files.exists(path));

    } catch (IOException | SEServiceInitializationException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void shouldRemoveFolderAndContentsOnInitialize() {

    try {
      File file = this.folder.newFolder("test");
      Path path = Paths.get(file.toURI()).resolve("child-folder");

      TempFolderLifecycleEventHandler handler = new TempFolderLifecycleEventHandler(path);

      Files.createDirectories(path);

      Files.newOutputStream(path.resolve("test-file-0.txt")).close();
      Files.newOutputStream(path.resolve("test-file-1.txt")).close();
      Files.newOutputStream(path.resolve("test-file-2.txt")).close();

      Path nestedPath = path.resolve("folder-a/folder-b");
      Files.createDirectories(nestedPath);
      Files.newOutputStream(nestedPath.resolve("test-file-3.txt")).close();
      Files.newOutputStream(nestedPath.resolve("test-file-4.txt")).close();
      Files.newOutputStream(nestedPath.resolve("test-file-5.txt")).close();

      handler.onInitialize();

      // should create file
      Assert.assertTrue(Files.exists(path));

      // should be empty
      Assert.assertEquals(0, Files.list(path).count());

    } catch (IOException | SEServiceInitializationException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

}
