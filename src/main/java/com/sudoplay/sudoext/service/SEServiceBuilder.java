package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.ContainerAPI;
import com.sudoplay.sudoext.api.logging.Slf4jLoggerAPIProvider;
import com.sudoplay.sudoext.candidate.extractor.ICompressedCandidateExtractor;
import com.sudoplay.sudoext.candidate.extractor.ZipCompressedCandidateExtractor;
import com.sudoplay.sudoext.candidate.locator.CandidateCompressedFileLocator;
import com.sudoplay.sudoext.candidate.locator.CandidateFolderLocator;
import com.sudoplay.sudoext.candidate.locator.ICandidateLocator;
import com.sudoplay.sudoext.classloader.intercept.ClassIntercept;
import com.sudoplay.sudoext.classloader.intercept.DelegateClassInterceptProcessor;
import com.sudoplay.sudoext.classloader.intercept.IClassInterceptProcessor;
import com.sudoplay.sudoext.classloader.intercept.StaticFieldClassInterceptProcessor;
import com.sudoplay.sudoext.container.*;
import com.sudoplay.sudoext.folder.DefaultFolderLifecycleInitializeEventHandler;
import com.sudoplay.sudoext.folder.IFolderLifecycleEventHandler;
import com.sudoplay.sudoext.folder.TempFolderLifecycleEventHandler;
import com.sudoplay.sudoext.meta.DefaultMetaFactory;
import com.sudoplay.sudoext.meta.IMetaFactory;
import com.sudoplay.sudoext.meta.parser.IMetaElementParser;
import com.sudoplay.sudoext.meta.parser.element.*;
import com.sudoplay.sudoext.meta.validator.IMetaValidator;
import com.sudoplay.sudoext.meta.validator.element.ApiVersionValidator;
import com.sudoplay.sudoext.meta.validator.element.DependsOnValidator;
import com.sudoplay.sudoext.meta.validator.element.IdValidator;
import com.sudoplay.sudoext.meta.validator.element.JarValidator;
import com.sudoplay.sudoext.security.IClassFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Creates a default implementation of the {@link SEService} with the given {@link SEConfig}.
 * <p>
 * Created by codetaylor on 2/20/2017.
 */
public class SEServiceBuilder {

  private SEConfig config;

  private IContainerCacheFactory containerCacheFactory;
  private IContainerSorter containerSorter;
  private IMetaFactory metaFactory;
  private ICompressedCandidateExtractor compressedCandidateExtractor;

  private List<IClassFilter> defaultClassFilterList;
  private List<ClassIntercept> defaultClassInterceptList;
  private List<IMetaElementParser> defaultMetaElementParserList;
  private List<IMetaValidator> defaultMetaValidatorList;
  private List<ICandidateLocator> defaultCandidateLocatorList;
  private List<IFolderLifecycleEventHandler> defaultFolderLifecycleEventHandlerList;

  private List<IClassFilter> classFilterList;
  private List<ClassIntercept> classInterceptList;
  private List<IMetaElementParser> metaElementParserList;
  private List<IMetaValidator> metaValidatorList;
  private List<ICandidateLocator> candidateLocatorList;
  private List<IFolderLifecycleEventHandler> folderLifecycleEventHandlerList;

  public SEServiceBuilder(SEConfigBuilder configBuilder) {
    this.config = configBuilder.getConfig();

    // init user-defined lists
    this.classFilterList = new ArrayList<>();
    this.classInterceptList = new ArrayList<>();
    this.metaElementParserList = new ArrayList<>();
    this.metaValidatorList = new ArrayList<>();
    this.candidateLocatorList = new ArrayList<>();
    this.folderLifecycleEventHandlerList = new ArrayList<>();

    this.initializeDefaults();
  }

  private void initializeDefaults() {

    // init component objects
    this.containerCacheFactory = new LRUContainerCacheFactory(64);
    this.containerSorter = new DefaultContainerSorter();
    this.metaFactory = new DefaultMetaFactory();
    this.compressedCandidateExtractor = new ZipCompressedCandidateExtractor();

    // adds the default folder lifecycle event handlers
    this.defaultFolderLifecycleEventHandlerList = new ArrayList<>();
    this.defaultFolderLifecycleEventHandlerList.add(
        new DefaultFolderLifecycleInitializeEventHandler(
            config.getLocation()
        )
    );
    this.defaultFolderLifecycleEventHandlerList.add(
        new DefaultFolderLifecycleInitializeEventHandler(
            config.getDataLocation()
        )
    );
    this.defaultFolderLifecycleEventHandlerList.add(
        new TempFolderLifecycleEventHandler(
            config.getTempLocation()
        )
    );

    // adds the default candidate locators
    this.defaultCandidateLocatorList = new ArrayList<>();
    this.defaultCandidateLocatorList.add(new CandidateFolderLocator(
        this.config.getMetaFilename()
    ));
    this.defaultCandidateLocatorList.add(new CandidateCompressedFileLocator(
        this.config.getMetaFilename(),
        this.config.getCompressedFileExtension()
    ));

    // adds the default meta element parsers
    this.defaultMetaElementParserList = new ArrayList<>();
    this.defaultMetaElementParserList.add(new IdParser());
    this.defaultMetaElementParserList.add(new NameParser());
    this.defaultMetaElementParserList.add(new AuthorParser());
    this.defaultMetaElementParserList.add(new VersionParser());
    this.defaultMetaElementParserList.add(new DescriptionParser());
    this.defaultMetaElementParserList.add(new OptionalWebsiteParser()); // optional
    this.defaultMetaElementParserList.add(new OptionalApiVersionParser()); // optional
    this.defaultMetaElementParserList.add(new OptionalDependsOnParser()); // optional
    this.defaultMetaElementParserList.add(new OptionalJarParser()); // optional

    // adds the default meta validators
    this.defaultMetaValidatorList = new ArrayList<>();
    this.defaultMetaValidatorList.add(new IdValidator());
    this.defaultMetaValidatorList.add(new ApiVersionValidator(this.config.getApiVersion()));
    this.defaultMetaValidatorList.add(new DependsOnValidator());
    this.defaultMetaValidatorList.add(new JarValidator());

    // adds the default class filter blacklist
    this.defaultClassFilterList = new ArrayList<>();
    this.defaultClassFilterList.add(name -> {
      switch (name) {
        case "java.lang.Thread":
          return false;
      }
      return true;
    });

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
                    "META",
                    Container::getMeta
                )
            }
        )
    ));

  }

  public SEServiceBuilder addClassFilter(IClassFilter filter) {
    this.classFilterList.add(filter);
    return this;
  }

  public SEServiceBuilder removeAllDefaultClassFilters() {
    this.defaultClassFilterList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultClassFilter(Class<? extends IClassFilter> aClass) {
    return this.removeByClass(aClass, this.defaultClassFilterList);
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

  public SEServiceBuilder setContainerSorter(IContainerSorter sorter) {
    this.containerSorter = sorter;
    return this;
  }

  public SEServiceBuilder setMetaFactory(IMetaFactory metaFactory) {
    this.metaFactory = metaFactory;
    return this;
  }

  public SEServiceBuilder addMetaElementParser(IMetaElementParser parser) {
    this.metaElementParserList.add(parser);
    return this;
  }

  public SEServiceBuilder removeAllDefaultMetaElementParsers() {
    this.defaultMetaElementParserList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultMetaElementParser(Class<? extends IMetaElementParser> aClass) {
    return this.removeByClass(aClass, this.defaultMetaElementParserList);
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

  public SEServiceBuilder setCompressedCandidateExtractor(ICompressedCandidateExtractor extractor) {
    this.compressedCandidateExtractor = extractor;
    return this;
  }

  public SEServiceBuilder addCandidateLocator(ICandidateLocator locator) {
    this.candidateLocatorList.add(locator);
    return this;
  }

  public SEServiceBuilder removeAllDefaultCandidateLocators() {
    this.defaultCandidateLocatorList.clear();
    return this;
  }

  public SEServiceBuilder removeDefaultCandidateLocator(Class<? extends ICandidateLocator> aClass) {
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

  /* package */ IClassFilter[] getClassFilters() {
    List<IClassFilter> list = new ArrayList<>();
    list.addAll(this.defaultClassFilterList);
    list.addAll(this.classFilterList);
    return list.toArray(new IClassFilter[list.size()]);
  }

  /* package */ ClassIntercept[] getClassIntercepts() {
    List<ClassIntercept> list = new ArrayList<>();
    list.addAll(this.defaultClassInterceptList);
    list.addAll(this.classInterceptList);
    return list.toArray(new ClassIntercept[list.size()]);
  }

  /* package */ IMetaElementParser[] getMetaElementParsers() {
    List<IMetaElementParser> list = new ArrayList<>();
    list.addAll(this.defaultMetaElementParserList);
    list.addAll(this.metaElementParserList);
    return list.toArray(new IMetaElementParser[list.size()]);
  }

  /* package */ IMetaValidator[] getMetaValidators() {
    List<IMetaValidator> list = new ArrayList<>();
    list.addAll(this.defaultMetaValidatorList);
    list.addAll(this.metaValidatorList);
    return list.toArray(new IMetaValidator[list.size()]);
  }

  /* package */ ICandidateLocator[] getCandidateLocators() {
    List<ICandidateLocator> list = new ArrayList<>();
    list.addAll(this.defaultCandidateLocatorList);
    list.addAll(this.candidateLocatorList);
    return list.toArray(new ICandidateLocator[list.size()]);
  }

  /* package */ IFolderLifecycleEventHandler[] getFolderLifecycleEventHandlers() {
    List<IFolderLifecycleEventHandler> list = new ArrayList<>();
    list.addAll(this.defaultFolderLifecycleEventHandlerList);
    list.addAll(this.folderLifecycleEventHandlerList);
    return list.toArray(new IFolderLifecycleEventHandler[list.size()]);
  }

  /* package */ IContainerCacheFactory getContainerCacheFactory() {
    return this.containerCacheFactory;
  }

  /* package */ IContainerSorter getContainerSorter() {
    return this.containerSorter;
  }

  /* package */ IMetaFactory getMetaFactory() {
    return this.metaFactory;
  }

  /* package */ ICompressedCandidateExtractor getCompressedCandidateExtractor() {
    return this.compressedCandidateExtractor;
  }

  public SEService create() throws SEServiceInitializationException {
    return SEServiceFactory.create(this, this.config);
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
