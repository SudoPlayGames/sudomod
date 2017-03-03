package com.sudoplay.sudoext.meta.processor;

import com.sudoplay.sudoext.meta.Dependency;
import com.sudoplay.sudoext.meta.LoadOrder;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.sort.CyclicGraphException;
import com.sudoplay.sudoext.sort.TopologicalSort;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class SortedMetaListProcessorTest {

  @Test
  public void processLocalSortsAfter() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaB,
        metaA
    ));

    metaA.setId("metaA");
    metaB.setId("metaB");

    metaB.addDependency(new Dependency("metaA", LoadOrder.After, null));

    metaList = new SortedMetaListProcessor(null, new TopologicalSort<>()).processLocal(metaList);

    Assert.assertTrue(metaA == metaList.get(0));
  }

  @Test
  public void processLocalSortsBefore() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB
    ));

    metaA.setId("metaA");
    metaB.setId("metaB");

    metaB.addDependency(new Dependency("metaA", LoadOrder.Before, null));

    metaList = new SortedMetaListProcessor(null, new TopologicalSort<>()).processLocal(metaList);

    Assert.assertTrue(metaB == metaList.get(0));
  }

  @Test
  public void processLocalSortsAfterAll() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);
    Meta metaC = new Meta(null, null);
    Meta metaD = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB,
        metaC,
        metaD
    ));

    metaA.setId("metaA");
    metaB.setId("metaB");
    metaC.setId("metaC");
    metaD.setId("metaD");

    metaA.addDependency(new Dependency("*", LoadOrder.After, null));

    metaList = new SortedMetaListProcessor(null, new TopologicalSort<>()).processLocal(metaList);

    Assert.assertTrue(metaA == metaList.get(3));
  }

  @Test
  public void processLocalSortsBeforeAll() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);
    Meta metaC = new Meta(null, null);
    Meta metaD = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB,
        metaC,
        metaD
    ));

    metaA.setId("metaA");
    metaB.setId("metaB");
    metaC.setId("metaC");
    metaD.setId("metaD");

    metaD.addDependency(new Dependency("*", LoadOrder.Before, null));

    metaList = new SortedMetaListProcessor(null, new TopologicalSort<>()).processLocal(metaList);

    Assert.assertTrue(metaD == metaList.get(0));
  }

  @Test(expected = CyclicGraphException.class)
  public void processLocalThrowsWhenCycle() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);
    Meta metaC = new Meta(null, null);
    Meta metaD = new Meta(null, null);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB,
        metaC,
        metaD
    ));

    metaA.setId("metaA");
    metaB.setId("metaB");
    metaC.setId("metaC");
    metaD.setId("metaD");

    metaA.addDependency(new Dependency("metaD", LoadOrder.After, null));
    metaB.addDependency(new Dependency("metaA", LoadOrder.After, null));
    metaC.addDependency(new Dependency("metaB", LoadOrder.After, null));
    metaD.addDependency(new Dependency("metaC", LoadOrder.After, null));

    metaList = new SortedMetaListProcessor(null, new TopologicalSort<>()).processLocal(metaList);

    Assert.assertTrue(metaD == metaList.get(0));
  }

}