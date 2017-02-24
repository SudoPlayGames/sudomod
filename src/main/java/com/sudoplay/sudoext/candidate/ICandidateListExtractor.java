package com.sudoplay.sudoext.candidate;

import java.util.List;

/**
 * Created by codetaylor on 2/20/2017.
 */
public interface ICandidateListExtractor {

  List<Candidate> extract(List<Candidate> candidateList, List<Candidate> store);
}
