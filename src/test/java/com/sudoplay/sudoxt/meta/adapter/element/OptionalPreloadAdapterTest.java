package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.json.JSONObject;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by codetaylor on 3/6/2017.
 */
public class OptionalPreloadAdapterTest {

  @Test
  public void adaptDoesNotThrowWhenMissingKey() throws Exception {

    Meta meta = mock(Meta.class);
    JSONObject jsonObject = new JSONObject("{}");

    OptionalPreloadAdapter adapter = new OptionalPreloadAdapter();
    adapter.adapt(jsonObject, meta);
  }

  @Test
  public void adaptDoesNotThrowWhenEmptyArray() throws Exception {

    Meta meta = mock(Meta.class);
    JSONObject jsonObject = new JSONObject("{ \"preload\": []}");

    OptionalPreloadAdapter adapter = new OptionalPreloadAdapter();
    adapter.adapt(jsonObject, meta);
  }

  @Test
  public void adaptAdaptsWhenArray() throws Exception {

    Meta meta = mock(Meta.class);
    JSONObject jsonObject = new JSONObject("{ \"preload\": [\"TestA\", \"TestB\"]}");

    OptionalPreloadAdapter adapter = new OptionalPreloadAdapter();
    adapter.adapt(jsonObject, meta);

    verify(meta, times(1)).addPreload("TestA");
    verify(meta, times(1)).addPreload("TestB");
  }

}