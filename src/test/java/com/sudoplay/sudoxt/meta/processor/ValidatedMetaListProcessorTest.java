package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class ValidatedMetaListProcessorTest {

  @Test
  public void processLocalShouldInvalidateMetaWhenValidatorReturnsFalse() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB
    ));

    IMetaValidator metaValidator = mock(IMetaValidator.class);
    when(metaValidator.isValid(eq(metaA), any(), any())).thenReturn(false);
    when(metaValidator.isValid(eq(metaB), any(), any())).thenReturn(true);

    new ValidatedMetaListProcessor(null, metaValidator).processLocal(metaList);

    Assert.assertFalse(metaA.isValid());
    Assert.assertTrue(metaB.isValid());
  }

}