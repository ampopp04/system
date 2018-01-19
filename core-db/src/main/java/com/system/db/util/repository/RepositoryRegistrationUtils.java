package com.system.db.util.repository;

import com.system.inversion.util.InversionUtils;
import com.system.manipulator.object.creation.util.ObjectCreationUtils;
import com.system.util.clazz.ClassUtils;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.data.repository.RepositoryDefinition;

import java.io.Serializable;
import java.util.HashMap;

import static com.system.db.entity.identity.EntityIdentity.ID_TYPE_NAME_UPPERCASE;
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
     * @param entityType
     */
    public static BeanDefinition registerRepositoryImplBeanDefinition(Class<?> entityType) {
        Class entityRepositoryInterface = createEntityRepositoryInterface(entityType);
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
        AnnotationDescription repositoryDefinition = null;
        Class entityRepositoryInterfaceClass = null;

        try {

            final Class<? extends Serializable> idType = (Class<? extends Serializable>) ClassUtils.getGenericTypeArgument(entityType, ID_TYPE_NAME_UPPERCASE);

                    repositoryDefinition = ObjectCreationUtils.buildAnnotationDescription(REPOSITORY_DEFINITION_ANNOTATION, new HashMap<String, Class<?>>() {{
                put("domainClass", entityType);
                put("idClass", idType);

            }});

            Class repositoryClass = getRepositoryClass(entityType);

            entityRepositoryInterfaceClass = ObjectCreationUtils.extendInterface(getRepositoryInterfaceName(entityType), repositoryClass, entityType, (isSystemRepositoryClass(repositoryClass)) ? idType : null, repositoryDefinition, repositoryRestResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityRepositoryInterfaceClass;
    }
}
