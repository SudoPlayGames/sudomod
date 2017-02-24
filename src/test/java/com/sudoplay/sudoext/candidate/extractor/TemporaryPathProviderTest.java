package com.sudoplay.sudoext.candidate.extractor;

import com.sudoplay.sudoext.candidate.CandidateCompressed;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class TemporaryPathProviderTest {

  @Test
  public void shouldReturnPathDerivedFromCompressedCandidateFilename() {

    CandidateCompressed candidateCompressed = mock(CandidateCompressed.class);

    when(candidateCompressed.getPath()).thenReturn(Paths.get("filename.zip"));

    Path tempfolder = Paths.get("tempfolder");

    TemporaryPathProvider pathProvider = new TemporaryPathProvider(tempfolder, "zip");

    Path temporaryPath = pathProvider.getTemporaryPath(candidateCompressed);

    Assert.assertTrue(tempfolder.resolve("filename").equals(temporaryPath));
  }
}
