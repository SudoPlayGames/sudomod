package com.sudoplay.sudoext.meta.parser.element;

import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaAdaptException;
import com.sudoplay.sudoext.meta.parser.IMetaElementAdapter;
import org.json.JSONObject;
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
    IMetaElementAdapter parser = new OptionalApiVersionAdapter();

    parser.adapt(new JSONObject(json), meta);

    verify(meta, times(1)).setApiVersionRange(any());
  }

  @Test(expected = MetaAdaptException.class)
  public void shouldThrowWhenVersionStringIsInvalid() throws Exception {

    String json = "{ \"api-version\": \"[1,version)\" }";
    Meta meta = mock(Meta.class);
    IMetaElementAdapter parser = new OptionalApiVersionAdapter();

    parser.adapt(new JSONObject(json), meta);
  }

  @Test
  public void shouldNotThrowWhenKeyDoesNotExist() throws Exception {

    String json = "{}";
    Meta meta = mock(Meta.class);
    IMetaElementAdapter parser = new OptionalApiVersionAdapter();

    parser.adapt(new JSONObject(json), meta);
  }
}
