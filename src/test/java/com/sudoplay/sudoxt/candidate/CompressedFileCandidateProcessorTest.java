package com.sudoplay.sudoxt.candidate;

import com.sudoplay.sudoxt.candidate.extractor.IZipFileExtractionPathProvider;
import com.sudoplay.sudoxt.candidate.extractor.IZipFileExtractor;
import com.sudoplay.sudoxt.util.RecursiveFileRemovalProcessor;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by codetaylor on 2/25/2017.
 */
public class CompressedFileCandidateProcessorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void shouldReplaceCompressedCandidatesInList() throws IOException, CandidateProcessorException {

    IZipFileExtractor zipFileExtractor = mock(IZipFileExtractor.class);

    IZipFileExtractionPathProvider zipFileExtractionPathProvider = mock(IZipFileExtractionPathProvider.class);
    when(zipFileExtractionPathProvider.getTemporaryPath(any(Path.class))).thenReturn(Paths.get("test-path"));

    IInputStreamProvider inputStreamProvider = mock(IInputStreamProvider.class);
    when(inputStreamProvider.getInputStream(any(Path.class))).thenReturn(null);

    CompressedFileCandidateProcessor processor = new CompressedFileCandidateProcessor(
        zipFileExtractor,
        zipFileExtractionPathProvider,
        inputStreamProvider,
        null
    );

    Candidate candidate = processor.process(new Candidate(Paths.get("folder-a")));

    Assert.assertEquals(Paths.get("test-path"), candidate.getPath());
  }

  @Test
  public void shouldCallRecursivePathCleanupWhenExtractionFails() throws IOException {

    IZipFileExtractor zipFileExtractor = mock(IZipFileExtractor.class);
    doThrow(IOException.class).when(zipFileExtractor).extract(any(), any());

    IZipFileExtractionPathProvider zipFileExtractionPathProvider = mock(IZipFileExtractionPathProvider.class);
    when(zipFileExtractionPathProvider.getTemporaryPath(Paths.get("folder-a"))).thenReturn(Paths.get("test-path"));

    IInputStreamProvider inputStreamProvider = mock(IInputStreamProvider.class);
    when(inputStreamProvider.getInputStream(any(Path.class))).thenReturn(null);

    RecursiveFileRemovalProcessor fileRemovalProcessor = mock(RecursiveFileRemovalProcessor.class);

    CompressedFileCandidateProcessor processor = new CompressedFileCandidateProcessor(
        zipFileExtractor,
        zipFileExtractionPathProvider,
        inputStreamProvider,
        fileRemovalProcessor
    );

    try {
      processor.process(new Candidate(Paths.get("folder-a")));
      Assert.fail();

    } catch (CandidateProcessorException e) {
      // expected
    }

    verify(fileRemovalProcessor, times(1)).deleteRecursively(Paths.get("test-path"));
  }

}
