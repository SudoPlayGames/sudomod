package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Meta;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class IdValidatorTest {

  @Test
  public void isValidShouldReturnFalseWhenIdContainsInvalidCharacters() throws Exception {

    Meta meta = mock(Meta.class);
    IdValidator validator = new IdValidator();

    when(meta.getId()).thenReturn(null);
    Assert.assertFalse(validator.isValid(meta, null, Collections.emptyList()));

    when(meta.getId()).thenReturn("");
    Assert.assertFalse(validator.isValid(meta, null, Collections.emptyList()));

    when(meta.getId()).thenReturn("A");
    Assert.assertFalse(validator.isValid(meta, null, Collections.emptyList()));

    when(meta.getId()).thenReturn(";");
    Assert.assertFalse(validator.isValid(meta, null, Collections.emptyList()));
  }

  @Test
  public void isValidShouldReturnTrueWhenIdContainsValidCharacters() throws Exception {

    Meta meta = mock(Meta.class);
    IdValidator validator = new IdValidator();

    // requires list to check for duplicate ids
    List<Meta> metaList = new ArrayList<>();
    metaList.add(meta);

    when(meta.getId()).thenReturn("abcdefghijklmnopqrstuvwxyz0123456789-_");
    Assert.assertTrue(validator.isValid(meta, null, metaList));
  }

  @Test
  public void isValidShouldReturnFalseWhenIdIsDuplicate() throws Exception {

    Meta metaA = mock(Meta.class);
    Meta metaB = mock(Meta.class);
    IdValidator validator = new IdValidator();

    // requires list to check for duplicate ids
    List<Meta> metaList = new ArrayList<>();
    metaList.add(metaA);
    metaList.add(metaB);

    when(metaA.getId()).thenReturn("duplicate-id");
    when(metaB.getId()).thenReturn("duplicate-id");
    Assert.assertFalse(validator.isValid(metaA, null, metaList));
  }

}