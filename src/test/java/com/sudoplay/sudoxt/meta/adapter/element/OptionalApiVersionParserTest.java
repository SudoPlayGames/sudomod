package com.sudoplay.sudoxt.meta.adapter.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.MetaAdaptException;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.json.JSONObject;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class OptionalApiVersionParserTest {

  @Test
  public void shouldReadFromValidJsonAndStoreInMetaObject() throws Exception {

    String json = "{ \"api-version\": \"[1,2)\" }";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new OptionalApiVersionAdapter();

    parser.adapt(new JSONObject(json), meta);

    verify(meta, times(1)).setApiVersionRange(any());
  }

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenVersionStringIsInvalid() throws Exception {

    String json = "{ \"api-version\": \"[1,version)\" }";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new OptionalApiVersionAdapter();

    parser.adapt(new JSONObject(json), meta);
  }

  @Test
  public void shouldNotThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaAdapter parser = new OptionalApiVersionAdapter();

    parser.adapt(new JSONObject(json), meta);
  }
}
