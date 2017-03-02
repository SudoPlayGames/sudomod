package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.IMetaElementAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class NameParserTest {

  @Test
  public void shouldReadFromValidJsonAndStoreInMetaObject() throws Exception {

    String json = "{ \"name\": \"name\" }";
    Meta meta = mock(Meta.class);
    IMetaElementAdapter parser = new NameAdapter();

    parser.adapt(new JSONObject(json), meta);

    verify(meta, times(1)).setName("name");
  }

  @Test(expected = JSONException.class)
  public void shouldThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaElementAdapter parser = new NameAdapter();

    parser.adapt(new JSONObject(json), meta);
  }
}
