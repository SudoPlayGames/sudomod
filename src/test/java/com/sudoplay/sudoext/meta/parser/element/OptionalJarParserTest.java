package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class OptionalJarParserTest {

  @Test
  public void shouldNotThrowWhenMissingKey() throws JSONException, MetaParseException {
    OptionalJarParser parser = new OptionalJarParser();
    JSONObject jsonObject = new JSONObject("{}");
    parser.parse(jsonObject, new Meta());
  }

  @Test(expected = MetaParseException.class)
  public void shouldThrowWhenArrayContainsEmptyString() throws JSONException, MetaParseException {
    OptionalJarParser parser = new OptionalJarParser();
    JSONObject jsonObject = new JSONObject("{ \"jars\": [ \"\" ] }");
    parser.parse(jsonObject, null);
  }

  @Test(expected = MetaParseException.class)
  public void shouldThrowWhenArrayContainsDuplicate() throws JSONException, MetaParseException {
    OptionalJarParser parser = new OptionalJarParser();
    JSONObject jsonObject = new JSONObject("{ \"jars\": [ \"jarfile1\", \"jarfile1\" ] }");
    parser.parse(jsonObject, null);
  }

  @Test
  public void shouldSetJarStringSetInMeta() throws JSONException, MetaParseException {
    OptionalJarParser parser = new OptionalJarParser();
    JSONObject jsonObject = new JSONObject("{ \"jars\": [ \"jar-file-1\", \"jar-file-2\" ] }");
    Meta meta = new Meta();
    parser.parse(jsonObject, meta);
    Assert.assertTrue(meta.getJarFileSet().contains("jar-file-1"));
    Assert.assertTrue(meta.getJarFileSet().contains("jar-file-2"));
  }

}
