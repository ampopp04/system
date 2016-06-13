package com.system.db.util.repository;

import com.system.inversion.util.InversionUtils;
import com.system.manipulator.object.creation.util.ObjectCreationUtils;
import com.system.util.clazz.ClassUtils;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.HashMap;

import static com.system.db.util.repository.RepositoryUtils.*;

/**
 * The <class>RepositoryRegistrationUtils</class> defines
 * repository registration related utility methods
 *
 * @author Andrew
 */
public class RepositoryRegistrationUtils {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    public static final Class REPOSITORY_DEFINITION_ANNOTATION = RepositoryDefinition.class;
    public static final Class REPOSITORY_REST_RESOURCE =
            ClassUtils.forNameOrNull("org.springframework.data.rest.core.annotation.RepositoryRestResource");

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Util Methods                                                     //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Register a repository that is applicable for the given entity.
     * <p>
     * This will either be a named or base repository implementation based on the type of entity.
     *
     * @param registry
     * @param beanName
     * @param entityType
     */
    public static BeanDefinition registerRepositoryImplBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class<?> entityType) {
        Class entityRepositoryInterface = createEntityRepositoryInterface(entityType);
        //registerRepositoryImpl(registry, beanName, entityType, createRepositoryImpl(entityType, entityRepositoryInterface));
        return InversionUtils.createBeanDefinition(entityRepositoryInterface);
    }

    /**
     * Create entity repository interface for a given entity type
     *
     * @param entityType
     * @return
     */
    public static Class createEntityRepositoryInterface(Class<?> entityType) {
        AnnotationDescription repositoryRestResource = ObjectCreationUtils.buildAnnotationDescription(REPOSITORY_REST_RESOURCE, null);
        AnnotationDescription repositoryDefinition = ObjectCreationUtils.buildAnnotationDescription(REPOSITORY_DEFINITION_ANNOTATION, new HashMap<String, Class<?>>() {{
            put("domainClass", entityType);
            put("idClass", entityType);
        }});

        return ObjectCreationUtils.extendInterface(getRepositoryInterfaceName(entityType), getRepositoryClass(entityType, true), entityType, repositoryDefinition, repositoryRestResource);
    }

    /**
     * Create entity repository interface implementation for a given entity type
     *
     * @param entityType
     * @param repositoryInterface
     * @return
     */
    private static Class createRepositoryImpl(Class<?> entityType, Class repositoryInterface) {
        return ObjectCreationUtils.subclass(getRepositoryImplName(entityType), getRepositoryClass(entityType, false), repositoryInterface, entityType);
    }

    /**
     * Register repository implementation bean definition into the registry
     *
     * @param registry
     * @param beanName
     * @param entityType
     * @param repositoryImpl
     */
    private static void registerRepositoryImpl(BeanDefinitionRegistry registry, String beanName, Class<?> entityType, Class repositoryImpl) {
        MutablePropertyValues properties = new MutablePropertyValues();
        properties.addPropertyValue("entityType", entityType);
        InversionUtils.registerBeanDefinition(registry, beanName, repositoryImpl, properties);
    }
}
