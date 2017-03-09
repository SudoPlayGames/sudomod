package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class OptionalJarParserTest {

  @Test
  public void shouldNotThrowWhenMissingKey() throws JSONException, MetaAdaptException {
    OptionalJarAdapter parser = new OptionalJarAdapter();
    JSONObject jsonObject = new JSONObject("{}");
    parser.adapt(jsonObject, new Meta(null, null));
  }

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenArrayContainsEmptyString() throws JSONException, MetaAdaptException {
    OptionalJarAdapter parser = new OptionalJarAdapter();
    JSONObject jsonObject = new JSONObject("{ \"jars\": [ \"\" ] }");
    parser.adapt(jsonObject, null);
  }

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenArrayContainsDuplicate() throws JSONException, MetaAdaptException {
    OptionalJarAdapter parser = new OptionalJarAdapter();
    JSONObject jsonObject = new JSONObject("{ \"jars\": [ \"jarfile1\", \"jarfile1\" ] }");
    parser.adapt(jsonObject, null);
  }

  @Test
  public void shouldSetJarStringSetInMeta() throws JSONException, MetaAdaptException {
    OptionalJarAdapter parser = new OptionalJarAdapter();
    JSONObject jsonObject = new JSONObject("{ \"jars\": [ \"jar-file-1\", \"jar-file-2\" ] }");
    Meta meta = new Meta(null, null);
    parser.adapt(jsonObject, meta);
    Assert.assertTrue(meta.getJarFileSet().contains("jar-file-1"));
    Assert.assertTrue(meta.getJarFileSet().contains("jar-file-2"));
  }

}
