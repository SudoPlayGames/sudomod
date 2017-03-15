package com.sudoplay.sudoxt.service;

import com.sudoplay.sudoxt.candidate.*;
import com.sudoplay.sudoxt.candidate.extractor.ZipFileExtractionPathProvider;
import com.sudoplay.sudoxt.candidate.extractor.ZipFileExtractor;
import com.sudoplay.sudoxt.classloader.asm.callback.AccountingCallbackDelegateFactory;
import com.sudoplay.sudoxt.classloader.asm.callback.ICallbackDelegateFactory;
import com.sudoplay.sudoxt.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoxt.classloader.filter.IClassFilter;
import com.sudoplay.sudoxt.classloader.intercept.SandboxPathProviderStaticInjector;
import com.sudoplay.sudoxt.classloader.intercept.StaticInjector;
import com.sudoplay.sudoxt.container.IContainerCacheFactory;
import com.sudoplay.sudoxt.container.LRUContainerCacheFactory;
import com.sudoplay.sudoxt.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudoxt.folder.TempFolderLifecycleEventHandler;
import com.sudoplay.sudoxt.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoxt.meta.adapter.element.*;
import com.sudoplay.sudoxt.meta.validator.IMetaValidator;
import com.sudoplay.sudoxt.meta.validator.element.*;
import com.sudoplay.sudoxt.util.PreCondition;
import com.sudoplay.sudoxt.util.RecursiveFileRemovalProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Configures and creates an implementation of the {@link SXService} with the given {@link SXConfig}.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class SXServiceBuilder {

  private SXConfig config;

  private ICallbackDelegateFactory callbackDelegateFactory;
  private IContainerCacheFactory containerCacheFactory;
  private SEByteCodeTransformerBuilder byteCodeTransformerBuilder;

  private List<IClassFilter> defaultClassLoaderClassFilterList;
  private List<IClassFilter> classLoaderClassFilterList;

  private List<StaticInjector<?>> defaultStaticInjectorList;
  private List<StaticInjector<?>> staticInjectorList;

  public SXServiceBuilder(SXConfigBuilder configBuilder) {
    this.config = PreCondition.notNull(configBuilder).getConfig();

    // init user-defined lists
    this.classLoaderClassFilterList = new ArrayList<>();
    this.staticInjectorList = new ArrayList<>();

    // init component objects
    this.containerCacheFactory = new LRUContainerCacheFactory(64);

    // init the bytecode transformer
    this.byteCodeTransformerBuilder = new SEByteCodeTransformerBuilder();

    // adds the default class filters
    this.defaultClassLoaderClassFilterList = new ArrayList<>();

    // adds the default static injectors
    this.defaultStaticInjectorList = new ArrayList<>();
    this.defaultStaticInjectorList.add(new SandboxPathProviderStaticInjector());

    this.callbackDelegateFactory = new AccountingCallbackDelegateFactory();
  }

  public SXServiceBuilder addStaticInjector(@NotNull StaticInjector<?> staticInjector) {
    this.staticInjectorList.add(PreCondition.notNull(staticInjector));
    return this;
  }

  public SXServiceBuilder removeAllDefaultStaticInjectors() {
    this.defaultStaticInjectorList.clear();
    return this;
  }

  public SXServiceBuilder removeDefaultStaticInjector(@NotNull Class<? extends StaticInjector<?>> aClass) {
    return this.removeByClass(PreCondition.notNull(aClass), this.defaultStaticInjectorList);
  }

  public SXServiceBuilder setCallbackDelegateFactory(@NotNull ICallbackDelegateFactory callbackDelegateFactory) {
    this.callbackDelegateFactory = PreCondition.notNull(callbackDelegateFactory);
    return this;
  }

  public SXServiceBuilder addClassLoaderClassFilter(@NotNull IClassFilter filter) {
    this.classLoaderClassFilterList.add(PreCondition.notNull(filter));
    return this;
  }

  public SXServiceBuilder removeAllDefaultClassLoaderClassFilters() {
    this.defaultClassLoaderClassFilterList.clear();
    return this;
  }

  public SXServiceBuilder removeDefaultClassLoaderClassFilter(@NotNull Class<? extends IClassFilter> aClass) {
    return this.removeByClass(PreCondition.notNull(aClass), this.defaultClassLoaderClassFilterList);
  }

  public SXServiceBuilder setContainerCacheFactory(@NotNull IContainerCacheFactory factory) {
    this.containerCacheFactory = PreCondition.notNull(factory);
    return this;
  }

  public SXServiceBuilder setByteCodeTransformerBuilder(@NotNull SEByteCodeTransformerBuilder builder) {
    this.byteCodeTransformerBuilder = PreCondition.notNull(builder);
    return this;
  }

  private IClassFilter[] getClassLoaderClassFilters() {
    List<IClassFilter> list = new ArrayList<>();
    list.addAll(this.defaultClassLoaderClassFilterList);
    list.addAll(this.classLoaderClassFilterList);
    return list.toArray(new IClassFilter[list.size()]);
  }

  private StaticInjector<?>[] getStaticInjectors() {
    List<StaticInjector<?>> list = new ArrayList<>();
    list.addAll(this.defaultStaticInjectorList);
    list.addAll(this.staticInjectorList);
    return list.toArray(new StaticInjector<?>[list.size()]);
  }

  public SXService create() throws SXServiceInitializationException {

    RecursiveFileRemovalProcessor recursiveFileRemovalProcessor;
    PluginFinder pluginFinder;

    recursiveFileRemovalProcessor = new RecursiveFileRemovalProcessor();

    pluginFinder = new PluginFinder(
        new ZipSearch()
    );

    return new SXServiceFactory().create(
        new ICandidateProvider[]{
            new FileSystemCandidateProvider(
                new FolderPathListProvider(
                    this.config.getLocations()
                ),
                new FolderPathValidator(
                    this.config.getMetaFilename()
                ),
                new DefaultCandidateFactory(),
                new NoOpCandidateProcessor()
            ),
            new FileSystemCandidateProvider(
                new FileExtensionPathListProvider(
                    this.config.getLocations(),
                    this.config.getCompressedFileExtension()
                ),
                new CompressedFilePathValidator(
                    this.config.getMetaFilename()
                ),
                new DefaultCandidateFactory(),
                new CompressedFileCandidateProcessor(
                    new ZipFileExtractor(),
                    new ZipFileExtractionPathProvider(
                        this.config.getTempLocation(),
                        this.config.getCompressedFileExtension()
                    ),
                    new DefaultInputStreamProvider(),
                    recursiveFileRemovalProcessor
                )
            )
        },
        new IMetaAdapter[]{
            new IdAdapter(),
            new NameAdapter(),
            new AuthorAdapter(),
            new VersionAdapter(),
            new DescriptionAdapter(),
            new OptionalWebsiteAdapter(),
            new OptionalApiVersionAdapter(),
            new OptionalDependsOnAdapter(),
            new OptionalJarAdapter(),
            new OptionalRegisterAdapter(
                this.config.isAutoPreloadRegisteredPlugins()
            ),
            new OptionalPreloadAdapter(),
            new OptionalOverrideAdapter()
        },
        new IMetaValidator[]{
            new IdValidator(),
            new ApiVersionValidator(
                this.config.getApiVersion()
            ),
            new DependsOnValidator(),
            new JarValidator(),
            new RegisterValidator(
                pluginFinder
            ),
            new PreloadValidator(
                pluginFinder
            ),
            new OverrideValidator(
                pluginFinder
            )
        },
        this.containerCacheFactory,
        this.getClassLoaderClassFilters(),
        this.getStaticInjectors(),
        this.callbackDelegateFactory,
        this.byteCodeTransformerBuilder,
        new IFolderLifecycleEventHandler[]{
            new TempFolderLifecycleEventHandler(
                this.config.getTempLocation(),
                recursiveFileRemovalProcessor
            )
        },
        this.config.getCharset(),
        this.config.getMetaFilename()
    );
  }

  private SXServiceBuilder removeByClass(Class<?> aClass, List<?> list) {

    for (Iterator<?> it = list.iterator(); it.hasNext(); ) {

      if (aClass.isAssignableFrom(it.next().getClass())) {
        it.remove();
      }
    }
    return this;
  }
}
