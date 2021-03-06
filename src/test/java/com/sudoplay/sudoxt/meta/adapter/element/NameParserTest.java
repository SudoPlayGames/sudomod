package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.json.JSONException;
import com.sudoplay.json.JSONObject;
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
    IMetaAdapter parser = new NameAdapter();

    parser.adapt(new JSONObject(json), meta);

    verify(meta, times(1)).setName("name");
  }

  @Test(expected = JSONException.class)
  public void shouldThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new NameAdapter();

    parser.adapt(new JSONObject(json), meta);
  }
}
