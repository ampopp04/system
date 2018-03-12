/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.util.collection;

import java.util.AbstractMap;
import java.util.Map;

/**
 * The <class>Pair</class> defines
 * an object that holds a key and value
 *
 * @author Andrew
 */
public class Pair<K, V> extends AbstractMap.SimpleEntry<K, V> {

    /**
     * Construct the key value pair object
     *
     * @param key
     * @param value
     */
    public Pair(K key, V value) {
        super(key, value);
    }

    /**
     * Construct the key value pair object from a map entry object
     *
     * @param entry
     */
    public Pair(Map.Entry entry) {
        super(entry);
    }

    /**
     * Create a new pair object from a key and value
     *
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Pair<K, V> newPair(K key, V value) {
        return new Pair(key, value);
    }
}
