package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.versioning.DefaultArtifactVersion;
import com.sudoplay.sudoxt.versioning.VersionRange;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class ApiVersionValidatorTest {

  @Test
  public void isValidReturnsTrueWhenApiVersionIsValid() throws Exception {

    Meta meta = mock(Meta.class);
    when(meta.getApiVersionRange()).thenReturn(VersionRange.createFromVersionSpec("[1.0,)"));

    ApiVersionValidator validator = new ApiVersionValidator(
        new DefaultArtifactVersion("1.0")
    );

    Assert.assertTrue(validator.isValid(meta, null, null));
  }

  @Test
  public void isValidReturnsFalseWhenApiVersionIsInvalid() throws Exception {

    Meta meta = mock(Meta.class);
    when(meta.getApiVersionRange()).thenReturn(VersionRange.createFromVersionSpec("[3.0,)"));

    ApiVersionValidator validator = new ApiVersionValidator(
        new DefaultArtifactVersion("1.0")
    );

    Assert.assertFalse(validator.isValid(meta, null, null));
  }

}