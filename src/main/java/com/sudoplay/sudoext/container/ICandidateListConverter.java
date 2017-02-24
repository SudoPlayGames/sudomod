package com.sudoplay.sudoext.container;

import com.sudoplay.sudoext.candidate.Candidate;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface ICandidateListConverter {

  List<Container> convert(
      List<Candidate> candidateList,
      List<Container> store
  );
}
