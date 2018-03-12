/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.inversion.intercept;


import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;

import static com.system.util.clazz.ClassUtils.*;


/**
 * The <class>SimpleClasspathMetadataReader</class>
 * reads class metadata directly from the classpath
 *
 * @author Andrew
 */
public class SimpleClasspathMetadataReader implements MetadataReader {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The class for which we are reading metadata for
     */
    private Class clazz;
    /**
     * The resource stream representing the class object
     */
    private Resource resource;
    /**
     * The class metadata
     */
    private ClassMetadata classMetadata;
    /**
     * The class annotation metadata
     */
    private AnnotationMetadata annotationMetadata;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Constructors                                                  //////////
    //////////////////////////////////////////////////////////////////////

    public SimpleClasspathMetadataReader(Resource classResource) throws IOException {
        this.resource = classResource;
        getMetadata(toClassName(classResource.getInputStream()));
    }

    public SimpleClasspathMetadataReader(String className) {
        getMetadata(className);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                Advanced Getters                                              //////////
    //////////////////////////////////////////////////////////////////////

    private void getMetadata(String className) {
        this.clazz = forNameSafe(className);
        if (this.resource == null) {
            this.resource = new InputStreamResource(toInputStreamSafe(clazz));
        }
        this.annotationMetadata = getMetadata(clazz);
        this.classMetadata = annotationMetadata;
    }

    private AnnotationMetadata getMetadata(Class clazz) {
        try {
            return new StandardAnnotationMetadata(clazz);
        } catch (Throwable e) {
            return new StandardAnnotationMetadata(Object.class);
        }
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Basic   Getters                                               //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }
}

