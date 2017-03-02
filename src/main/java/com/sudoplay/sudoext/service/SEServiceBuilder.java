package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.ContainerAPI;
import com.sudoplay.sudoext.api.logging.Slf4jLoggerAPIProvider;
import com.sudoplay.sudoext.candidate.*;
import com.sudoplay.sudoext.candidate.extractor.IZipFileExtractor;
import com.sudoplay.sudoext.candidate.extractor.ZipFileExtractionPathProvider;
import com.sudoplay.sudoext.candidate.extractor.ZipFileExtractor;
import com.sudoplay.sudoext.classloader.ClassLoaderFactoryProvider;
import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoext.classloader.filter.ClassFilterPredicate;
import com.sudoplay.sudoext.classloader.filter.IClassFilter;
import com.sudoplay.sudoext.classloader.intercept.*;
import com.sudoplay.sudoext.container.ContainerFactory;
import com.sudoplay.sudoext.container.IContainerCacheFactory;
import com.sudoplay.sudoext.container.LRUContainerCacheFactory;
import com.sudoplay.sudoext.container.provider.*;
import com.sudoplay.sudoext.folder.DefaultFolderLifecycleInitializeEventHandler;
import com.sudoplay.sudoext.folder.FolderLifecycleEventPlugin;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudoext.folder.TempFolderLifecycleEventHandler;
import com.sudoplay.sudoext.meta.*;
import com.sudoplay.sudoext.meta.parser.IMetaElementAdapter;
import com.sudoplay.sudoext.meta.parser.MetaAdapter;
import com.sudoplay.sudoext.meta.parser.element.*;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import com.sudoplay.sudoext.meta.validator.MetaValidator;
import com.sudoplay.sudoext.meta.validator.element.ApiVersionValidator;
import com.sudoplay.sudoext.meta.validator.element.DependsOnValidator;
import com.sudoplay.sudoext.meta.validator.element.IdValidator;
import com.sudoplay.sudoext.meta.validator.element.JarValidator;
import com.sudoplay.sudoext.sort.TopologicalSort;
import com.sudoplay.sudoext.util.InputStreamByteArrayConverter;
import com.sudoplay.sudoext.util.RecursiveFileRemovalProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Creates a default implementation of the {@link SEService} with the given {@link SEConfig}.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class SEServiceBuilder {

  private SEConfig config;

  private IContainerCacheFactory containerCacheFactory;
  private IMetaFactory metaFactory;
  private IZipFileExtractor compressedCandidateExtractor;
  private IByteCodeTransformer byteCodeTransformer;

  private List<IClassFilter> defaultClassLoaderClassFilterList;
  private List<ClassIntercept> defaultClassInterceptList;
  private List<IMetaElementAdapter<? extends Meta>> defaultMetaElementAdapterList;
  private List<IMetaValidator> defaultMetaValidatorList;
  private List<ICandidateProvider> defaultCandidateLocatorList;
  private List<IFolderLifecycleEventHandler> defaultFolderLifecycleEventHandlerList;

  private List<IClassFilter> classLoaderClassFilterList;
  private List<ClassIntercept> classInterceptList;
  private List<IMetaElementAdapter<? extends Meta>> metaElementAdapterList;
  private List<IMetaValidator> metaValidatorList;
  private List<ICandidateProvider> candidateLocatorList;
  private List<IFolderLifecycleEventHandler> folderLifecycleEventHandlerList;

  public SEServiceBuilder(SEConfigBuilder configBuilder) {
    this.config = configBuilder.getConfig();

    // init user-defined lists
    this.classLoaderClassFilterList = new ArrayList<>();
    this.classInterceptList = new ArrayList<>();
    this.metaElementAdapterList = new ArrayList<>();
    this.metaValidatorList = new ArrayList<>();
    this.candidateLocatorList = new ArrayList<>();
    this.folderLifecycleEventHandlerList = new ArrayList<>();

    // init component objects
    this.containerCacheFactory = new LRUContainerCacheFactory(64);
    this.metaFactory = new DefaultMetaFactory();
    this.compressedCandidateExtractor = new ZipFileExtractor();

    // init the bytecode transformer
    this.byteCodeTransformer = new SEByteCodeTransformerBuilder()
        .create();

    // init statics
    RecursiveFileRemovalProcessor recursiveFileRemovalProcessor;
    recursiveFileRemovalProcessor = new RecursiveFileRemovalProcessor();

    // adds the default folder lifecycle event handlers
    this.defaultFolderLifecycleEventHandlerList = new ArrayList<>();
    this.defaultFolderLifecycleEventHandlerList.add(
        new DefaultFolderLifecycleInitializeEventHandler(
            this.config.getLocation()
        )
    );
    this.defaultFolderLifecycleEventHandlerList.add(
        new DefaultFolderLifecycleInitializeEventHandler(
            this.config.getDataLocation()
        )
    );
    this.defaultFolderLifecycleEventHandlerList.add(
        new TempFolderLifecycleEventHandler(
            this.config.getTempLocation(),
            recursiveFileRemovalProcessor
        )
    );

    // adds the default candidate locators
    this.defaultCandidateLocatorList = new ArrayList<>();
    this.defaultCandidateLocatorList.add(
        new FileSystemCandidateProvider(
            new FolderPathListProvider(
                this.config.getLocation()
            ),
            new FolderPathValidator(
                this.config.getMetaFilename()
            ),
            new DefaultCandidateFactory(),
            new NoOpCandidateProcessor()
        )
    );
    this.defaultCandidateLocatorList.add(
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
                this.getCompressedCandidateExtractor(),
                new ZipFileExtractionPathProvider(
                    this.config.getTempLocation(),
                    this.config.getCompressedFileExtension()
                ),
                new DefaultInputStreamProvider(),
                recursiveFileRemovalProcessor
            )
        )
    );

    // adds the default meta element parsers
    this.defaultMetaElementAdapterList = new ArrayList<>();
    this.defaultMetaElementAdapterList.add(new IdAdapter());
    this.defaultMetaElementAdapterList.add(new NameAdapter());
    this.defaultMetaElementAdapterList.add(new AuthorAdapter());
    this.defaultMetaElementAdapterList.add(new VersionAdapter());
    this.defaultMetaElementAdapterList.add(new DescriptionAdapter());
    this.defaultMetaElementAdapterList.add(new OptionalWebsiteAdapter());
    this.defaultMetaElementAdapterList.add(new OptionalApiVersionAdapter());
    this.defaultMetaElementAdapterList.add(new OptionalDependsOnAdapter(
        DependencyContainer::new
    ));
    this.defaultMetaElementAdapterList.add(new OptionalJarAdapter());

    // adds the default meta validators
    this.defaultMetaValidatorList = new ArrayList<>();
    this.defaultMetaValidatorList.add(new IdValidator());
    this.defaultMetaValidatorList.add(new ApiVersionValidator(this.config.getApiVersion()));
    this.defaultMetaValidatorList.add(new DependsOnValidator());
    this.defaultMetaValidatorList.add(new JarValidator());

    // adds the default class filter
    this.defaultClassLoaderClassFilterList = new ArrayList<>();

    // adds the default container api class intercept
    this.defaultClassInterceptList = new ArrayList<>();
    this.defaultClassInterceptList.add(new ClassIntercept(
        ContainerAPI.class,
        new DelegateClassInterceptProcessor(
            new IClassInterceptProcessor[]{
                new StaticFieldClassInterceptProcessor(
                    "LOGGING_API_PROVIDER",
                    container -> new Slf4jLoggerAPIProvider(container.getMeta().getId())
                ),
                new StaticFieldClassInterceptProcessor(
                    "ID",
                    container -> container.getMeta().getId()
                )
            }
        )
    ));

  }

  public SEServiceBuilder addClassLoaderClassFilter(IClassFilter filter) {
    this.classLoaderClassFilterList.add(filter);
    return this;
  }

  public SEServiceBuilder removeAllDefaultClassLoaderClassFilters() {
    this.defaultClassLoaderClassFilterList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultClassLoaderClassFilter(Class<? extends IClassFilter> aClass) {
    return this.removeByClass(aClass, this.defaultClassLoaderClassFilterList);
  }

  public SEServiceBuilder setContainerCacheFactory(IContainerCacheFactory factory) {
    this.containerCacheFactory = factory;
    return this;
  }

  public SEServiceBuilder addClassIntercept(ClassIntercept classIntercept) {
    this.classInterceptList.add(classIntercept);
    return this;
  }

  public SEServiceBuilder removeAllDefaultClassIntercepts() {
    this.defaultClassInterceptList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultClassIntercept(Class<? extends ClassIntercept> aClass) {
    return this.removeByClass(aClass, this.defaultClassInterceptList);
  }

  public SEServiceBuilder setMetaFactory(IMetaFactory metaFactory) {
    this.metaFactory = metaFactory;
    return this;
  }

  public SEServiceBuilder addMetaElementAdapter(IMetaElementAdapter<? extends Meta> adapter) {
    this.metaElementAdapterList.add(adapter);
    return this;
  }

  public SEServiceBuilder removeAllDefaultMetaElementAdapters() {
    this.defaultMetaElementAdapterList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultMetaElementAdapter(Class<? extends IMetaElementAdapter> aClass) {
    return this.removeByClass(aClass, this.defaultMetaElementAdapterList);
  }

  public SEServiceBuilder addMetaValidator(IMetaValidator validator) {
    this.metaValidatorList.add(validator);
    return this;
  }

  public SEServiceBuilder removeAllDefaultMetaValidators() {
    this.defaultMetaValidatorList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultMetaValidator(Class<? extends IMetaValidator> aClass) {
    return this.removeByClass(aClass, this.defaultMetaValidatorList);
  }

  public SEServiceBuilder setCompressedCandidateExtractor(IZipFileExtractor extractor) {
    this.compressedCandidateExtractor = extractor;
    return this;
  }

  public SEServiceBuilder addCandidateLocator(ICandidateProvider locator) {
    this.candidateLocatorList.add(locator);
    return this;
  }

  public SEServiceBuilder removeAllDefaultCandidateLocators() {
    this.defaultCandidateLocatorList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultCandidateLocator(Class<? extends ICandidateProvider> aClass) {
    return this.removeByClass(aClass, this.defaultCandidateLocatorList);
  }

  public SEServiceBuilder addFolderLifecycleEventHandler(IFolderLifecycleEventHandler handler) {
    this.folderLifecycleEventHandlerList.add(handler);
    return this;
  }

  public SEServiceBuilder removeAllDefaultFolderLifecycleEventHandlers() {
    this.defaultFolderLifecycleEventHandlerList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultFolderLifecycleEventHandler(
      Class<? extends IFolderLifecycleEventHandler> aClass
  ) {
    return this.removeByClass(aClass, this.defaultFolderLifecycleEventHandlerList);
  }

  public SEServiceBuilder setByteCodeTransformer(IByteCodeTransformer byteCodeTransformer) {
    this.byteCodeTransformer = byteCodeTransformer;
    return this;
  }

  private IClassFilter[] getClassLoaderClassFilters() {
    List<IClassFilter> list = new ArrayList<>();
    list.addAll(this.defaultClassLoaderClassFilterList);
    list.addAll(this.classLoaderClassFilterList);
    return list.toArray(new IClassFilter[list.size()]);
  }

  private ClassIntercept[] getClassIntercepts() {
    List<ClassIntercept> list = new ArrayList<>();
    list.addAll(this.defaultClassInterceptList);
    list.addAll(this.classInterceptList);
    return list.toArray(new ClassIntercept[list.size()]);
  }

  private IMetaElementAdapter[] getMetaElementAdapters() {
    List<IMetaElementAdapter<? extends Meta>> list = new ArrayList<>();
    list.addAll(this.defaultMetaElementAdapterList);
    list.addAll(this.metaElementAdapterList);
    return list.toArray(new IMetaElementAdapter[list.size()]);
  }

  private IMetaValidator[] getMetaValidators() {
    List<IMetaValidator> list = new ArrayList<>();
    list.addAll(this.defaultMetaValidatorList);
    list.addAll(this.metaValidatorList);
    return list.toArray(new IMetaValidator[list.size()]);
  }

  private ICandidateProvider[] getCandidateLocators() {
    List<ICandidateProvider> list = new ArrayList<>();
    list.addAll(this.defaultCandidateLocatorList);
    list.addAll(this.candidateLocatorList);
    return list.toArray(new ICandidateProvider[list.size()]);
  }

  private IFolderLifecycleEventHandler[] getFolderLifecycleEventHandlers() {
    List<IFolderLifecycleEventHandler> list = new ArrayList<>();
    list.addAll(this.defaultFolderLifecycleEventHandlerList);
    list.addAll(this.folderLifecycleEventHandlerList);
    return list.toArray(new IFolderLifecycleEventHandler[list.size()]);
  }

  private IContainerCacheFactory getContainerCacheFactory() {
    return this.containerCacheFactory;
  }

  private IMetaFactory getMetaFactory() {
    return this.metaFactory;
  }

  private IZipFileExtractor getCompressedCandidateExtractor() {
    return this.compressedCandidateExtractor;
  }

  private IByteCodeTransformer getByteCodeTransformer() {
    return this.byteCodeTransformer;
  }

  public SEService create() throws SEServiceInitializationException {

    ICandidateListProvider candidateListProvider = new CandidateListProvider(
        this.getCandidateLocators()
    );

    IContainerListProvider candidateContainerListProvider = new CandidateContainerListProvider(
        candidateListProvider,
        new ContainerFactory(
            this.getContainerCacheFactory()
        )
    );

    IContainerListProvider adaptedMetaContainerListProvider = new AdaptedMetaContainerListProvider(
        candidateContainerListProvider,
        new MetaAdapter(
            this.getMetaElementAdapters()
        ),
        this.getMetaFactory(),
        new DefaultMetaJsonProvider(
            new DefaultStringLoader(
                this.config.getCharset()
            ),
            new DefaultJsonAdapter(),
            this.config.getMetaFilename()
        )
    );

    IContainerListProvider metaValidatedContainerListProvider = new MetaValidatedContainerListProvider(
        adaptedMetaContainerListProvider,
        new MetaValidator(
            this.getMetaValidators()
        )
    );

    IContainerListProvider sortedContainerListProvider = new SortedContainerListProvider(
        metaValidatedContainerListProvider,
        new TopologicalSort()
    );

    IContainerListProvider dependencyValidatedContainerListProvider = new DependencyValidatedContainerListProvider(
        sortedContainerListProvider
    );

    IContainerListProvider validContainerListProvider = new ValidContainerListProvider(
        dependencyValidatedContainerListProvider
    );

    IContainerListProvider initializedContainerListProvider = new InitializedContainerListProvider(
        validContainerListProvider,
        new ClassLoaderFactoryProvider(
            new ClassFilterPredicate(
                this.getClassLoaderClassFilters()
            ),
            new DefaultClassInterceptorFactory(
                this.getClassIntercepts()
            ),
            this.getByteCodeTransformer(),
            new InputStreamByteArrayConverter()
        )
    );

    IContainerMapProvider containerMapProvider = new DefaultContainerMapProvider(
        initializedContainerListProvider,
        LinkedHashMap::new
    );

    return new SEService(
        new FolderLifecycleEventPlugin(
            this.getFolderLifecycleEventHandlers()
        ),
        containerMapProvider.getContainerMap()
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
