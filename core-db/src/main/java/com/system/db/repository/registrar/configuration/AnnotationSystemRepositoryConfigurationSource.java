package com.system.db.repository.registrar.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.system.db.util.repository.SystemRepositoryConfigurationUtils.processCustomEntities;
import static com.system.util.collection.CollectionUtils.*;
import static com.system.util.string.StringUtils.substringAfterLastDot;

/**
 * The <interface>AnnotationSystemRepositoryConfigurationSource</interface> sub-classes
 * {@link AnnotationRepositoryConfigurationSource} to intercept the
 * {@link AnnotationRepositoryConfigurationSource#getCandidates} method
 * to inject system made repository candidates
 *
 * @author Andrew
 */
public class AnnotationSystemRepositoryConfigurationSource extends AnnotationRepositoryConfigurationSource {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The registry to which we will register new repositories
     */
    private BeanDefinitionRegistry registry;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Creates a new {@link AnnotationRepositoryConfigurationSource} from the given {@link AnnotationMetadata} and
     * annotation.
     *
     * @param metadata
     * @param annotation     must not be {@literal null}.
     * @param resourceLoader must not be {@literal null}.
     * @param environment
     */
    public AnnotationSystemRepositoryConfigurationSource(BeanDefinitionRegistry registry, AnnotationMetadata metadata, Class<? extends Annotation> annotation, ResourceLoader resourceLoader, Environment environment) {
        super(metadata, annotation, resourceLoader, environment);
        this.registry = registry;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Intercept Super                                                  //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Intercept the supers getCandidates method to process custom entities
     *
     * @param loader
     * @return
     */
    @Override
    public Collection<BeanDefinition> getCandidates(ResourceLoader loader) {
        Collection<BeanDefinition> result = super.getCandidates(loader);
        result.addAll(processCustomEntities(registry, beanDefinitionListToNameList(result)));
        return result;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Helper Methods                                                 //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Convert the list of already processed repositories to their names
     *
     * @param beanDefinitionList
     * @return
     */
    private List<String> beanDefinitionListToNameList(Collection<BeanDefinition> beanDefinitionList) {
        List<String> beanNameList = new ArrayList<>();
        iterate(iterable(beanDefinitionList),
                (beanDefinition) ->
                        add(beanNameList, substringAfterLastDot(beanDefinition.getBeanClassName())));

        return beanNameList;
    }
}
