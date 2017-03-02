package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
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
    IMetaElementParser parser = new AuthorParser();

    parser.parse(new JSONObject(json), meta);

    verify(meta, times(1)).setAuthor("codetaylor");
  }

  @Test(expected = JSONException.class)
  public void shouldThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaElementParser parser = new AuthorParser();

    parser.parse(new JSONObject(json), meta);
  }
}
