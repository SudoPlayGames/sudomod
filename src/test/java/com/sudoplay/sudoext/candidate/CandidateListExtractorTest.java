package com.sudoplay.sudoext.candidate;

import com.sudoplay.sudoext.candidate.extractor.IZipFileExtractionPathProvider;
import com.sudoplay.sudoext.candidate.extractor.IZipFileExtractor;
import com.sudoplay.sudoext.util.RecursiveFileRemovalProcessor;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class CandidateListExtractorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReplaceCompressedCandidatesInList() throws IOException {

    IZipFileExtractor zipFileExtractor = mock(IZipFileExtractor.class);
    when(zipFileExtractor.extract(any(), eq(Paths.get("folder-a")))).thenReturn(new Candidate(
        Paths.get("test-path"),
        Candidate.Type.Folder
    ));

    IZipFileExtractionPathProvider zipFileExtractionPathProvider = mock(IZipFileExtractionPathProvider.class);
    when(zipFileExtractionPathProvider.getTemporaryPath(any(Path.class))).thenReturn(Paths.get("folder-a"));

    IInputStreamProvider inputStreamProvider = mock(IInputStreamProvider.class);
    when(inputStreamProvider.getInputStream(any(Path.class))).thenReturn(null);

    List<Candidate> candidateList = new ArrayList<>();
    candidateList.add(new Candidate(Paths.get("folder-a"), Candidate.Type.Compressed));

    CandidateListExtractor candidateListExtractor = new CandidateListExtractor(
        zipFileExtractor,
        zipFileExtractionPathProvider,
        inputStreamProvider,
        null
    );

    List<Candidate> newList = candidateListExtractor.extract(candidateList);

    Assert.assertEquals(1, newList.size());
    Assert.assertEquals(Candidate.Type.Folder, newList.get(0).getType());
    Assert.assertEquals(Paths.get("test-path"), newList.get(0).getPath());
  }

  @Test
  public void shouldCallRecursivePathCleanupWhenExtractionFails() throws IOException {

    IZipFileExtractor zipFileExtractor = mock(IZipFileExtractor.class);
    when(zipFileExtractor.extract(any(), eq(Paths.get("folder-a")))).thenThrow(IOException.class);
    when(zipFileExtractor.extract(any(), eq(Paths.get("folder-b")))).thenReturn(new Candidate(
        Paths.get("test-path"),
        Candidate.Type.Folder
    ));

    IZipFileExtractionPathProvider zipFileExtractionPathProvider = mock(IZipFileExtractionPathProvider.class);
    when(zipFileExtractionPathProvider.getTemporaryPath(Paths.get("folder-a"))).thenReturn(Paths.get("folder-a"));
    when(zipFileExtractionPathProvider.getTemporaryPath(Paths.get("folder-b"))).thenReturn(Paths.get("folder-b"));

    IInputStreamProvider inputStreamProvider = mock(IInputStreamProvider.class);
    when(inputStreamProvider.getInputStream(any(Path.class))).thenReturn(null);

    RecursiveFileRemovalProcessor fileRemovalProcessor = mock(RecursiveFileRemovalProcessor.class);

    List<Candidate> candidateList = new ArrayList<>();
    candidateList.add(new Candidate(Paths.get("folder-a"), Candidate.Type.Compressed));
    candidateList.add(new Candidate(Paths.get("folder-b"), Candidate.Type.Compressed));

    CandidateListExtractor candidateListExtractor = new CandidateListExtractor(
        zipFileExtractor,
        zipFileExtractionPathProvider,
        inputStreamProvider,
        fileRemovalProcessor
    );

    List<Candidate> newList = candidateListExtractor.extract(candidateList);

    Assert.assertEquals(1, newList.size());
    Assert.assertEquals(Candidate.Type.Folder, newList.get(0).getType());
    Assert.assertEquals(Paths.get("test-path"), newList.get(0).getPath());

    verify(fileRemovalProcessor, times(1)).deleteRecursively(Paths.get("folder-a"));
  }

  @Test
  public void shouldKeepNonCompressedCandidatesInList() throws IOException {

    List<Candidate> candidateList = new ArrayList<>();
    candidateList.add(new Candidate(Paths.get("folder-a"), Candidate.Type.Folder));

    CandidateListExtractor candidateListExtractor = new CandidateListExtractor(
        null,
        null,
        null,
        null
    );

    List<Candidate> newList = candidateListExtractor.extract(candidateList);

    Assert.assertEquals(1, newList.size());
    Assert.assertEquals(Candidate.Type.Folder, newList.get(0).getType());
    Assert.assertEquals(Paths.get("folder-a"), newList.get(0).getPath());
  }

}
