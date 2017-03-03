package com.sudoplay.sudoext.meta.validator;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class MetaValidatorTest {

  @Test
  public void isValidShouldDispatchToAllRegisteredValidators() throws Exception {

    IMetaValidator validatorA = mock(IMetaValidator.class);
    IMetaValidator validatorB = mock(IMetaValidator.class);
    IMetaValidator validatorC = mock(IMetaValidator.class);

    MetaValidator metaValidator = new MetaValidator(new IMetaValidator[]{
        validatorA,
        validatorB,
        validatorC
    });

    metaValidator.isValid(null, null, null);

    verify(validatorA, times(1)).isValid(null, null, null);
    verify(validatorB, times(1)).isValid(null, null, null);
    verify(validatorC, times(1)).isValid(null, null, null);
  }

  @Test
  public void isValidShouldDispatchToAllRegisteredValidatorsIfAnyFail() throws Exception {

    IMetaValidator validatorA = mock(IMetaValidator.class);
    IMetaValidator validatorB = mock(IMetaValidator.class);
    IMetaValidator validatorC = mock(IMetaValidator.class);

    when(validatorA.isValid(null, null, null)).thenReturn(false);
    when(validatorB.isValid(null, null, null)).thenReturn(true);
    when(validatorC.isValid(null, null, null)).thenReturn(true);

    MetaValidator metaValidator = new MetaValidator(new IMetaValidator[]{
        validatorA,
        validatorB,
        validatorC
    });

    Assert.assertFalse(metaValidator.isValid(null, null, null));

    verify(validatorA, times(1)).isValid(null, null, null);
    verify(validatorB, times(1)).isValid(null, null, null);
    verify(validatorC, times(1)).isValid(null, null, null);
  }

  @Test
  public void isValidShouldReturnTrueIfAllValidatorsPass() throws Exception {

    IMetaValidator validatorA = mock(IMetaValidator.class);
    IMetaValidator validatorB = mock(IMetaValidator.class);
    IMetaValidator validatorC = mock(IMetaValidator.class);

    when(validatorA.isValid(null, null, null)).thenReturn(true);
    when(validatorB.isValid(null, null, null)).thenReturn(true);
    when(validatorC.isValid(null, null, null)).thenReturn(true);

    MetaValidator metaValidator = new MetaValidator(new IMetaValidator[]{
        validatorA,
        validatorB,
        validatorC
    });

    Assert.assertTrue(metaValidator.isValid(null, null, null));
  }

}