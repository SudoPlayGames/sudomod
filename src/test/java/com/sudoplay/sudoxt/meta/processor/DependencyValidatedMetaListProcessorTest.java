package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.Dependency;
import com.sudoplay.sudoxt.meta.LoadOrder;
import com.sudoplay.sudoxt.meta.Meta;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class DependencyValidatedMetaListProcessorTest {

  @Test
  public void processLocalShouldInvalidateValidMetaDependentOnInvalidMeta_Test1() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB
    ));

    metaA.setValid(false);
    metaB.setValid(true);

    metaA.setId("metaA");
    metaB.setId("metaB");

    metaB.addDependency(new Dependency("metaA", LoadOrder.RequiredAfter, null));

    new DependencyValidatedMetaListProcessor(null).processLocal(metaList);

    Assert.assertFalse(metaB.isValid());
  }

  @Test
  public void processLocalShouldInvalidateValidMetaDependentOnInvalidMeta_Test2() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaB, // reversed order because tested method expects sorted list
        metaA
    ));

    metaA.setValid(false);
    metaB.setValid(true);

    metaA.setId("metaA");
    metaB.setId("metaB");

    metaB.addDependency(new Dependency("metaA", LoadOrder.RequiredBefore, null));

    new DependencyValidatedMetaListProcessor(null).processLocal(metaList);

    Assert.assertFalse(metaB.isValid());
  }

  @Test
  public void processLocalShouldInvalidateValidMetaDependentOnInvalidMeta_Test3() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);
    Meta metaC = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB,
        metaC
    ));

    metaA.setValid(false);
    metaB.setValid(true);
    metaC.setValid(true);

    metaA.setId("metaA");
    metaB.setId("metaB");
    metaC.setId("metaC");

    metaB.addDependency(new Dependency("metaA", LoadOrder.RequiredAfter, null));
    metaC.addDependency(new Dependency("metaB", LoadOrder.RequiredAfter, null));

    new DependencyValidatedMetaListProcessor(null).processLocal(metaList);

    Assert.assertFalse(metaB.isValid());
    Assert.assertFalse(metaC.isValid());
  }

}