package com.sudoplay.sudoext.candidate.locator;

import com.sudoplay.sudoext.candidate.Candidate;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class CandidateLocatorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReturnListOfFolderCandidatesThatValidate() throws IOException {

    IPathListProvider pathListProvider = mock(IPathListProvider.class);
    IPathValidator pathValidator = mock(IPathValidator.class);

    when(pathListProvider.getPathList()).thenReturn(Arrays.asList(
        Paths.get("path-a"),
        Paths.get("path-b")
    ));

    when(pathValidator.isPathValid(Paths.get("path-a"))).thenReturn(true);
    when(pathValidator.isPathValid(Paths.get("path-b"))).thenReturn(false);

    CandidateLocator locator = new CandidateLocator(
        pathListProvider,
        pathValidator,
        path -> new Candidate(path, null)
    );

    List<Candidate> candidateList = locator.locateCandidates();

    Assert.assertEquals(1, candidateList.size());
    Assert.assertEquals(Paths.get("path-a"), candidateList.get(0).getPath());
  }

}
