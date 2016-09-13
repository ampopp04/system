package com.system.db.util.entity;

import com.system.db.entity.Entity;
import com.system.util.clazz.ClassUtils;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import static com.system.inversion.util.InversionUtils.getResourceProperties;
import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;
import static com.system.util.string.StringUtils.contains;
import static com.system.util.string.StringUtils.removeBefore;

/**
 * The <class>EntityUtils</class> defines
 * entity related utility methods
 *
 * @author Andrew
 */
public class EntityUtils {

    /**
     * Get all Jpa specific properties from the supplied environment
     *
     * @param env
     * @return
     */
    public static Map<String, Object> getJpaProperties(Environment env) {
        Map<String, Object> propertyMap = new HashMap<>();
        Map<String, Object> resourcePropertyMap = getResourceProperties(env);
        iterate(iterable(resourcePropertyMap), (k) -> {
            if (contains(k, "hibernate")) {
                propertyMap.put(removeBefore(k, "hibernate"), resourcePropertyMap.get(k));
            }
        });

        return propertyMap;
    }

    /**
     * Returns whether a class is assignable to our Entity interface
     *
     * @param clazz
     * @return
     */
    public static boolean isEntityClass(Class clazz) {
        return ClassUtils.isAssignable(clazz, Entity.class);
    }

}
