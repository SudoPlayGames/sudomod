package com.sudoplay.sudoext.candidate;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class CandidateListCreatorTest {

  @Test
  public void shouldReturnListOfAggregateResultsFromAllLocators() throws IOException {

    Candidate candidateA = new Candidate(Paths.get("candidate-a"));
    Candidate candidateB = new Candidate(Paths.get("candidate-b"));
    Candidate candidateC = new Candidate(Paths.get("candidate-c"));
    Candidate candidateD = new Candidate(Paths.get("candidate-d"));

    ICandidateProvider locatorA = mock(ICandidateProvider.class);
    when(locatorA.getCandidates()).thenReturn(Arrays.asList(
        candidateA,
        candidateB
    ));

    ICandidateProvider locatorB = mock(ICandidateProvider.class);
    when(locatorB.getCandidates()).thenReturn(Arrays.asList(
        candidateC,
        candidateD
    ));

    CandidateListCreator listCreator = new CandidateListCreator(
        new ICandidateProvider[]{
            locatorA,
            locatorB
        }
    );

    List<Candidate> candidateList = listCreator.createCandidateList();

    Assert.assertEquals(4, candidateList.size());
    Assert.assertTrue(candidateList.contains(candidateA));
    Assert.assertTrue(candidateList.contains(candidateB));
    Assert.assertTrue(candidateList.contains(candidateC));
    Assert.assertTrue(candidateList.contains(candidateD));
  }

  @Test
  public void shouldReturnListOfAggregateResultsFromAllLocatorsThatDoNotThrow() throws IOException {

    Candidate candidateA = new Candidate(Paths.get("candidate-a"));
    Candidate candidateB = new Candidate(Paths.get("candidate-b"));

    ICandidateProvider locatorA = mock(ICandidateProvider.class);
    when(locatorA.getCandidates()).thenReturn(Arrays.asList(
        candidateA,
        candidateB
    ));

    ICandidateProvider locatorB = mock(ICandidateProvider.class);
    when(locatorB.getCandidates()).thenThrow(IOException.class);

    CandidateListCreator listCreator = new CandidateListCreator(
        new ICandidateProvider[]{
            locatorA,
            locatorB
        }
    );

    List<Candidate> candidateList = listCreator.createCandidateList();

    Assert.assertEquals(2, candidateList.size());
    Assert.assertTrue(candidateList.contains(candidateA));
    Assert.assertTrue(candidateList.contains(candidateB));
  }

}
