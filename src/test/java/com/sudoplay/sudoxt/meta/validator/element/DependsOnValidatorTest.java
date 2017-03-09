package com.sudoplay.sudoxt.meta.validator.element;

import com.sudoplay.sudoxt.meta.Dependency;
import com.sudoplay.sudoxt.meta.Meta;
import com.sudoplay.sudoxt.versioning.DefaultArtifactVersion;
import com.sudoplay.sudoxt.versioning.VersionRange;
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
  public void validateRequiredShouldReturnFalseWhenDependsOnSelf() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, null));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("1.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("meta-a", meta);

    boolean result = validator.validateRequired(
        "meta-a",
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }

  @Test
  public void validateSoftShouldReturnFalseWhenDependsOnSelf() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, null));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("1.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("meta-a", meta);

    boolean result = validator.validateSoft(
        "meta-a",
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }

  @Test
  public void validateRequiredShouldReturnTrueWhenDependencyExistsWithCorrectVersion() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("1.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("meta-a", meta);

    boolean result = validator.validateRequired(
        "meta-b",
        dependencyList,
        metaMap
    );

    Assert.assertTrue(result);
  }

  @Test
  public void validateRequiredShouldReturnFalseWhenDependencyExistsWithIncorrectVersion() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("3.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("meta-a", meta);

    boolean result = validator.validateRequired(
        "meta-b",
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }

  @Test
  public void validateRequiredShouldReturnFalseWhenDependencyDoesNotExist() throws Exception {
    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Map<String, Meta> metaMap = new HashMap<>();

    boolean result = validator.validateRequired(
        "meta-b",
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }

  @Test
  public void validateSoftShouldReturnTrueWhenDependencyExistsWithCorrectVersion() throws Exception {

    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("1.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("meta-a", meta);

    boolean result = validator.validateSoft(
        "meta-b",
        dependencyList,
        metaMap
    );

    Assert.assertTrue(result);
  }

  @Test
  public void validateSoftShouldReturnTrueWhenDependencyDoesNotExist() throws Exception {

    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Map<String, Meta> metaMap = new HashMap<>();

    boolean result = validator.validateSoft(
        "meta-b",
        dependencyList,
        metaMap
    );

    Assert.assertTrue(result);
  }

  @Test
  public void validateSoftShouldReturnFalseWhenDependencyExistsWithIncorrectVersion() throws Exception {

    DependsOnValidator validator = new DependsOnValidator();

    List<Dependency> dependencyList = new ArrayList<>();
    dependencyList.add(new Dependency("meta-a", null, VersionRange.createFromVersionSpec("[1.0,2.0]")));

    Meta meta = mock(Meta.class);
    when(meta.getVersion()).thenReturn(new DefaultArtifactVersion("3.5"));

    Map<String, Meta> metaMap = new HashMap<>();
    metaMap.put("meta-a", meta);

    boolean result = validator.validateSoft(
        "meta-b",
        dependencyList,
        metaMap
    );

    Assert.assertFalse(result);
  }
}