package com.system.util.collection;

import java.util.ArrayList;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>PairList</class> defines
 * an object that holds a list of Pair objects
 *
 * @author Andrew
 */
public class PairList<K, V> extends ArrayList<Pair<K, V>> {

    /**
     * Create a pair list from an array of pairs
     *
     * @param pairs
     */
    private PairList(Pair<K, V>... pairs) {
        iterate(iterable(pairs), (pair) -> add(pair));
    }

    /**
     * Create a new pair list from an array of pairs
     *
     * @param pairs
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> PairList<K, V> newPairList(Pair<K, V>... pairs) {
        return new PairList(pairs);
    }
}
