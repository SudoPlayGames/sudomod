package com.sudoplay.sudoxt.classloader.asm.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by codetaylor on 4/7/2017.
 */
public class ASMClassFilterPredicateTest {

  @Test
  public void stripClassNameShouldStripArrayObjectClassName() throws Exception {

    ASMClassFilterPredicate predicate = new ASMClassFilterPredicate(null);

    String s = predicate.stripClassName("[Lapi/activemission/MapEntityApi;");

    Assert.assertEquals("api/activemission/MapEntityApi", s);

  }

  @Test
  public void stripClassNameShouldStripArraysObjectClassName() throws Exception {

    ASMClassFilterPredicate predicate = new ASMClassFilterPredicate(null);

    String s = predicate.stripClassName("[[[Lapi/activemission/MapEntityApi;");

    Assert.assertEquals("api/activemission/MapEntityApi", s);

  }

  @Test
  public void stripClassNameShouldStripObjectClassName() throws Exception {

    ASMClassFilterPredicate predicate = new ASMClassFilterPredicate(null);

    String s = predicate.stripClassName("Lapi/activemission/MapEntityApi;");

    Assert.assertEquals("api/activemission/MapEntityApi", s);

  }

}