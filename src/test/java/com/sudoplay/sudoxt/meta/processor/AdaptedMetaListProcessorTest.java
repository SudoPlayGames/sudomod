package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.IMetaJsonLoader;
import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class AdaptedMetaListProcessorTest {

  @Test
  public void processLocalShouldDelegate() throws Exception {
    IMetaAdapter metaAdapter = mock(IMetaAdapter.class);
    IMetaJsonLoader metaJsonProvider = mock(IMetaJsonLoader.class);
    Meta meta = mock(Meta.class);

    AdaptedMetaListProcessor provider = new AdaptedMetaListProcessor(
        null,
        metaAdapter,
        metaJsonProvider
    );

    provider.processLocal(new ArrayList<>(Collections.singletonList(meta)));

    verify(metaJsonProvider, times(1)).load(any());
    verify(metaAdapter, times(1)).adapt(any(), any());
  }

  @Test
  public void processLocalShouldSetMetaInvalidWhenJsonFailsToLoad() throws Exception {
    IMetaAdapter metaAdapter = mock(IMetaAdapter.class);
    IMetaJsonLoader metaJsonProvider = mock(IMetaJsonLoader.class);
    Meta meta = mock(Meta.class);

    when(metaJsonProvider.load(any())).thenThrow(IOException.class);

    AdaptedMetaListProcessor provider = new AdaptedMetaListProcessor(
        null,
        metaAdapter,
        metaJsonProvider
    );

    provider.processLocal(new ArrayList<>(Collections.singletonList(meta)));

    verify(metaJsonProvider, times(1)).load(any());
    verify(metaAdapter, never()).adapt(any(), any());
    verify(meta, times(1)).setValid(false);
  }

  @Test
  public void processLocalShouldSetMetaInvalidWhenJsonFailsToParse() throws Exception {
    IMetaAdapter metaAdapter = mock(IMetaAdapter.class);
    IMetaJsonLoader metaJsonProvider = mock(IMetaJsonLoader.class);
    Meta meta = mock(Meta.class);

    when(metaJsonProvider.load(any())).thenThrow(JSONException.class);

    AdaptedMetaListProcessor provider = new AdaptedMetaListProcessor(
        null,
        metaAdapter,
        metaJsonProvider
    );

    provider.processLocal(new ArrayList<>(Collections.singletonList(meta)));

    verify(metaJsonProvider, times(1)).load(any());
    verify(metaAdapter, never()).adapt(any(), any());
    verify(meta, times(1)).setValid(false);
  }

  @Test
  public void processLocalShouldSetMetaInvalidWhenJsonFailsToAdapt() throws Exception {
    IMetaAdapter metaAdapter = mock(IMetaAdapter.class);
    IMetaJsonLoader metaJsonProvider = mock(IMetaJsonLoader.class);
    Meta meta = mock(Meta.class);

    doThrow(MetaAdaptException.class).when(metaAdapter).adapt(any(), any());

    AdaptedMetaListProcessor provider = new AdaptedMetaListProcessor(
        null,
        metaAdapter,
        metaJsonProvider
    );

    provider.processLocal(new ArrayList<>(Collections.singletonList(meta)));

    verify(metaJsonProvider, times(1)).load(any());
    verify(metaAdapter, times(1)).adapt(any(), any());
    verify(meta, times(1)).setValid(false);
  }

}