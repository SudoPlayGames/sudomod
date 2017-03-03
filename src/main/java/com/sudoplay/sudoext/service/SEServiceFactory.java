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

    processor = new AdaptedMetaListProcessor(
        null,
        new MetaAdapter(
            metaElementAdapters
        ),
        new DefaultMetaJsonLoader(
            new DefaultStringLoader(
                charset
            ),
            new DefaultJsonAdapter(),
            metaFilename
        )
    );

    processor = new ValidatedMetaListProcessor(
        processor,
        new MetaValidator(
            metaValidators
        )
    );

    processor = new SortedMetaListProcessor(
        processor,
        new TopologicalSort<>()
    );

    processor = new DependencyValidatedMetaListProcessor(
        processor
    );

    processor = new PruneInvalidMetaListProcessor(
        processor
    );

    metaListProvider = new MetaListProvider(
        new CandidateListProvider(
            candidateLocators
        ),
        processor,
        metaFilename
    );

    metaListProvider = new CachedMetaListProvider(
        metaListProvider
    );

    containerMapProvider = new ContainerMapCreator(
        metaListProvider,
        containerCacheFactory,
        new PluginInstantiator()
    );

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

    return new SEService(
        new FolderLifecycleEventPlugin(
            folderLifecycleEventHandlers
        ),
        containerMapProvider.getContainerMap()
    );
  }
}
