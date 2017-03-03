package com.sudoplay.sudoext.meta.validator.element;

import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.versioning.DefaultArtifactVersion;
import com.sudoplay.sudoext.versioning.VersionRange;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class DependsOnValidatorTest {

  @Test
  public void isValid() throws Exception {

  }

  @Test
  public void validateRequiredShouldReturnTrueWhenDependencyExistsWithCorrectVersion() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("id", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("1.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("id", meta);

    boolean result = validator.validateRequired(
        null,
        dependencyList,
        metaMap
    );

    Assert.assertTrue(result);
  }

  @Test
  public void validateRequiredShouldReturnFalseWhenDependencyExistsWithIncorrectVersion() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("id", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("3.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("id", meta);

    boolean result = validator.validateRequired(
        null,
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }

  @Test
  public void validateRequiredShouldReturnFalseWhenDependencyDoesNotExist() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("id", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Map<String, Meta> metaMap = new HashMap<>();

    boolean result = validator.validateRequired(
        null,
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }

  @Test
  public void validateSoftShouldReturnTrueWhenDependencyExistsWithCorrectVersion() throws Exception {

    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("id", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("1.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("id", meta);

    boolean result = validator.validateSoft(
        null,
        dependencyList,
        metaMap
    );

    Assert.assertTrue(result);
  }

  @Test
  public void validateSoftShouldReturnTrueWhenDependencyDoesNotExist() throws Exception {

    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("id", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Map<String, Meta> metaMap = new HashMap<>();

    boolean result = validator.validateSoft(
        null,
        dependencyList,
        metaMap
    );

    Assert.assertTrue(result);
  }

  @Test
  public void validateSoftShouldReturnFalseWhenDependencyExistsWithIncorrectVersion() throws Exception {

    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("id", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("3.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("id", meta);

    boolean result = validator.validateSoft(
        null,
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }
}