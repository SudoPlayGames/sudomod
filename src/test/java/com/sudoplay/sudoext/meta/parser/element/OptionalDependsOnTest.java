package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.DependencyContainer;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaAdaptException;
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

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenArrayContainsEmptyString() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"\" ] }");
    parser.adapt(jsonObject, null);
  }

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenArrayContainsDuplicateId() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [ \"after:id\", \"before:id@2.3\" ] }");
    parser.adapt(jsonObject, null);
  }

  @Test
  public void shouldNotThrowWhenMissingKey() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{}");
    parser.adapt(jsonObject, mock(Meta.class));
  }

  @Test
  public void shouldNotThrowWhenEmptyArray() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter(DependencyContainer::new);
    JSONObject jsonObject = new JSONObject("{ \"depends-on\": [] }");
    parser.adapt(jsonObject, mock(Meta.class));
  }

  @Test
  public void shouldThrowWhenArrayContainsInvalidString() throws MetaAdaptException, JSONException {
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter(DependencyContainer::new);

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
        parser.adapt(jsonObject, new Meta());
        Assert.fail(invalidString);

      } catch (MetaAdaptException e) {
        // expected
      }
    }
  }

  @Test
  public void shouldNotThrowWhenArrayContainsValidString() throws MetaAdaptException, JSONException {
    DependencyContainer dependencyContainer = mock(DependencyContainer.class);
    OptionalDependsOnAdapter parser = new OptionalDependsOnAdapter(() -> dependencyContainer);

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
        parser.adapt(jsonObject, store);

      } catch (MetaAdaptException e) {
        Assert.fail(invalidString);
      }
    }

    verify(dependencyContainer, times(invalidStrings.length)).addDependency(any(), any());
  }
}
