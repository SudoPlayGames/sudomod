package com.sudoplay.sudoxt.service;

import com.sudoplay.sudoxt.candidate.CandidateListProvider;
import com.sudoplay.sudoxt.candidate.ICandidateProvider;
import com.sudoplay.sudoxt.classloader.ClassLoaderFactoryProvider;
import com.sudoplay.sudoxt.classloader.ICompilerFactory;
import com.sudoplay.sudoxt.classloader.asm.callback.ICallbackDelegateFactory;
import com.sudoplay.sudoxt.classloader.asm.transform.SXByteCodeTransformerBuilder;
import com.sudoplay.sudoxt.classloader.filter.ClassFilterPredicate;
import com.sudoplay.sudoxt.classloader.filter.IClassFilter;
import com.sudoplay.sudoxt.classloader.intercept.DefaultClassInterceptorFactory;
import com.sudoplay.sudoxt.classloader.intercept.StaticInjector;
import com.sudoplay.sudoxt.container.*;
import com.sudoplay.sudoxt.folder.FolderLifecycleEventPlugin;
import com.sudoplay.sudoxt.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudoxt.meta.*;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoxt.meta.adapter.MetaAdapter;
import com.sudoplay.sudoxt.meta.processor.*;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import com.sudoplay.sudoxt.meta.validator.MetaValidator;
import com.sudoplay.sudoxt.sort.TopologicalSort;
import com.sudoplay.sudoxt.util.InputStreamByteArrayConverter;

import java.nio.charset.Charset;

/**
 * Created by codetaylor on 3/3/2017.
 */
/* package */ class SXServiceFactory {

  /* package */ SXService create(
      ICandidateProvider[] candidateLocators,
      IMetaAdapter[] metaElementAdapters,
      IMetaValidator[] metaValidators,
      IContainerCacheFactory containerCacheFactory,
      IClassFilter[] classLoaderClassFilters,
      StaticInjector<?>[] staticInjectors,
      ICallbackDelegateFactory callbackDelegateFactory,
      SXByteCodeTransformerBuilder byteCodeTransformerBuilder,
      IFolderLifecycleEventHandler[] folderLifecycleEventHandlers,
      Charset charset,
      String metaFilename,
      ICompilerFactory compilerFactory
  ) throws SXServiceInitializationException {

    FolderLifecycleEventPlugin folderLifecycleEventPlugin;
    IMetaListProvider metaListProvider;
    ChainedMetaListProcessor processor;
    IContainerMapProvider containerMapProvider;

    // initializes required folders (ie. mods-temp)
    folderLifecycleEventPlugin = new FolderLifecycleEventPlugin(
        folderLifecycleEventHandlers
    );

    folderLifecycleEventPlugin.initialize();

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
                staticInjectors
            ),
            byteCodeTransformerBuilder.create(),
            new InputStreamByteArrayConverter(),
            compilerFactory
        ),
        new DependencyContainerListMapper()
    );

    // assigns container override handlers
    containerMapProvider = new OverrideContainerMapProvider(
        metaListProvider,
        containerMapProvider
    );

    // creates the service
    return new SXService(
        folderLifecycleEventPlugin,
        new PluginPreLoader(),
        containerMapProvider.getContainerMap()
    );
  }
}
