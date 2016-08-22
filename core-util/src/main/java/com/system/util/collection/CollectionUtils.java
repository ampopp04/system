package com.system.util.collection;

import com.system.util.compare.CompareUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.system.util.stream.StreamUtils.stream;
import static com.system.util.stream.StreamUtils.streamIterate;

/**
 * The <class>CollectionUtils</class> defines
 * collection related utility methods
 *
 * @author Andrew
 */
public class CollectionUtils {

    /**
     * Create our standard empty ArrayList
     *
     * @return
     */
    public static List newList() {
        return new ArrayList<>();
    }

    /**
     * Creates our standard empty HashMap
     *
     * @return
     */
    public static Map newMap() {
        return new HashMap<>();
    }

    /**
     * Returns a new list if the provided one is null
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> List<E> newListIfNull(List<E> list) {
        if (list == null) {
            return newList();
        }
        return list;
    }

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
     * Iterates on elements within a collection applying the supplied action against each element
     *
     * @param collection
     * @param action
     * @param <E>
     */
    public static <E> void iterate(Iterable<E> collection, Consumer<? super E> action) {
        streamIterate(stream(collection), action);
    }

    /**
     * Iterates on elements within an iterator applying the supplied action against each element
     *
     * @param iterator
     * @param action
     * @param <E>
     */
    public static <E> void iterate(Iterator<E> iterator, Consumer<? super E> action) {
        streamIterate(stream(iterator), action);
    }

    /**
     * Retrieve map key iterator from a map. This will perform a null check.
     *
     * @param map
     * @param <K>
     * @return
     */
    public static <K> Iterable<K> iterable(Map<K, ?> map) {
        return map == null ? Collections.emptyList() : map.keySet();
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
     * For a given iterable return the first element
     * where the function applied against E is equal to value
     *
     * @param iterable
     * @param function
     * @param value
     * @param <E>
     * @return
     */
    public static <E> E firstEquals(Iterable<E> iterable, Function<E, ?> function, Object value) {
        if (iterable == null) return null;
        for (E element : iterable) if (CompareUtils.equals(function.apply(element), value)) return element;
        return null;
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
        return values == null ? Collections.emptyList() : new ArrayList<>(Arrays.asList(values));
    }

    /**
     * Convert an maps values into a list by
     * aggregating all of the values
     *
     * @param keyMap
     * @param <K>
     * @return
     */
    public static <K> List<K> asKeyList(Map<K, ?> keyMap) {
        return keyMap == null ? Collections.emptyList() : new ArrayList<>(keyMap.keySet());
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

    /**
     * Add an element to a collection safely. This
     * skips NULL elements.
     *
     * @param collection
     * @param element
     * @param <E>
     */
    public static <E> void add(Collection<E> collection, E element) {
        if (element != null && collection != null) {
            collection.add(element);
        }
    }
}
