package com.system.inversion.intercept;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;

/**
 * The <class>SimpleClasspathMetadataReaderFactory</class>
 * creates instances to read class metadata directly from the classpath.
 * <p>
 * This class is used by CachingMetadataReaderFactory to cache these results
 * <p>
 * It's important to note that this class is used as an interceptor to {@link SimpleClasspathMetadataReader}
 * It is hot swapped at run time which means the class definition cannot change. Hence why resourceLoader is left null
 * and methods are not implemented.  This is to avoid the need to run the application with an attached java-agent
 *
 * @author Andrew
 * @see SimpleClasspathMetadataReader
 * @see CachingMetadataReaderFactory
 */
public class SimpleClasspathMetadataReaderFactory implements MetadataReaderFactory {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private final ResourceLoader resourceLoader = null;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Constructors                                                  //////////
    //////////////////////////////////////////////////////////////////////

    public SimpleClasspathMetadataReaderFactory() {
    }


    public SimpleClasspathMetadataReaderFactory(ResourceLoader resourceLoader) {
    }


    public SimpleClasspathMetadataReaderFactory(ClassLoader classLoader) {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                Advanced Getters                                              //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Get the {@link MetadataReader} by class name
     *
     * @param className
     * @return
     * @throws IOException
     */
    @Override
    public MetadataReader getMetadataReader(String className) throws IOException {
        return new SimpleClasspathMetadataReader(className);
    }

    /**
     * Get the {@link MetadataReader} by {@link Resource}
     *
     * @param resource
     * @return
     * @throws IOException
     */
    @Override
    public MetadataReader getMetadataReader(Resource resource) throws IOException {
        return new SimpleClasspathMetadataReader(resource);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Basic   Getters                                               //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Not implemented but cannot change class definition
     * in order to allow for hot swap VM intercept
     */
    public final ResourceLoader getResourceLoader() {
        throw new UnsupportedOperationException();
    }
}
