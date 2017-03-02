package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.DependencyContainer;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class OptionalDependsOnTest {

  @Test(expected = MetaParseException.class)
  public void shouldThrowWhenArrayContainsEmptyString() throws MetaParseException, JSONException {
    OptionalDependsOnParser parser = new OptionalDependsOnParser(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"\" ] }");
    parser.parse(jsonObject, null);
  }

  @Test(expected = MetaParseException.class)
  public void shouldThrowWhenArrayContainsDuplicateId() throws MetaParseException, JSONException {
    OptionalDependsOnParser parser = new OptionalDependsOnParser(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"after:id\", \"before:id@2.3\" ] }");
    parser.parse(jsonObject, null);
  }

  @Test
  public void shouldNotThrowWhenMissingKey() throws MetaParseException, JSONException {
    OptionalDependsOnParser parser = new OptionalDependsOnParser(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{}");
    parser.parse(jsonObject, mock(Meta.class));
  }

  @Test
  public void shouldNotThrowWhenEmptyArray() throws MetaParseException, JSONException {
    OptionalDependsOnParser parser = new OptionalDependsOnParser(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [] }");
    parser.parse(jsonObject, mock(Meta.class));
  }

  @Test
  public void shouldThrowWhenArrayContainsInvalidString() throws MetaParseException, JSONException {
    OptionalDependsOnParser parser = new OptionalDependsOnParser(DependencyContainer::new);

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
        parser.parse(jsonObject, new Meta());
        Assert.fail(invalidString);

      } catch (MetaParseException e) {
        // expected
      }
    }
  }

  @Test
  public void shouldNotThrowWhenArrayContainsValidString() throws MetaParseException, JSONException {
    DependencyContainer dependencyContainer = mock(DependencyContainer.class);
    OptionalDependsOnParser parser = new OptionalDependsOnParser(() -> dependencyContainer);

    String[] invalidStrings = new String[]{
        "after:id@1.0",
        "before:id@[1,3.2]",
        "after:*",
        "after:id"
    };

    for (String invalidString : invalidStrings) {
      try {
        JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"" + invalidString + "\" ] }");
        Meta store = new Meta();
        parser.parse(jsonObject, store);

      } catch (MetaParseException e) {
        Assert.fail(invalidString);
      }
    }

    verify(dependencyContainer, times(invalidStrings.length)).addDependency(any(), any());
  }
}
