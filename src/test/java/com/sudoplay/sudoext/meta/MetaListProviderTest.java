package com.sudoplay.sudoext.meta;

import com.sudoplay.sudoext.candidate.Candidate;
import com.sudoplay.sudoext.candidate.ICandidateListProvider;
import com.sudoplay.sudoext.meta.IMetaListProcessor;
import com.sudoplay.sudoext.meta.Meta;
import com.sudoplay.sudoext.meta.MetaListProvider;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 3/2/2017.
 */
public class MetaListProviderTest {

  @Test
  public void getContainerList() throws Exception {

    Candidate candidate = mock(Candidate.class);
    when(candidate.getPath()).thenReturn(Paths.get("path"));

    ICandidateListProvider listProvider = mock(ICandidateListProvider.class);
    when(listProvider.getCandidateList()).thenReturn(new ArrayList<>(Collections.singletonList(
        candidate
    )));

    IMetaListProcessor metaListProcessor = metaList -> metaList;

    MetaListProvider metaListProvider = new MetaListProvider(
        listProvider,
        metaListProcessor,
        "meta.json"
    );

    List<Meta> metaList = metaListProvider.getMetaList();

    Assert.assertEquals(Paths.get("path"), metaList.get(0).getParentPath());
    Assert.assertEquals(Paths.get("path/meta.json"), metaList.get(0).getPath());
  }

}