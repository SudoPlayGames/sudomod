package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
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
    IMetaElementParser parser = new OptionalWebsiteParser();

    parser.parse(new JSONObject(json), meta);

    verify(meta, times(1)).setWebsite("www.example.com");
  }

  @Test
  public void shouldNotThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaElementParser parser = new OptionalWebsiteParser();

    parser.parse(new JSONObject(json), meta);
  }
}
