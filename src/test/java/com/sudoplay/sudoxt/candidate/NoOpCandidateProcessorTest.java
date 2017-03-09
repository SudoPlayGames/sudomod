package com.sudoplay.sudoxt.candidate;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class NoOpCandidateProcessorTest {

  @Test
  public void shouldReturnCandidatePassedIn() {

    Candidate candidate = mock(Candidate.class);
    when(candidate.getPath()).thenReturn(Paths.get("path"));

    NoOpCandidateProcessor processor = new NoOpCandidateProcessor();
    Candidate processedCandidate = processor.process(candidate);

    Assert.assertTrue(processedCandidate == candidate);
  }
}
