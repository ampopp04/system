/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.util.stream;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The <class>StreamUtils</class> defines
 * stream related utility methods
 *
 * @author Andrew
 */
public class StreamUtils {

    /**
     * Iterates on elements within a stream applying the supplied action against each element
     *
     * @param stream
     * @param action
     * @param <E>
     */
    public static <E> void streamIterate(Stream<E> stream, Consumer<? super E> action) {
        if (stream != null) {
            stream.forEach(action);
        }
    }

    /**
     * Convert a collection into a stream
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> Stream<T> stream(Iterable<T> collection) {
        return (collection == null) ? Stream.empty() : StreamSupport.stream(collection.spliterator(), false);
    }

    /**
     * Convert an iterator into a stream
     *
     * @param iterator
     * @param <T>
     * @return
     */
    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return (iterator == null) ? Stream.empty() : StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }
}
