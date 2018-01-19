package com.system.ws.projection.util;

import com.system.manipulator.object.creation.util.ObjectCreationUtils;
import com.system.util.string.StringUtils;
import org.springframework.data.rest.core.config.ProjectionDefinitionConfiguration;
import org.springframework.data.rest.core.projection.ProjectionDefinitions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.system.util.collection.CollectionUtils.iterate;
import static com.system.util.collection.CollectionUtils.newMap;
import static com.system.util.string.StringUtils.*;


/**
 * The <class>DynamicProjectionUtils</class> defines
 * projection related utility methods.
 *
 * @author Andrew
 * @see org.springframework.data.rest.core.config.Projection
 */
public class DynamicProjectionUtils {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * This field defines the property that can be included in a server request to activate the
     * dynamic projection
     */
    public static final String DYNAMIC_PROJECTION_FIELDS = "dynamicProjectionFields";

    /**
     * If DYNAMIC_PROJECTION_FIELDS is set then specific fields to dynamically include in the
     * response will be separated by the following separated character.
     */
    public static final String DYNAMIC_PROJECTION_FIELDS_SEPARATOR = ":";

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                         //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * This method will return a specific projection or create and cache it if it doesn't exist
     *
     * @param source
     * @param projection
     * @param projectionDefinitions
     * @return
     */
    public static Class<?> getProjection(Object source, String projection, ProjectionDefinitions projectionDefinitions) {
        String projectionName = isDynamicProjection(projection) ? getDynamicProjectionName(source, projection) : projection;

        Class<?> projectionType = projectionDefinitions.getProjectionType(source.getClass(), projectionName);

        if (projectionType == null && isDynamicProjection(projection)) {
            //Dynamically create and set projection type
            projectionType = createDynamicProjection(source, projection);

            //Add newly created projection to cache to future refuse
            addProjectionToCache(source, projectionType, projection, projectionDefinitions);
        }

        return projectionType;
    }

    /**
     * Checks a projection name to see if it starts with  DYNAMIC_PROJECTION_FIELDS
     *
     * @param projection
     * @return
     */
    public static boolean isDynamicProjection(String projection) {
        return StringUtils.startsWithIgnoreCase(projection, DYNAMIC_PROJECTION_FIELDS);
    }

    /**
     * Caches a created projection for future retrieval
     *
     * @param source
     * @param targetType
     * @param projection
     * @param projectionDefinitions
     */
    public static void addProjectionToCache(Object source, Class<?> targetType, String projection, ProjectionDefinitions projectionDefinitions) {
        Class<?> sourceType = source.getClass();
        ((ProjectionDefinitionConfiguration) projectionDefinitions).addProjection(targetType, getDynamicProjectionName(source, projection), sourceType);
    }

    /**
     * Set the name of the dynamic projection to be the class named appended
     * with all the fields specific to this dynamic projection
     *
     * @param source
     * @param projection
     * @return
     */
    public static String getDynamicProjectionName(Object source, String projection) {
        return source.getClass().getSimpleName() + "_" + replace(replace(projection, ":", "_"), ",", "_");
    }

    /**
     * Create a new dynamic projection through our byte code manipulator.
     * This will actually create the Projection interface on the fly
     *
     * @param source
     * @param projection
     * @return
     */
    public static Class<?> createDynamicProjection(Object source, String projection) {
        Set<String> attributes = StringUtils.commaDelimitedListToSet(removeBeforeInclusive(projection, DYNAMIC_PROJECTION_FIELDS_SEPARATOR));
        return ObjectCreationUtils.createInterfaceWithPublicMethods(
                getDynamicProjectionName(source, projection),
                getGetterMethodsFromPrimaryNames(source, attributes), source.getClass().getClassLoader());
    }

    /**
     * Create the getter method names to be used in the byte code generated interface
     *
     * @param source
     * @param primaryMethodNames
     * @return
     */
    public static Map<Method, Annotation[]> getGetterMethodsFromPrimaryNames(Object source, Collection<String> primaryMethodNames) {
        Map<Method, Annotation[]> matchedMethodAnnotationMap = newMap();

        iterate(primaryMethodNames, (methodName) -> {
            try {
                Field field = org.apache.commons.lang3.reflect.FieldUtils.getField(source.getClass(), methodName, true);
                Annotation[] annotations = field == null ? new Annotation[0] : field.getDeclaredAnnotations();
                matchedMethodAnnotationMap.put(source.getClass().getMethod(convertPropertyNameToGetterMethodName(methodName)), annotations == null ? new Annotation[0] : annotations);
            } catch (NoSuchMethodException e) {
            }
        });

        return matchedMethodAnnotationMap;
    }

    /**
     * Convert a specific property name to it's given getter method name
     *
     * @param propertyName
     * @return
     */
    public static String convertPropertyNameToGetterMethodName(String propertyName) {
        return "get" + capitalize(propertyName);
    }
}