package com.sudoplay.sudoext.classloader.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class ClassFilterPredicateTest {

  @Test
  public void shouldReturnFalseWhenNoClassFilters() {
    ClassFilterPredicate predicate = new ClassFilterPredicate(new IClassFilter[0]);
    Assert.assertFalse(predicate.isAllowed("any-class"));
  }

  @Test
  public void shouldReturnFalseWhenEmptyClassFilter() {
    ClassFilterPredicate predicate = new ClassFilterPredicate(new IClassFilter[]{
        new IClassFilter() {
          //
        }
    });
    Assert.assertFalse(predicate.isAllowed("any-class"));
  }

  @Test
  public void shouldReturnTrueWhenAllowed() {
    ClassFilterPredicate predicate = new ClassFilterPredicate(new IClassFilter[]{
        new IClassFilter() {
          @Override
          public boolean isAllowed(String name) {
            return "some-class".equals(name);
          }
        }
    });
    Assert.assertTrue(predicate.isAllowed("some-class"));
  }

  @Test
  public void shouldReturnFalseWhenAllowedAndRestricted() {
    ClassFilterPredicate predicate = new ClassFilterPredicate(new IClassFilter[]{
        new IClassFilter() {
          @Override
          public boolean isAllowed(String name) {
            return "some-class".equals(name);
          }

          @Override
          public boolean isRestricted(String name) {
            return "some-class".equals(name);
          }
        }
    });
    Assert.assertFalse(predicate.isAllowed("some-class"));
  }

  @Test
  public void shouldReturnFalseWhenAllowedAndRestrictedBySeparateFilters() {
    ClassFilterPredicate predicate = new ClassFilterPredicate(new IClassFilter[]{
        new IClassFilter() {
          @Override
          public boolean isAllowed(String name) {
            return "some-class".equals(name);
          }
        },
        new IClassFilter() {
          @Override
          public boolean isRestricted(String name) {
            return "some-class".equals(name);
          }
        }
    });
    Assert.assertFalse(predicate.isAllowed("some-class"));
  }

}
