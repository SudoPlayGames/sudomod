package com.sudoplay.sudoext.candidate;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface ICandidateListProvider {

  List<Candidate> getCandidateList(
      List<Candidate> store
  );
}
