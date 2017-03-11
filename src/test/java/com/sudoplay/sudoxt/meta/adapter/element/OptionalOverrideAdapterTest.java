package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.service.ResourceLocation;
import org.json.JSONObject;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/9/2017.
 */
public class OptionalOverrideAdapterTest {

  @Test
  public void adaptShouldNotThrowWhenEmptyKey() throws Exception {
    Meta meta = mock(Meta.class);
    OptionalOverrideAdapter adapter = new OptionalOverrideAdapter();

    adapter.adapt(new JSONObject("{}"), meta);
  }

  @Test
  public void adaptShouldNotThrowWhenEmptyObject() throws Exception {
    Meta meta = mock(Meta.class);
    OptionalOverrideAdapter adapter = new OptionalOverrideAdapter();

    adapter.adapt(new JSONObject("{ \"override\": {} }"), meta);
  }

  @Test
  public void adaptShouldNotThrowWhenEmptySubObject() throws Exception {
    Meta meta = mock(Meta.class);
    OptionalOverrideAdapter adapter = new OptionalOverrideAdapter();

    adapter.adapt(new JSONObject("{ \"override\": { \"mod-id\": {} } }"), meta);
  }

  @Test
  public void adaptShouldAddOverrideToMeta() throws Exception {
    Meta meta = mock(Meta.class);
    OptionalOverrideAdapter adapter = new OptionalOverrideAdapter();

    adapter.adapt(new JSONObject("{ \"override\": { \"mod-id\": { \"script.SomePlugin\": \"script.SomeOtherPlugin\" } } }"), meta);

    verify(meta, times(1)).addOverride(any(ResourceLocation.class), any(ResourceLocation.class));
  }
}