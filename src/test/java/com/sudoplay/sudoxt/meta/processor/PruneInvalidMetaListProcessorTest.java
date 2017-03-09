package com.sudoplay.sudoxt.meta.processor;

import com.sudoplay.sudoxt.meta.Meta;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by codetaylor on 3/3/2017.
 */
public class PruneInvalidMetaListProcessorTest {

  @Test
  public void processLocalShouldReturnNewListWithoutInvalidMeta() throws Exception {

    Meta metaA = new Meta(null, null);
    Meta metaB = new Meta(null, null);

    metaA.setValid(true);
    metaB.setValid(false);

    List<Meta> metaList = new ArrayList<>(Arrays.asList(
        metaA,
        metaB
    ));

    List<Meta> newMetaList = new PruneInvalidMetaListProcessor(null).processLocal(metaList);

    Assert.assertEquals(1, newMetaList.size());
    Assert.assertEquals(metaA, newMetaList.get(0));
  }

}