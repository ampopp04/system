package com.system.util.collection;

import java.util.*;

public class CollectionUtils {

    /**
     * Retrieve iterator from a collection. This will perform a null check.
     */
    public static <E> Iterable<E> getIterable(Collection<E> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }

    public static <E> Iterable<E> getIterable(E[] collection) {
        return collection == null ? Collections.emptyList() : new ArrayList<>(Arrays.asList(collection));
    }

    public static int getSize(Iterable<?> iterable) {
        if (iterable instanceof Collection)
            return ((Collection<?>) iterable).size();

        // else iterate

        int i = 0;
        for (Object object : iterable) i++;
        return i;
    }

}
