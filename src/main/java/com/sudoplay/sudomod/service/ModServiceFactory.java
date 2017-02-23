package com.sudoplay.sudomod.service;

import com.sudoplay.sudomod.api.core.impl.Slf4jLoggingAPIProviderFactory_Impl;
import com.sudoplay.sudomod.config.Config;
import com.sudoplay.sudomod.folder.DefaultFolderLifecycleInitializeEventHandler;
import com.sudoplay.sudomod.folder.FolderLifecycleEventPlugin;
import com.sudoplay.sudomod.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudomod.folder.TempFolderLifecycleEventHandler;
import com.sudoplay.sudomod.mod.candidate.ModCandidateListExtractor;
import com.sudoplay.sudomod.mod.candidate.ModCandidateListProvider;
import com.sudoplay.sudomod.mod.candidate.extractor.CompressedModCandidateExtractor;
import com.sudoplay.sudomod.mod.candidate.extractor.TemporaryModPathProvider;
import com.sudoplay.sudomod.mod.candidate.locator.IModCandidateLocator;
import com.sudoplay.sudomod.mod.candidate.locator.ModCandidateCompressedFileLocator;
import com.sudoplay.sudomod.mod.candidate.locator.ModCandidateFolderLocator;
import com.sudoplay.sudomod.mod.container.*;
import com.sudoplay.sudomod.mod.info.DefaultModInfoFactory;
import com.sudoplay.sudomod.mod.info.ModContainerListInfoLoader;
import com.sudoplay.sudomod.mod.info.parser.IElementParser;
import com.sudoplay.sudomod.mod.info.parser.ModInfoParser;
import com.sudoplay.sudomod.mod.info.parser.element.*;
import com.sudoplay.sudomod.mod.info.validator.ModInfoValidator;

/**
 * Creates a default implementation of the {@link ModService} with the given {@link Config}.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class ModServiceFactory {

  /**
   * Creates a default implementation of the {@link ModService} with the given {@link Config}.
   *
   * @param config config
   * @return default ModService implementation
   */
  public static ModService create(Config config) throws ModServiceInitializationException {

    return new ModService(
        new FolderLifecycleEventPlugin(
            new IFolderLifecycleEventHandler[]{
                new DefaultFolderLifecycleInitializeEventHandler(
                    config.getModLocation()
                ),
                new DefaultFolderLifecycleInitializeEventHandler(
                    config.getModDataLocation()
                ),
                new TempFolderLifecycleEventHandler(
                    config.getModTempLocation()
                )
            }
        ),
        new ModCandidateListProvider(
            new IModCandidateLocator[]{
                new ModCandidateFolderLocator(
                    config.getModInfoFilename(),
                    config.isFollowLinks()
                ),
                new ModCandidateCompressedFileLocator(
                    config.getModInfoFilename(),
                    config.getCompressedModFileExtension(),
                    config.isFollowLinks()
                )
            },
            config.getModLocation()
        ),
        new ModCandidateListExtractor(
            new CompressedModCandidateExtractor(),
            new TemporaryModPathProvider(
                config.getModTempLocation(),
                config.getCompressedModFileExtension()
            )
        ),
        new ModCandidateListConverter(
            new ModContainerFactory(
                new LRUModContainerCacheFactory(64),
                new Slf4jLoggingAPIProviderFactory_Impl()
            )
        ),
        new ModContainerListInfoLoader(
            new ModInfoParser(
                new IElementParser[]{
                    new ApiVersionRangeParser(
                        config.getDefaultModInfoApiVersionString()
                    ),
                    new AuthorParser(),
                    new DependsOnParser(),
                    new DescriptionParser(),
                    new IdParser(),
                    new ImageParser(),
                    new JarParser(),
                    new ModPluginParser(),
                    new NameParser(),
                    new WebsiteParser(),
                    new VersionParser()
                }
            ),
            new DefaultModInfoFactory(),
            config.getModInfoFilename()
        ),
        new ModContainerListValidator(
            new ModInfoValidator(
                config.getApiVersion()
            )
        ),
        new ModContainerSorter(),
        config.getClassFilters()
    );
  }

  private ModServiceFactory() {
    //
  }
}
