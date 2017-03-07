package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.internal.ILogAPIProvider;
import com.sudoplay.sudoext.api.internal.PathProvider;
import com.sudoplay.sudoext.api.internal.Slf4JLogAPIProvider;
import com.sudoplay.sudoext.candidate.*;
import com.sudoplay.sudoext.candidate.extractor.ZipFileExtractionPathProvider;
import com.sudoplay.sudoext.candidate.extractor.ZipFileExtractor;
import com.sudoplay.sudoext.classloader.asm.callback.AccountingCallbackDelegateFactory;
import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegateFactory;
import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoext.classloader.filter.IClassFilter;
import com.sudoplay.sudoext.classloader.intercept.StaticInjector;
import com.sudoplay.sudoext.container.Container;
import com.sudoplay.sudoext.container.IContainerCacheFactory;
import com.sudoplay.sudoext.container.LRUContainerCacheFactory;
import com.sudoplay.sudoext.folder.DefaultFolderLifecycleInitializeEventHandler;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudoext.folder.TempFolderLifecycleEventHandler;
import com.sudoplay.sudoext.meta.adapter.IMetaAdapter;
import com.sudoplay.sudoext.meta.adapter.element.*;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import com.sudoplay.sudoext.meta.validator.element.*;
import com.sudoplay.sudoext.util.PreCondition;
import com.sudoplay.sudoext.util.RecursiveFileRemovalProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Configures and creates an implementation of the {@link SEService} with the given {@link SEConfig}.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class SEServiceBuilder {

  private SEConfig config;

  private ICallbackDelegateFactory callbackDelegateFactory;
  private IContainerCacheFactory containerCacheFactory;
  private IByteCodeTransformer byteCodeTransformer;

  private List<IClassFilter> defaultClassLoaderClassFilterList;
  private List<IClassFilter> classLoaderClassFilterList;

  private List<StaticInjector<?>> defaultStaticInjectorList;
  private List<StaticInjector<?>> staticInjectorList;

  public SEServiceBuilder(SEConfigBuilder configBuilder) {
    this.config = PreCondition.notNull(configBuilder).getConfig();

    // init user-defined lists
    this.classLoaderClassFilterList = new ArrayList<>();
    this.staticInjectorList = new ArrayList<>();

    // init component objects
    this.containerCacheFactory = new LRUContainerCacheFactory(64);

    // init the bytecode transformer
    this.byteCodeTransformer = new SEByteCodeTransformerBuilder()
        .create();

    // adds the default class filters
    this.defaultClassLoaderClassFilterList = new ArrayList<>();

    // adds the default static injectors
    this.defaultStaticInjectorList = new ArrayList<>();
    this.defaultStaticInjectorList.add(new StaticInjector<>(
        ILogAPIProvider.class,
        container -> new Slf4JLogAPIProvider(container.getId())
    ));
    this.defaultStaticInjectorList.add(new StaticInjector<>(
        String.class,
        "container-id",
        Container::getId
    ));
    this.defaultStaticInjectorList.add(new StaticInjector<>(
        PathProvider.class,
        container -> new PathProvider(container.getPath())
    ));

    this.callbackDelegateFactory = new AccountingCallbackDelegateFactory();
  }

  public SEServiceBuilder addStaticInjector(@NotNull StaticInjector<?> staticInjector) {
    this.staticInjectorList.add(PreCondition.notNull(staticInjector));
    return this;
  }

  public SEServiceBuilder removeAllDefaultStaticInjectors() {
    this.defaultStaticInjectorList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultStaticInjector(@NotNull Class<? extends StaticInjector<?>> aClass) {
    return this.removeByClass(PreCondition.notNull(aClass), this.defaultStaticInjectorList);
  }

  public SEServiceBuilder setCallbackDelegateFactory(@NotNull ICallbackDelegateFactory callbackDelegateFactory) {
    this.callbackDelegateFactory = PreCondition.notNull(callbackDelegateFactory);
    return this;
  }

  public SEServiceBuilder addClassLoaderClassFilter(@NotNull IClassFilter filter) {
    this.classLoaderClassFilterList.add(PreCondition.notNull(filter));
    return this;
  }

  public SEServiceBuilder removeAllDefaultClassLoaderClassFilters() {
    this.defaultClassLoaderClassFilterList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultClassLoaderClassFilter(@NotNull Class<? extends IClassFilter> aClass) {
    return this.removeByClass(PreCondition.notNull(aClass), this.defaultClassLoaderClassFilterList);
  }

  public SEServiceBuilder setContainerCacheFactory(@NotNull IContainerCacheFactory factory) {
    this.containerCacheFactory = PreCondition.notNull(factory);
    return this;
  }

  public SEServiceBuilder setByteCodeTransformer(@NotNull SEByteCodeTransformerBuilder builder) {
    this.byteCodeTransformer = PreCondition.notNull(builder).create();
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

  public SEService create() throws SEServiceInitializationException {

    RecursiveFileRemovalProcessor recursiveFileRemovalProcessor;
    PluginFinder pluginFinder;

    recursiveFileRemovalProcessor = new RecursiveFileRemovalProcessor();

    pluginFinder = new PluginFinder(
        new ZipSearch()
    );

    return new SEServiceFactory().create(
        new ICandidateProvider[]{
            new FileSystemCandidateProvider(
                new FolderPathListProvider(
                    this.config.getLocation()
                ),
                new FolderPathValidator(
                    this.config.getMetaFilename()
                ),
                new DefaultCandidateFactory(),
                new NoOpCandidateProcessor()
            ),
            new FileSystemCandidateProvider(
                new FileExtensionPathListProvider(
                    this.config.getLocation(),
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
            new OptionalRegisterAdapter(),
            new OptionalPreloadAdapter()
        },
        new IMetaValidator[]{
            new IdValidator(),
            new ApiVersionValidator(this.config.getApiVersion()),
            new DependsOnValidator(),
            new JarValidator(),
            new RegisterValidator(
                pluginFinder
            ),
            new PreloadValidator(
                pluginFinder
            )
        },
        this.containerCacheFactory,
        this.getClassLoaderClassFilters(),
        this.getStaticInjectors(),
        this.callbackDelegateFactory,
        this.byteCodeTransformer,
        new IFolderLifecycleEventHandler[]{
            new DefaultFolderLifecycleInitializeEventHandler(
                this.config.getLocation()
            ),
            new DefaultFolderLifecycleInitializeEventHandler(
                this.config.getDataLocation()
            ),
            new TempFolderLifecycleEventHandler(
                this.config.getTempLocation(),
                recursiveFileRemovalProcessor
            )
        },
        this.config.getCharset(),
        this.config.getMetaFilename()
    );
  }

  private SEServiceBuilder removeByClass(Class<?> aClass, List<?> list) {

    for (Iterator<?> it = list.iterator(); it.hasNext(); ) {

      if (aClass.isAssignableFrom(it.next().getClass())) {
        it.remove();
      }
    }
    return this;
  }
}
