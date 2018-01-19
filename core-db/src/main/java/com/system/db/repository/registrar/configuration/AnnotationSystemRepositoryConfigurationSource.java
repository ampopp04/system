package com.system.db.repository.registrar.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.util.Streamable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.system.db.util.repository.SystemRepositoryConfigurationUtils.processCustomEntities;
import static com.system.util.collection.CollectionUtils.*;
import static com.system.util.string.StringUtils.substringAfterLastDot;

/**
 * The <class>AnnotationSystemRepositoryConfigurationSource</class> sub-classes
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
     * @param metadata       must not be {@literal null}.
     * @param annotation     must not be {@literal null}.
     * @param resourceLoader must not be {@literal null}.
     * @param environment
     * @param registry
     */
    public AnnotationSystemRepositoryConfigurationSource(AnnotationMetadata metadata, Class<? extends Annotation> annotation, ResourceLoader resourceLoader, Environment environment, BeanDefinitionRegistry registry) {
        super(metadata, annotation, resourceLoader, environment, registry);
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
    public Streamable<BeanDefinition> getCandidates(ResourceLoader loader) {
        List<BeanDefinition> result = super.getCandidates(loader).stream().collect(Collectors.toList());
        result.addAll(processCustomEntities(registry, beanDefinitionListToNameList(result)));
        return Streamable.of(result);
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
