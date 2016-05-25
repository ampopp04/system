package com.system.util.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The <class>CollectionUtilsTests</class> defines
 * collection related tests
 *
 * @author Andrew
 */
public class CollectionUtilsTests {

    @Test
    public void iterableNotNullTest() {
        assertNotNull(CollectionUtils.iterable((Object[]) null));
    }

    @Test
    public void iterableExistsTest() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");

        assertNotNull(CollectionUtils.iterable(stringList));
    }

    @Test
    public void iterableExists1Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");

        assertNotNull(CollectionUtils.iterable(stringList));
    }

    @Test
    public void iterableSize1Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");

        Iterable<String> iterable = CollectionUtils.iterable(stringList);
        assertNotNull(iterable);

        assertEquals(1, CollectionUtils.size(iterable));
    }

    @Test
    public void iterableSize2Test() {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");

        Iterable<String> iterable = CollectionUtils.iterable(stringList);
        assertNotNull(iterable);
        assertEquals(2, CollectionUtils.size(iterable));
    }
}