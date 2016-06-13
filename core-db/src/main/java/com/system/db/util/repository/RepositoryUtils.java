package com.system.db.util.repository;

import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.util.clazz.ClassUtils;

import static com.system.util.string.StringUtils.decapitalize;

/**
 * The <class>RepositoryUtils</class> defines
 * repository related utility methods
 *
 * @author Andrew
 */
public class RepositoryUtils {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Denotes the postfix name for a repository
     */
    public static final String ENTITY_REPOSITORY_POSTFIX = "Repository";

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic Util Methods                                                //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Converts a entity name into it's associated repository name
     *
     * @param entityType
     * @return
     */
    public static String getRepositoryName(Class<?> entityType) {
        return decapitalize(getRepositoryInterfaceName(entityType));
    }

    /**
     * Returns repository interface name for a given entity type
     *
     * @param entityType
     * @return
     */
    public static String getRepositoryInterfaceName(Class<?> entityType) {
        return entityType.getSimpleName() + ENTITY_REPOSITORY_POSTFIX;
    }

    /**
     * Returns repository interface implementation name for a given entity type
     *
     * @param entityType
     * @return
     */
    public static String getRepositoryImplName(Class<?> entityType) {
        return entityType.getSimpleName() + ENTITY_REPOSITORY_POSTFIX + "impl";
    }

    /**
     * Returns the Repository interface class for
     * a given entity type
     *
     * @param entityType
     * @return
     */
    public static Class getRepositoryClass(Class<?> entityType) {
        return ClassUtils.isAssignable(entityType, NamedEntity.class) ? NamedEntityRepository.class : SystemRepository.class;
    }
}
