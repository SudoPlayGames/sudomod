package com.sudoplay.sudoext.meta.adapter.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import org.json.JSONObject;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class OptionalWebsiteParserTest {

  @Test
  public void shouldReadFromValidJsonAndStoreInMetaObject() throws Exception {

    String json = "{ \"website\": \"www.example.com\" }";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new OptionalWebsiteAdapter();

    parser.adapt(new JSONObject(json), meta);

    verify(meta, times(1)).setWebsite("www.example.com");
  }

  @Test
  public void shouldNotThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new OptionalWebsiteAdapter();

    parser.adapt(new JSONObject(json), meta);
  }
}
