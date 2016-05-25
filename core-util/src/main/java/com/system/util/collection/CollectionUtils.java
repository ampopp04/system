package com.system.util.collection;

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
}
