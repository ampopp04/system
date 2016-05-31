package com.system.util.collection;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * The <class>CollectionUtils</class> defines
 * collection related utility methods
 *
 * @author Andrew
 */
public class CollectionUtils {

    /**
     * Retrieve iterator from a collection. This will perform a null check.
     *
     * @param collection
     * @param <E>
     * @return
     */
    public static <E> Iterable<E> iterable(Collection<E> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }

    /**
     * Retrieve iterator from an array. This will perform a null check.
     *
     * @param collection
     * @param <E>
     * @return
     */
    public static <E> Iterable<E> iterable(E[] collection) {
        return collection == null ? Collections.emptyList() : new ArrayList<>(Arrays.asList(collection));
    }

    /**
     * Get size of an iterable
     *
     * @param iterable
     * @return
     */
    public static int size(Iterable<?> iterable) {
        if (iterable instanceof Collection)
            return ((Collection<?>) iterable).size();

        // else iterate

        int i = 0;
        for (Object ignored : iterable) i++;
        return i;
    }

    /**
     * Convert array to list
     *
     * @param values
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> asList(T... values) {
        return values == null ? Collections.emptyList() : Arrays.asList(values);
    }

    /**
     * Convert an maps values into a list by
     * aggregating all of the values
     *
     * @param valuesMap
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(Map<?, T> valuesMap) {
        return valuesMap == null ? Collections.emptyList() : new ArrayList<>(valuesMap.values());
    }

    /**
     * Convert a collection to an array
     *
     * @param collection
     * @param <E>
     * @return
     */
    public static <E> E[] asArray(Collection<E> collection) {
        return empty(collection) ? ArrayUtils.toArray() : collection.toArray((E[]) Array.newInstance(getRootSuperclass(firstElement(collection).getClass()), collection.size()));
    }

    /**
     * Traverses the {@link Class} inheritance hierarchy and
     * returns back the {@link Class} immediately prior to {@link Object}
     * for the given {@link Class}
     *
     * @param clazz
     * @return
     */
    private static Class<?> getRootSuperclass(Class<?> clazz) {
        Class<?> superClass = clazz;
        while (superClass != null && superClass.getSuperclass() != Object.class) {
            superClass = superClass.getSuperclass();
        }
        return superClass;
    }

    /**
     * Get the first element of the collection
     *
     * @param collection
     * @param <E>
     * @return NULL if empty else iterator first element
     */
    public static <E> E firstElement(Collection<E> collection) {
        return empty(collection) ? null : collection.iterator().next();
    }

    /**
     * Determines if the collection is null or size is zero
     *
     * @param collection
     * @param <E>
     * @return
     */
    public static <E> boolean empty(Collection<E> collection) {
        return collection == null || collection.size() == 0;
    }
}
