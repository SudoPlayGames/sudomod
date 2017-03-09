package com.sudoplay.sudoxt.folder;

import com.sudoplay.sudoxt.service.SEServiceInitializationException;
import com.sudoplay.sudoxt.util.RecursiveFileRemovalProcessor;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

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

      RecursiveFileRemovalProcessor fileRemovalProcessor = mock(RecursiveFileRemovalProcessor.class);

      TempFolderLifecycleEventHandler handler = new TempFolderLifecycleEventHandler(path, fileRemovalProcessor);

      handler.onInitialize();

      // should create file
      Assert.assertTrue(Files.exists(path));

      handler.onDispose();

      // should call the file removal processor twice, once on init and once on dispose
      verify(fileRemovalProcessor, times(2)).deleteRecursively(path);

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

      RecursiveFileRemovalProcessor fileRemovalProcessor = mock(RecursiveFileRemovalProcessor.class);

      TempFolderLifecycleEventHandler handler = new TempFolderLifecycleEventHandler(path, fileRemovalProcessor);

      Files.createDirectories(path);

      handler.onInitialize();

      // should create file
      Assert.assertTrue(Files.exists(path));

      // should call the file removal processor
      verify(fileRemovalProcessor, times(1)).deleteRecursively(path);

    } catch (IOException | SEServiceInitializationException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

}
