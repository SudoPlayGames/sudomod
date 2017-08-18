package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.json.JSONException;
import com.sudoplay.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 3/4/2017.
 */
public class OptionalRegisterAdapterTest {

  @Test
  public void adaptShouldNotThrowWhenMissingKey() throws Exception {

    JSONObject jsonObject = new JSONObject("{}");
    Meta meta = new Meta(null, null);
    OptionalRegisterAdapter adapter = new OptionalRegisterAdapter(false);

    adapter.adapt(jsonObject, meta);

    Assert.assertEquals(0, meta.getRegisteredPluginMap().size());
  }

  @Test
  public void adaptShouldNotThrowWhenEmptyKey() throws Exception {

    JSONObject jsonObject = new JSONObject("{ \"register\": {} }");
    Meta meta = new Meta(null, null);
    OptionalRegisterAdapter adapter = new OptionalRegisterAdapter(false);

    adapter.adapt(jsonObject, meta);

    Assert.assertEquals(0, meta.getRegisteredPluginMap().size());
  }

  @Test(expected = JSONException.class)
  public void adaptShouldThrowWhenKeyNotObject() throws Exception {

    JSONObject jsonObject = new JSONObject("{ \"register\": [] }");
    Meta meta = new Meta(null, null);
    OptionalRegisterAdapter adapter = new OptionalRegisterAdapter(false);

    adapter.adapt(jsonObject, meta);
  }

  @Test
  public void adaptShouldNotThrowWhenObjectContainsNonStringValue() throws Exception {

    JSONObject jsonObject = new JSONObject("{ \"register\": { \"key\": 123 } }");
    Meta meta = new Meta(null, null);
    OptionalRegisterAdapter adapter = new OptionalRegisterAdapter(false);

    adapter.adapt(jsonObject, meta);

    Assert.assertEquals(1, meta.getRegisteredPluginMap().size());
  }

  @Test
  public void adaptShouldAdaptJsonToMeta() throws Exception {

    JSONObject jsonObject = new JSONObject("{ \"register\": { \"key1\": \"value1\", \"key2\": \"value2\" } }");
    Meta meta = new Meta(null, null);
    OptionalRegisterAdapter adapter = new OptionalRegisterAdapter(false);

    adapter.adapt(jsonObject, meta);

    Assert.assertEquals(2, meta.getRegisteredPluginMap().size());
    Assert.assertEquals("value1", meta.getRegisteredPluginMap().get("key1"));
    Assert.assertEquals("value2", meta.getRegisteredPluginMap().get("key2"));
  }

  @Test
  public void adaptShouldAddToAutoPreloadSetWhenParameterIsTrue() throws Exception {

    JSONObject jsonObject = new JSONObject("{ \"register\": { \"key1\": \"value1\", \"key2\": \"value2\" } }");
    Meta meta = new Meta(null, null);
    OptionalRegisterAdapter adapter = new OptionalRegisterAdapter(true);

    adapter.adapt(jsonObject, meta);

    Assert.assertEquals(2, meta.getPreloadSet().size());
    Assert.assertTrue(meta.getPreloadSet().contains("value1"));
    Assert.assertTrue(meta.getPreloadSet().contains("value2"));
  }

  @Test
  public void adaptShouldNotAddToAutoPreloadSetWhenParameterIsFalse() throws Exception {

    JSONObject jsonObject = new JSONObject("{ \"register\": { \"key1\": \"value1\", \"key2\": \"value2\" } }");
    Meta meta = new Meta(null, null);
    OptionalRegisterAdapter adapter = new OptionalRegisterAdapter(false);

    adapter.adapt(jsonObject, meta);

    Assert.assertEquals(0, meta.getPreloadSet().size());
    Assert.assertFalse(meta.getPreloadSet().contains("value1"));
    Assert.assertFalse(meta.getPreloadSet().contains("value2"));
  }
}