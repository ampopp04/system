package com.system.util.collection;

import java.util.*;

public class CollectionUtils {
    
    /**
     * Retrieve iterator from a collection. This will perform a null check.
     */
    public static <E> Iterator<E> getIterator(Collection<E> collection){
        return collection == null ? Collections.emptyIterator() : collection.iterator();
    }
}
