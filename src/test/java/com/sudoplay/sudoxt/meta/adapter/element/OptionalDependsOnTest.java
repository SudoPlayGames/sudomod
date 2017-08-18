package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import com.sudoplay.json.JSONException;
import com.sudoplay.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class OptionalDependsOnTest {

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenArrayContainsEmptyString() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter();
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"\" ] }");
    parser.adapt(jsonObject, null);
  }

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenArrayContainsDuplicateId() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter();
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"after:id\", \"before:id@2.3\" ] }");
    parser.adapt(jsonObject, null);
  }

  @Test
  public void shouldNotThrowWhenMissingKey() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter();
    JSONObject jsonObject = new JSONObject("{}");
    parser.adapt(jsonObject, mock(Meta.class));
  }

  @Test
  public void shouldNotThrowWhenEmptyArray() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter();
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [] }");
    parser.adapt(jsonObject, mock(Meta.class));
  }

  @Test
  public void shouldThrowWhenArrayContainsInvalidString() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter();

    String[] invalidStrings = new String[]{
        "invalid",
        "invalid-instruction:valid-id",
        "after:id@1@",
        "after::id@1",
        "@after:id",
        "after@id:1",
        "after:id@[20.4,2.5)",
        "after@id"
    };

    for (String invalidString : invalidStrings) {
      try {
        JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"" + invalidString + "\" ] }");
        parser.adapt(jsonObject, new Meta(null, null));
        Assert.fail(invalidString);

      } catch (MetaAdaptException e) {
        // expected
      }
    }
  }

  @Test
  public void shouldNotThrowWhenArrayContainsValidString() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter();
    Meta meta = mock(Meta.class);

    String[] invalidStrings = new String[]{
        "after:id@1.0",
        "before:id@[1,3.2]",
        "after:*",
        "after:id"
    };

    for (String invalidString : invalidStrings) {
      try {
        JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"" + invalidString + "\" ] }");
        parser.adapt(jsonObject, meta);

      } catch (MetaAdaptException e) {
        Assert.fail(invalidString);
      }
    }

    verify(meta, times(invalidStrings.length)).addDependency(any());
  }
}
