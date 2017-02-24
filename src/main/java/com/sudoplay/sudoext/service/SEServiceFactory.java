package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.candidate.CandidateListExtractor;
import com.sudoplay.sudoext.candidate.CandidateListProvider;
import com.sudoplay.sudoext.candidate.extractor.CompressedCandidateExtractor;
import com.sudoplay.sudoext.candidate.extractor.TemporaryPathProvider;
import com.sudoplay.sudoext.candidate.locator.CandidateCompressedFileLocator;
import com.sudoplay.sudoext.candidate.locator.CandidateFolderLocator;
import com.sudoplay.sudoext.candidate.locator.ICandidateLocator;
import com.sudoplay.sudoext.classloader.ClassLoaderFactoryProvider;
import com.sudoplay.sudoext.classloader.intercept.DefaultClassInterceptorFactory;
import com.sudoplay.sudoext.config.Config;
import com.sudoplay.sudoext.container.CandidateListConverter;
import com.sudoplay.sudoext.container.ContainerFactory;
import com.sudoplay.sudoext.container.ContainerListValidator;
import com.sudoplay.sudoext.folder.DefaultFolderLifecycleInitializeEventHandler;
import com.sudoplay.sudoext.folder.FolderLifecycleEventPlugin;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudoext.folder.TempFolderLifecycleEventHandler;
import com.sudoplay.sudoext.meta.ContainerListMetaLoader;
import com.sudoplay.sudoext.meta.parser.IElementParser;
import com.sudoplay.sudoext.meta.parser.MetaParser;
import com.sudoplay.sudoext.meta.parser.element.*;
import com.sudoplay.sudoext.meta.validator.MetaValidator;

/**
 * Creates a default implementation of the {@link SEService} with the given {@link Config}.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class SEServiceFactory {

  /**
   * Creates a default implementation of the {@link SEService} with the given {@link Config}.
   *
   * @param config config
   * @return default Service implementation
   */
  public static SEService create(Config config) throws SEServiceInitializationException {

    return new SEService(
        new FolderLifecycleEventPlugin(
            new IFolderLifecycleEventHandler[]{
                new DefaultFolderLifecycleInitializeEventHandler(
                    config.getLocation()
                ),
                new DefaultFolderLifecycleInitializeEventHandler(
                    config.getDataLocation()
                ),
                new TempFolderLifecycleEventHandler(
                    config.getTempLocation()
                )
            }
        ),
        new CandidateListProvider(
            new ICandidateLocator[]{
                new CandidateFolderLocator(
                    config.getMetaFilename(),
                    config.isFollowLinks()
                ),
                new CandidateCompressedFileLocator(
                    config.getMetaFilename(),
                    config.getCompressedFileExtension(),
                    config.isFollowLinks()
                )
            },
            config.getLocation()
        ),
        new CandidateListExtractor(
            new CompressedCandidateExtractor(),
            new TemporaryPathProvider(
                config.getTempLocation(),
                config.getCompressedFileExtension()
            )
        ),
        new CandidateListConverter(
            new ContainerFactory(
                config.getContainerCacheFactory()
            )
        ),
        new ContainerListMetaLoader(
            new MetaParser(
                new IElementParser[]{
                    new ApiVersionRangeParser(
                        config.getDefaultMetaApiVersionString()
                    ),
                    new AuthorParser(),
                    new DependsOnParser(),
                    new DescriptionParser(),
                    new IdParser(),
                    new ImageParser(),
                    new JarParser(),
                    new PluginParser(),
                    new NameParser(),
                    new WebsiteParser(),
                    new VersionParser()
                }
            ),
            config.getMetaFactory(),
            config.getMetaFilename()
        ),
        new ContainerListValidator(
            new MetaValidator(
                config.getApiVersion()
            )
        ),
        config.getContainerSorter(),
        new ClassLoaderFactoryProvider(
            config.getClassFilters(),
            new DefaultClassInterceptorFactory(
                config.getClassIntercepts()
            )
        )
    );
  }

  private SEServiceFactory() {
    //
  }
}
