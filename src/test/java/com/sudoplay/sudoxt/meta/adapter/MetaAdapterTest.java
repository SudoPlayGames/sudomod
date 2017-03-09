package com.sudoplay.sudoxt.meta.adapter;

import com.sudoplay.sudoxt.meta.MetaAdaptException;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class MetaAdapterTest {

  @SuppressWarnings("unchecked")
  @Test
  public void adaptShouldDispatchToAllRegisteredAdapters() throws Exception {

    IMetaAdapter adapterA = mock(IMetaAdapter.class);
    IMetaAdapter adapterB = mock(IMetaAdapter.class);
    IMetaAdapter adapterC = mock(IMetaAdapter.class);

    MetaAdapter metaAdapter = new MetaAdapter(new IMetaAdapter[]{
        adapterA,
        adapterB,
        adapterC
    });

    metaAdapter.adapt(null, null);

    verify(adapterA, times(1)).adapt(null, null);
    verify(adapterB, times(1)).adapt(null, null);
    verify(adapterC, times(1)).adapt(null, null);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = MetaAdaptException.class)
  public void adaptShouldDispatchToAllRegisteredAdaptersIfAnyThrow() throws Exception {

    IMetaAdapter adapterA = mock(IMetaAdapter.class);
    IMetaAdapter adapterB = mock(IMetaAdapter.class);
    IMetaAdapter adapterC = mock(IMetaAdapter.class);

    doThrow(Exception.class).when(adapterB).adapt(null, null);

    MetaAdapter metaAdapter = new MetaAdapter(new IMetaAdapter[]{
        adapterA,
        adapterB,
        adapterC
    });

    metaAdapter.adapt(null, null);

    verify(adapterA, times(1)).adapt(null, null);
    verify(adapterB, times(1)).adapt(null, null);
    verify(adapterC, times(1)).adapt(null, null);
  }

}