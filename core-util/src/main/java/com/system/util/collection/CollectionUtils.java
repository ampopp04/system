package com.system.util.collection;

import java.util.*;

public class CollectionUtils {
    
    /**
     * Retrieve iterator from a collection. This will perform a null check.
     */
    public static Iterator<?> getIterator(Collection<?> collection){
        return collection == null? Collections.emptyIterator() : collection.iterator();
    }
}
