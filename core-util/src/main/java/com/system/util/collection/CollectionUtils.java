package com.system.util.collection;

import java.util.*;

public class CollectionUtils {

    /**
     * Retrieve iterator from a collection. This will perform a null check.
     */
    public static <E> Iterable<E> getIterator(Collection<E> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }

    public static <E> Iterable<E> getIterator(E[] collection) {
        return collection == null ? Collections.emptyList() : new ArrayList<>(Arrays.asList(collection));
    }

}
