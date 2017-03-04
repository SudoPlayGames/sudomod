package com.sudoplay.sudoext.service;

import com.sudoplay.sudoext.api.ContainerAPI;
import com.sudoplay.sudoext.api.logging.Slf4jLoggerAPIProvider;
import com.sudoplay.sudoext.candidate.*;
import com.sudoplay.sudoext.candidate.extractor.ZipFileExtractionPathProvider;
import com.sudoplay.sudoext.candidate.extractor.ZipFileExtractor;
import com.sudoplay.sudoext.classloader.asm.callback.BudgetCallbackDelegate;
import com.sudoplay.sudoext.classloader.asm.callback.ICallbackDelegate;
import com.sudoplay.sudoext.classloader.asm.transform.IByteCodeTransformer;
import com.sudoplay.sudoext.classloader.asm.transform.SEByteCodeTransformerBuilder;
import com.sudoplay.sudoext.classloader.filter.IClassFilter;
import com.sudoplay.sudoext.classloader.intercept.ClassIntercept;
import com.sudoplay.sudoext.classloader.intercept.DelegateClassInterceptProcessor;
import com.sudoplay.sudoext.classloader.intercept.IClassInterceptProcessor;
import com.sudoplay.sudoext.classloader.intercept.StaticFieldClassInterceptProcessor;
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

  private ICallbackDelegate callbackDelegate;
  private IContainerCacheFactory containerCacheFactory;
  private IByteCodeTransformer byteCodeTransformer;

  private List<IClassFilter> defaultClassLoaderClassFilterList;
  private List<IClassFilter> classLoaderClassFilterList;

  public SEServiceBuilder(SEConfigBuilder configBuilder) {
    this.config = PreCondition.notNull(configBuilder).getConfig();

    // init user-defined lists
    this.classLoaderClassFilterList = new ArrayList<>();

    // init component objects
    this.containerCacheFactory = new LRUContainerCacheFactory(64);

    // init the bytecode transformer
    this.byteCodeTransformer = new SEByteCodeTransformerBuilder()
        .create();

    // adds the default class filter
    this.defaultClassLoaderClassFilterList = new ArrayList<>();

    this.callbackDelegate = new BudgetCallbackDelegate();
  }

  public SEServiceBuilder setCallbackDelegate(@NotNull ICallbackDelegate callbackDelegate) {
    this.callbackDelegate = PreCondition.notNull(callbackDelegate);
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

  public SEService create() throws SEServiceInitializationException {

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
                    new RecursiveFileRemovalProcessor()
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
            new OptionalRegisterAdapter()
        },
        new IMetaValidator[]{
            new IdValidator(),
            new ApiVersionValidator(this.config.getApiVersion()),
            new DependsOnValidator(),
            new JarValidator(),
            new RegisterValidator(
                new ZipSearch()
            )
        },
        this.containerCacheFactory,
        this.getClassLoaderClassFilters(),
        new ClassIntercept[]{
            new ClassIntercept(
                ContainerAPI.class,
                new DelegateClassInterceptProcessor(
                    new IClassInterceptProcessor[]{
                        new StaticFieldClassInterceptProcessor(
                            "LOGGING_API_PROVIDER",
                            container -> new Slf4jLoggerAPIProvider(container.getId())
                        ),
                        new StaticFieldClassInterceptProcessor(
                            "ID",
                            Container::getId
                        )
                    }
                )
            )
        },
        this.callbackDelegate,
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
                new RecursiveFileRemovalProcessor()
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
