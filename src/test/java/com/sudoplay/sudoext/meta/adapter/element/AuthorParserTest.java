package com.sudoplay.sudoext.meta.adapter.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class AuthorParserTest {

  @Test
  public void shouldReadFromValidJsonAndStoreInMetaObject() throws Exception {

    String json = "{ \"author\": \"codetaylor\" }";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new AuthorAdapter();

    parser.adapt(new JSONObject(json), meta);

    verify(meta, times(1)).setAuthor("codetaylor");
  }

  @Test(expected = JSONException.class)
  public void shouldThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new AuthorAdapter();

    parser.adapt(new JSONObject(json), meta);
  }
}
