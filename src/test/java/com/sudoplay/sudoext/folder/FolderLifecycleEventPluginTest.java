package com.sudoplay.sudoext.folder;

import com.sudoplay.sudoext.service.SEServiceInitializationException;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class FolderLifecycleEventPluginTest {

  @Test
  public void shouldCallInitializeOnHandlers() {

    IFolderLifecycleInitializeEventHandler mock = mock(IFolderLifecycleInitializeEventHandler.class);

    FolderLifecycleEventPlugin plugin = new FolderLifecycleEventPlugin(
        new IFolderLifecycleEventHandler[]{
            mock
        }
    );

    try {
      plugin.initialize();

      verify(mock, times(1)).onInitialize();

    } catch (SEServiceInitializationException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void shouldCallDisposeOnHandlers() {

    IFolderLifecycleDisposeEventHandler mock = mock(IFolderLifecycleDisposeEventHandler.class);

    FolderLifecycleEventPlugin plugin = new FolderLifecycleEventPlugin(
        new IFolderLifecycleEventHandler[]{
            mock
        }
    );

    plugin.dispose();

    verify(mock, times(1)).onDispose();
  }
}
