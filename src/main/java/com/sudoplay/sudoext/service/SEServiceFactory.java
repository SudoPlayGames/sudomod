package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.candidate.CandidateListExtractor;
import com.sudoplay.sudoext.candidate.CandidateListProvider;
import com.sudoplay.sudoext.candidate.extractor.TemporaryPathProvider;
import com.sudoplay.sudoext.classloader.ClassLoaderFactoryProvider;
import com.sudoplay.sudoext.classloader.intercept.DefaultClassInterceptorFactory;
import com.sudoplay.sudoext.container.CandidateListConverter;
import com.sudoplay.sudoext.container.ContainerFactory;
import com.sudoplay.sudoext.container.ContainerListValidator;
import com.sudoplay.sudoext.folder.FolderLifecycleEventPlugin;
import com.sudoplay.sudoext.meta.ContainerListMetaLoader;
import com.sudoplay.sudoext.meta.parser.MetaParser;
import com.sudoplay.sudoext.meta.validator.MetaValidator;

/**
 * Created by codetaylor on 2/24/2017.
 */
public class SEServiceFactory {

  public static SEService create(
      SEServiceBuilder builder,
      SEConfig config
  ) throws SEServiceInitializationException {

    return new SEService(
        new FolderLifecycleEventPlugin(
            builder.getFolderLifecycleEventHandlers()
        ),
        new CandidateListProvider(
            builder.getCandidateLocators(),
            config.getLocation()
        ),
        new CandidateListExtractor(
            builder.getCompressedCandidateExtractor(),
            new TemporaryPathProvider(
                config.getTempLocation(),
                config.getCompressedFileExtension()
            )
        ),
        new CandidateListConverter(
            new ContainerFactory(
                builder.getContainerCacheFactory()
            )
        ),
        new ContainerListMetaLoader(
            new MetaParser(
                builder.getMetaElementParsers()
            ),
            builder.getMetaFactory(),
            config.getMetaFilename()
        ),
        new ContainerListValidator(
            new MetaValidator(
                builder.getMetaValidators()
            )
        ),
        builder.getContainerSorter(),
        new ClassLoaderFactoryProvider(
            builder.getClassFilters(),
            new DefaultClassInterceptorFactory(
                builder.getClassIntercepts()
            )
        )
    );
  }

  private SEServiceFactory() {
    //
  }
}
