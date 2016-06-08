package com.system.db.util.entity;

import java.beans.Introspector;

/**
 * The <class>EntityUtils</class> defines
 * entity related utility methods
 *
 * @author Andrew
 */
public class EntityUtils {

    /**
     * Denotes the postfix name for a repository
     */
    public static final String ENTITY_REPOSITORY_POSTFIX = "Repository";

    /**
     * Converts a entity name into it's associated repository name
     *
     * @param entityName
     * @return
     */
    public static String getRepositoryName(String entityName) {
        String repositoryBeanName = entityName + ENTITY_REPOSITORY_POSTFIX;
        return Introspector.decapitalize(repositoryBeanName);
    }
}
