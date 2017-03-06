package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.candidate.CandidateListProvider;
import com.sudoplay.sudoext.candidate.ICandidateProvider;
import com.sudoplay.sudoext.classloader.ClassLoaderFactoryProvider;
import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegateFactory;
import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.filter.ClassFilterPredicate;
import com.sudoplay.sudoext.classloader.filter.IClassFilter;
import com.sudoplay.sudoext.classloader.intercept.ClassIntercept;
import com.sudoplay.sudoext.classloader.intercept.DefaultClassInterceptorFactory;
import com.sudoplay.sudoext.container.*;
import com.sudoplay.sudoext.folder.FolderLifecycleEventPlugin;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudoext.meta.*;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoext.meta.adapter.MetaAdapter;
import com.sudoplay.sudoext.meta.processor.*;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import com.sudoplay.sudoext.meta.validator.MetaValidator;
import com.sudoplay.sudoext.sort.TopologicalSort;
import com.sudoplay.sudoext.util.InputStreamByteArrayConverter;

import java.nio.charset.Charset;

/**
 * Created by codetaylor on 3/3/2017.
 */
/* package */ class SEServiceFactory {

  /* package */ SEService create(
      ICandidateProvider[] candidateLocators,
      IMetaAdapter[] metaElementAdapters,
      IMetaValidator[] metaValidators,
      IContainerCacheFactory containerCacheFactory,
      IClassFilter[] classLoaderClassFilters,
      ClassIntercept[] classIntercepts,
      ICallbackDelegateFactory callbackDelegateFactory,
      IByteCodeTransformer byteCodeTransformer,
      IFolderLifecycleEventHandler[] folderLifecycleEventHandlers,
      Charset charset,
      String metaFilename
  ) throws SEServiceInitializationException {

    IMetaListProvider metaListProvider;
    ChainedMetaListProcessor processor;
    IContainerMapProvider containerMapProvider;

    // adapts meta
    processor = new AdaptedMetaListProcessor(
        null,
        new MetaAdapter(
            metaElementAdapters
        ),
        new DefaultMetaJsonLoader(
            new DefaultStringLoader(
                charset
            ),
            new DefaultJsonAdapter()
        )
    );

    // validates meta
    processor = new ValidatedMetaListProcessor(
        processor,
        new MetaValidator(
            metaValidators
        )
    );

    // sorts meta
    processor = new SortedMetaListProcessor(
        processor,
        new TopologicalSort<>()
    );

    // validates dependencies
    processor = new DependencyValidatedMetaListProcessor(
        processor
    );

    // prunes invalid meta
    processor = new PruneInvalidMetaListProcessor(
        processor
    );

    // turns candidate into meta
    metaListProvider = new MetaListProvider(
        new CandidateListProvider(
            candidateLocators
        ),
        processor,
        metaFilename
    );

    // caches the meta list for multiple calls to this provider
    metaListProvider = new CachedMetaListProvider(
        metaListProvider
    );

    // creates container map
    containerMapProvider = new ContainerMapCreator(
        metaListProvider,
        new ContainerFactory(
            containerCacheFactory,
            callbackDelegateFactory,
            new PluginInstantiator()
        )
    );

    // assigns container classloader factories
    containerMapProvider = new InitializedContainerMapProvider(
        metaListProvider,
        containerMapProvider,
        new ClassLoaderFactoryProvider(
            new ClassFilterPredicate(
                classLoaderClassFilters
            ),
            new DefaultClassInterceptorFactory(
                classIntercepts
            ),
            byteCodeTransformer,
            new InputStreamByteArrayConverter()
        ),
        new DependencyContainerListMapper()
    );

    // creates the service
    SEService service = new SEService(
        new FolderLifecycleEventPlugin(
            folderLifecycleEventHandlers
        ),
        new PluginPreLoader(),
        containerMapProvider.getContainerMap()
    );

    // initializes folders
    service.initializeFolders();

    // initializes each container's cache and classloader
    service.reload();

    return service;
  }
}
